package com.example.labandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private Context context;
    private List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleTextView = convertView.findViewById(android.R.id.text1);
        TextView authorTextView = convertView.findViewById(android.R.id.text2);

        titleTextView.setText(currentBook.getTitle());
        authorTextView.setText(currentBook.getAuthor());

        return convertView;
    }
}
