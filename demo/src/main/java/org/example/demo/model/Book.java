package org.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.demo.model.enums.Genre;
import org.example.demo.model.enums.Language;
import org.example.demo.model.enums.Format;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//You can generate getters and setters, here Lombok lib is used. They are generated for you
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("book")
public class Book extends Publication{
    //Variables should be private and their values retrieved and changed using getters and setters respectively
    private String publisher;
    private String isbn;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private int pageCount;
    @Enumerated(EnumType.STRING)
    private Language language;
    private int publicationYear;
    @Enumerated(EnumType.STRING)
    private Format format;
    private String summary;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Chat> chatList;
    @OneToMany(mappedBy = "bookReview", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> reviewList;

    public Book(String title, String author, String publisher, String isbn, Genre genre, int pageCount, Language language, int publicationYear, Format format, String summary) {
        super(title, author);
        this.publisher = publisher;
        this.isbn = isbn;
        this.genre = genre;
        this.pageCount = pageCount;
        this.language = language;
        this.publicationYear = publicationYear;
        this.format = format;
        this.summary = summary;
    }

}
