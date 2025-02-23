package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.model.Book;

import com.example.lab2weblayer.model.Client;
import com.example.lab2weblayer.model.enums.Format;
import com.example.lab2weblayer.model.enums.Genre;
import com.example.lab2weblayer.model.enums.Language;
import com.example.lab2weblayer.model.enums.PublicationStatus;
import com.example.lab2weblayer.repos.BookRepo;
import com.example.lab2weblayer.repos.ChatRepo;
import com.example.lab2weblayer.repos.ClientRepo;
import com.example.lab2weblayer.repos.CommentRepo;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/books")
public class BookController {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ChatRepo chatRepo;

    @PostMapping("/createBook")
    public String createBook(@RequestBody Book book) {
        bookRepo.save(book);
        return "Book created successfully.";
    }

    @PostMapping("/createBookByOwnerID")
    public String createBook(@RequestBody BookRequest bookRequest) {
        Client owner = clientRepo.findById(bookRequest.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        String response = "Owner found: " + owner.toString();
        Book book = new Book(
                bookRequest.getTitle(),
                bookRequest.getAuthor(),
                bookRequest.getPublisher(),
                bookRequest.getIsbn(),
                bookRequest.getGenre(),
                bookRequest.getPageCount(),
                bookRequest.getLanguage(),
                bookRequest.getPublicationYear(),
                bookRequest.getFormat(),
                bookRequest.getSummary()
        );
        book.setOwner(owner);
        bookRepo.save(book);

        return "Book created successfully.";
    }



    @GetMapping("/allAvailableBooks/{userId}")
    public Iterable<Book> getAllAvailableBooks(@PathVariable int userId) {
        return bookRepo.findByPublicationStatusAndOwnerIdNot(PublicationStatus.AVAILABLE, userId);
    }

    @GetMapping("/allBorrowedBooks/{userId}")
    public Iterable<Book> getAllBorrowedBooks(@PathVariable int userId) {
        return bookRepo.findByPublicationStatusAndClientId(PublicationStatus.RESERVED, userId);
    }

    @GetMapping("/allBooks")
    public Iterable<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @GetMapping("/book/{id}")
    public EntityModel<Book> getBookById(@PathVariable Integer id) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID " + id));

        return EntityModel.of(book,
                linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).getAllBooks()).withRel("allBooks"));
    }

    @PutMapping("/reserveBook/{bookId}/{clientId}")
    public ResponseEntity<String> reserveBook(@PathVariable int bookId, @PathVariable int clientId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID " + bookId));

        if (book.getPublicationStatus() == PublicationStatus.RESERVED) {
            return ResponseEntity.status(400).body("Book is already reserved.");
        }

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID " + clientId));

        book.setPublicationStatus(PublicationStatus.RESERVED);
        book.setClient(client);

        bookRepo.save(book);

        return ResponseEntity.ok("Book reserved successfully.");
    }

    @PutMapping("/unreserveBook/{bookId}")
    public ResponseEntity<String> unreserveBook(@PathVariable int bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID " + bookId));

        if (book.getPublicationStatus() == PublicationStatus.AVAILABLE) {
            return ResponseEntity.status(400).body("Book is already available.");
        }
        book.setPublicationStatus(PublicationStatus.AVAILABLE);
        book.setClient(null);

        bookRepo.save(book);

        return ResponseEntity.ok("Book unreserved successfully.");
    }


    @PutMapping("/updateBook/{id}")
    public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        Book existingBook = bookRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublisher(updatedBook.getPublisher());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setPageCount(updatedBook.getPageCount());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setFormat(updatedBook.getFormat());
        existingBook.setSummary(updatedBook.getSummary());

        bookRepo.save(existingBook);

        System.out.println("Received update for book: " + updatedBook);

        return ResponseEntity.ok("Book updated successfully");
    }

    @GetMapping("/latestBook")
    public EntityModel<Book> getLatestBook() {
        Book latestBook = bookRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No books found"));

        return EntityModel.of(latestBook,
                linkTo(methodOn(BookController.class).getLatestBook()).withSelfRel(),
                linkTo(methodOn(BookController.class).getAllBooks()).withRel("allBooks"));
    }

    @DeleteMapping("/deleteLatestBook")
    public String deleteLatestBook() {
        Optional<Book> latestBook = bookRepo.findTopByOrderByIdDesc();

        if (latestBook.isPresent()) {
            bookRepo.delete(latestBook.get());
            return "Latest book deleted successfully.";
        } else {
            return "No books found.";
        }
    }

    @Transactional
    @DeleteMapping("/deleteBook/{id}")
    public String deleteBookById(@PathVariable Integer id) {
        Book book = bookRepo.findBookById(id);
        if (book != null) {
            bookRepo.deleteBookById(id);
            return "Book with ID " + id + " deleted successfully.";
        } else {
            return "Book with ID " + id + " not found.";
        }
    }


    @GetMapping("/findByGenre/{genre}")
    public Iterable<Book> findBooksByGenre(@PathVariable Genre genre) {
        return bookRepo.findByGenre(genre);
    }

    @GetMapping("/byOwner/{ownerId}")
    public Iterable<Book> getBooksByOwnerId(@PathVariable Integer ownerId) {
        return bookRepo.findByOwnerId(ownerId);
    }

    @Setter
    @Getter
    public static class BookRequest {
        private String title;
        private String author;
        private String publisher;
        private String isbn;
        private Genre genre;
        private int pageCount;
        private Language language;
        private int publicationYear;
        private Format format;
        private String summary;
        private int ownerId;

    }

}
