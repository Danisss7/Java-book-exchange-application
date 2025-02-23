package com.example.labandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.labandroid.helpers.Constants;
import com.example.labandroid.helpers.RESTController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Tab3Fragment extends Fragment {

    private static final String TAG = "Tab3Fragment";
    private ListView borrowedBooksListView;
    private ArrayList<Book> borrowedBooksList;
    private BookAdapter borrowedBooksAdapter;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        userId = getActivity().getIntent().getStringExtra("userId");
        borrowedBooksListView = rootView.findViewById(R.id.borrowedBooksListView);
        borrowedBooksList = new ArrayList<>();
        borrowedBooksAdapter = new BookAdapter(getActivity(), borrowedBooksList);
        borrowedBooksListView.setAdapter(borrowedBooksAdapter);
        loadBorrowedBooks();
        borrowedBooksListView.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = borrowedBooksList.get(position);

            showBookDetailsDialog(selectedBook);
        });

        return rootView;
    }

    private void loadBorrowedBooks() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String url = Constants.GET_BORROWED_BOOKS + "/" + userId;
                String jsonResponse = RESTController.sendGet(url);

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    Log.d(TAG, "Full JSON Response: " + jsonResponse);

                    Gson gson = new Gson();
                    JsonArray booksArray = gson.fromJson(jsonResponse, JsonArray.class);

                    borrowedBooksList.clear();

                    for (int i = 0; i < booksArray.size(); i++) {
                        JsonObject bookJson = booksArray.get(i).getAsJsonObject();
                        int id = bookJson.get("id").getAsInt();
                        String publisher = bookJson.get("publisher").getAsString();
                        String title = bookJson.get("title").getAsString();
                        String author = bookJson.get("author").getAsString();
                        Book book = new Book(id, publisher, title, author);
                        borrowedBooksList.add(book);

                        Log.d(TAG, "Fetched book: " + book.toString());
                    }

                    getActivity().runOnUiThread(() -> borrowedBooksAdapter.notifyDataSetChanged());
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to load borrowed books", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error fetching borrowed books", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showBookDetailsDialog(Book selectedBook) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_unreserve_book_details, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.bookTitle);
        TextView authorTextView = dialogView.findViewById(R.id.bookAuthor);
        Button unreserveButton = dialogView.findViewById(R.id.unreserveBookButton);
        Button closeButton = dialogView.findViewById(R.id.closeDialogButton);

        titleTextView.setText(selectedBook.getTitle());
        authorTextView.setText(selectedBook.getAuthor());
        AlertDialog dialog = builder.create();
        unreserveButton.setOnClickListener(v -> {
            if (userId != null && !userId.isEmpty()) {
                unreserveBook(selectedBook.getId(), Integer.parseInt(userId), dialog);
            }
        });
        closeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void unreserveBook(int bookId, int clientId, AlertDialog dialog) {
        String url = Constants.UNRESERVE_BOOK + "/" + bookId;
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String response = RESTController.sendPut(url, "");
                getActivity().runOnUiThread(() -> {
                    if (response.contains("successfully")) {
                        Toast.makeText(getActivity(), "Book unreserved successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        updateContent();
                    } else {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error unreserving book", Toast.LENGTH_SHORT).show());
            }
        });
    }

    void updateContent() {
        loadBorrowedBooks();
    }
}