package com.example.labandroid;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Tab2Fragment extends Fragment {

    private static final String TAG = "Tab2Fragment";
    private ListView availableBooksListView;
    private ArrayList<Book> availableBooksList;
    private BookAdapter availableBooksAdapter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);

        userId = getActivity().getIntent().getStringExtra("userId");

        availableBooksListView = rootView.findViewById(R.id.availableBooksListView);
        availableBooksList = new ArrayList<>();
        availableBooksAdapter = new BookAdapter(getActivity(), availableBooksList);
        availableBooksListView.setAdapter(availableBooksAdapter);

        availableBooksListView.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = availableBooksList.get(position);

            showBookDetailsDialog(selectedBook);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAvailableBooks();
    }

    private void loadAvailableBooks() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String url = Constants.GET_AVAILABLE_BOOKS + "/" + userId;
                String jsonResponse = RESTController.sendGet(url);

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    Log.d(TAG, "Full JSON Response: " + jsonResponse);

                    Gson gson = new Gson();
                    JsonArray booksArray = gson.fromJson(jsonResponse, JsonArray.class);

                    availableBooksList.clear();

                    for (int i = 0; i < booksArray.size(); i++) {
                        JsonObject bookJson = booksArray.get(i).getAsJsonObject();
                        int id = bookJson.get("id").getAsInt();
                        String publisher = bookJson.get("publisher").getAsString();
                        String title = bookJson.get("title").getAsString();
                        String author = bookJson.get("author").getAsString();
                        Book book = new Book(id, publisher, title, author);
                        availableBooksList.add(book);

                        Log.d(TAG, "Fetched book: " + book.toString());
                    }

                    getActivity().runOnUiThread(() -> {
                        updateContent();
                        Toast.makeText(getActivity(), "Available books loaded successfully", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to load available books", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error fetching available books", Toast.LENGTH_SHORT).show());
            }
        });
    }

    void updateContent() {
        availableBooksAdapter.notifyDataSetChanged();
    }

    private void showBookDetailsDialog(Book selectedBook) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reserve_book_details, null);
        builder.setView(dialogView);
        TextView titleTextView = dialogView.findViewById(R.id.bookTitle);
        TextView authorTextView = dialogView.findViewById(R.id.bookAuthor);
        Button reserveButton = dialogView.findViewById(R.id.reserveBookButton);
        Button closeButton = dialogView.findViewById(R.id.closeDialogButton);
        titleTextView.setText(selectedBook.getTitle());
        authorTextView.setText(selectedBook.getAuthor());
        AlertDialog dialog = builder.create();
        reserveButton.setOnClickListener(v -> {
            if (userId != null && !userId.isEmpty()) {
                reserveBook(selectedBook.getId(), Integer.parseInt(userId), dialog);
                loadAvailableBooks();
            }
        });
        closeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void reserveBook(int bookId, int clientId, AlertDialog dialog) {
        String url = Constants.RESERVE_BOOK + "/" + bookId + "/" + clientId;
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String response = RESTController.sendPut(url, "");
                getActivity().runOnUiThread(() -> {
                    if (response.contains("successfully")) {
                        Toast.makeText(getActivity(), "Book reserved successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadAvailableBooks();
                    } else {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Error reserving book", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
