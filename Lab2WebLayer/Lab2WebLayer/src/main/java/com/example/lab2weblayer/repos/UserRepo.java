package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

}
