package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Admin;
import com.example.lab2weblayer.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
}
