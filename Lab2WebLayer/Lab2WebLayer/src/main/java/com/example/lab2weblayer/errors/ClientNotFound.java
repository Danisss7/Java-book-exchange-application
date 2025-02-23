package com.example.lab2weblayer.errors;

public class ClientNotFound extends RuntimeException {

    public ClientNotFound(Integer id) {
        super("Could not find client " + id);
    }
}
