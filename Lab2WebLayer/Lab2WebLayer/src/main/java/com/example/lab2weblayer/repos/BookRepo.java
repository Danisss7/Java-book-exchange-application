package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Book;
import com.example.lab2weblayer.model.enums.Genre;
import com.example.lab2weblayer.model.enums.PublicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Integer> {
    Iterable<Book> findByGenre(Genre genre);
    List<Book> findAllByPublicationStatus(PublicationStatus publicationStatus);
    Optional<Book> findTopByOrderByIdDesc();
    List<Book> findByOwnerId(int ownerId);
    Iterable<Book> findByPublicationStatusAndOwnerIdNot(PublicationStatus publicationStatus, int owner_id);
    Iterable<Book> findByPublicationStatusAndClientId(PublicationStatus publicationStatus, int client_id);
    Optional<Book> deleteBookById(int id);
    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = :id")
    void deleteBookById(@Param("id") Integer id);
    Book findBookById(int id);
}
