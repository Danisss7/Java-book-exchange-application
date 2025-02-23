package com.example.lab2weblayer.errors;

public class UserNotFound extends RuntimeException {

    public UserNotFound(Integer id) {
        super("Could not find user " + id);
    }
}
