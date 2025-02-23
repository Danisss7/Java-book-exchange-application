package com.example.labandroid;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.labandroid.helpers.Constants;
import com.example.labandroid.helpers.RESTController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Tab1Fragment extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private ListView myBooks;
    private ArrayList<Book> booksList;
    private BookAdapter booksAdapter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        myBooks = rootView.findViewById(R.id.booksListView);
        booksList = new ArrayList<>();
        booksAdapter = new BookAdapter(getActivity(), booksList);
        myBooks.setAdapter(booksAdapter);

        loadBooksByUser();

        myBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = booksList.get(position);

            showBookDetailsDialog(selectedBook);
        });

        Button createBookButton = rootView.findViewById(R.id.createBookButton);
        createBookButton.setOnClickListener(v -> showCreateBookDialog());

        return rootView;
    }

    public void updateContent() {
        loadBooksByUser();
    }

    private List<Book> fetchBooksForUser(String userId) {
        List<Book> fetchedBooks = new ArrayList<>();
        return fetchedBooks;
    }

    private void showCreateBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create Book");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_book, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.inputTitle);
        EditText authorInput = dialogView.findViewById(R.id.inputAuthor);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String author = authorInput.getText().toString().trim();
            String ownerIdStr = userId.trim();

            if (!title.isEmpty() && !author.isEmpty() && !ownerIdStr.isEmpty()) {
                Book book = new Book(title, author, "test");

                book.setPageCount(0);
                book.setPublicationYear(0);

                int ownerId = Integer.parseInt(ownerIdStr);
                book.setOwnerId(ownerId);

                createBook(book);
            } else {
                Toast.makeText(getActivity(), "Please enter all required fields.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void createBook(Book book) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String bookJson = new Gson().toJson(book);
                Log.d("CreateBook", "Sending JSON: " + bookJson);

                String url = Constants.CREATE_BOOK;
                String response = RESTController.sendPost(url, bookJson);
                Log.d("CreateBook", "Server response: " + response);

                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Book created successfully!", Toast.LENGTH_SHORT).show();
                    loadBooksByUser();
                });
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to create book", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateBook(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Book");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_book, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.inputTitle);
        EditText authorInput = dialogView.findViewById(R.id.inputAuthor);

        titleInput.setText(book.getTitle());
        authorInput.setText(book.getAuthor());

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newTitle = titleInput.getText().toString().trim();
            String newAuthor = authorInput.getText().toString().trim();

            if (!newTitle.isEmpty() && !newAuthor.isEmpty()) {
                book.setTitle(newTitle);
                book.setAuthor(newAuthor);

                book.setPublisher("Updated Publisher");
                book.setIsbn("978-1234567890");
                book.setGenre("ADVENTURE");
                book.setPageCount(350);
                book.setLanguage("ENGLISH");
                book.setPublicationYear(2021);
                book.setFormat("HARDCOVER");
                book.setSummary("Updated summary of the book.");

                sendUpdatedBookToServer(book);
            } else {
                Toast.makeText(getActivity(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void sendUpdatedBookToServer(Book book) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                JsonObject bookJson = new JsonObject();

                bookJson.addProperty("title", book.getTitle());
                bookJson.addProperty("author", book.getAuthor());

                bookJson.addProperty("publisher", book.getPublisher() != null && !book.getPublisher().isEmpty() ? book.getPublisher() : "");
                bookJson.addProperty("isbn", book.getIsbn() != null && !book.getIsbn().isEmpty() ? book.getIsbn() : "");

                bookJson.addProperty("genre", book.getGenre() != null ? book.getGenre().toString() : "");
                bookJson.addProperty("pageCount", book.getPageCount() > 0 ? book.getPageCount() : 0);
                bookJson.addProperty("language", book.getLanguage() != null ? book.getLanguage().toString() : "");
                bookJson.addProperty("publicationYear", book.getPublicationYear() > 0 ? book.getPublicationYear() : 0);
                bookJson.addProperty("format", book.getFormat() != null ? book.getFormat().toString() : "");
                bookJson.addProperty("summary", book.getSummary() != null && !book.getSummary().isEmpty() ? book.getSummary() : "");

                String jsonPayload = bookJson.toString();
                Log.d("UpdateBook", "Sending JSON: " + jsonPayload);

                String url = Constants.UPDATE_BOOK + "/" + book.getId();
                String response = RESTController.sendPut(url, jsonPayload);

                Log.d("UpdateBook", "Server response: " + response);

                if (response.isEmpty()) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Empty response from server", Toast.LENGTH_SHORT).show());
                } else {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Book updated successfully!", Toast.LENGTH_SHORT).show();
                        loadBooksByUser();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to update book", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void deleteBook(Book book) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String url = Constants.DELETE_BOOK + "/" + book.getId();
                RESTController.sendDelete(url);
                getActivity().runOnUiThread(() -> {
                    booksList.remove(book);
                    booksAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Book deleted successfully", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error deleting book", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showBookDetailsDialog(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Book Details");

        StringBuilder message = new StringBuilder();
        message.append("Title: ").append(book.getTitle()).append("\n");
        message.append("Author: ").append(book.getAuthor()).append("\n");
        message.append("Publisher: ").append(book.getPublisher()).append("\n");

        builder.setMessage(message.toString());

        builder.setPositiveButton("Update", (dialog, which) -> {
            updateBook(book);
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            deleteBook(book);
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.create().show();
    }


    private void loadBooksByUser() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String url = Constants.GET_BOOKS_BY_OWNER + "/" + userId;
                String jsonResponse = RESTController.sendGet(url);

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    Log.d(TAG, "Full JSON Response: " + jsonResponse);

                    Gson gson = new Gson();
                    JsonArray booksArray = gson.fromJson(jsonResponse, JsonArray.class);

                    booksList.clear();

                    for (int i = 0; i < booksArray.size(); i++) {
                        JsonObject bookJson = booksArray.get(i).getAsJsonObject();
                        int id = bookJson.get("id").getAsInt();
                        String publisher = bookJson.get("publisher").getAsString();
                        String title = bookJson.get("title").getAsString();
                        String author = bookJson.get("author").getAsString();
                        Book book = new Book(id, publisher, title, author);
                        booksList.add(book);

                        Log.d(TAG, "Fetched book: " + book.toString());
                    }

                    getActivity().runOnUiThread(() -> {
                        booksAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Books loaded successfully", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to load books", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error fetching books", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
