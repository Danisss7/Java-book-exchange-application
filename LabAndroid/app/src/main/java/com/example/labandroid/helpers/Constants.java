package com.example.labandroid.helpers;

public class Constants {
    public static final String BASE_URL = "http://172.20.10.5:8080/";
    public static final String VALIDATE_USER = BASE_URL + "validateClient";
    public static final String GET_AVAILABLE_BOOKS = BASE_URL + "books/allAvailableBooks";
    public static final String GET_BOOKS_BY_OWNER = BASE_URL + "books/byOwner";
    public static final String DELETE_BOOK = BASE_URL + "books/deleteBook";
    public static final String CREATE_BOOK = BASE_URL + "books/createBookByOwnerID";
    public static final String UPDATE_BOOK = BASE_URL + "books/updateBook";
    public static final String RESERVE_BOOK = BASE_URL + "books/reserveBook";
    public static final String GET_BORROWED_BOOKS = BASE_URL + "books/allBorrowedBooks";
    public static final String UNRESERVE_BOOK = BASE_URL + "books/unreserveBook";
}
