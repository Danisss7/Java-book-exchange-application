package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Optional<Admin> findTopByOrderByIdDesc();
    Optional<Admin> findByLoginAndPassword(String login, String password);
    Optional<Admin> findByLogin(String login);
}
