package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Book;
import com.example.lab2weblayer.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepo extends JpaRepository<Client, Integer> {
    Client getClientByLoginAndPassword(String login, String password);
    Optional<Client> findTopByOrderByIdDesc();
    Client getClientByLogin(String login);
}
