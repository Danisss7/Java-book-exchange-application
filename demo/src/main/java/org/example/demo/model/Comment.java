package org.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String body;
    private LocalDateTime timestamp;
    private boolean isRead;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Chat chat;
    @ManyToOne
    private Client commentOwner;
    @ManyToOne
    private Book bookReview;

    public Comment(String title, String body, LocalDateTime timestamp, List<Comment> replies, Comment parentComment, Client client) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.replies = replies;
        this.parentComment = parentComment;
        this.client = client;
    }

    public Comment(String title, String body, LocalDateTime timestamp, Comment parentComment, Client client, Chat chat) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.parentComment = parentComment;
        this.client = client;
        this.chat = chat;
    }

    public Comment(String title, String body, LocalDateTime timestamp) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Comment(String title, String body, Client client, Client commentOwner) {
        this.title = title;
        this.body = body;
        this.client = client;
        this.commentOwner = commentOwner;
        this.timestamp = LocalDateTime.now();
    }

    public Comment(String title, String body, Client client, Client commentOwner, Chat chat) {
        this.title = title;
        this.body = body;
        this.client = client;
        this.commentOwner = commentOwner;
        this.chat = chat;
        this.timestamp = LocalDateTime.now();
    }

    public Comment(String title, String body, Comment parentComment, Client commentOwner) {
        this.title = title;
        this.body = body;
        this.parentComment = parentComment;
        this.commentOwner = commentOwner;
        this.timestamp = LocalDateTime.now();
    }

    public Comment(String title, String body, LocalDateTime timestamp, Client client, Comment parentComment) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.client = client;
        this.parentComment = parentComment;
    }

    @Override
    public String toString() {
        return body;
    }
}
