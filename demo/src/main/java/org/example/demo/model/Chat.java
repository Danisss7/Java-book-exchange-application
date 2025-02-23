package org.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Book book;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> messages;
    private LocalDate creationDate;

    public Chat(Book book, LocalDate creationDate, List<Comment> messages) {
        this.book = book;
        this.creationDate = creationDate;
        this.messages = messages;
    }

    public Chat(Book book, LocalDate creationDate) {
        this.book = book;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return creationDate + " " + book.getTitle();
    }
}
