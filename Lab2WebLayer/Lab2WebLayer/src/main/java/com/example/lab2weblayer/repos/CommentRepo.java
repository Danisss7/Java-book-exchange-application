package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Client;
import com.example.lab2weblayer.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
    List<Comment> findByChatId(Integer chatId);
    List<Comment> findByClientId(Integer clientId);
    Optional<Comment> findTopByOrderByIdDesc();
    Optional<Comment> deleteCommentById(int id);
}
