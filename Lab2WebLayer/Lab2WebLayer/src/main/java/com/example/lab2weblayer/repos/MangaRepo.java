package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Book;
import com.example.lab2weblayer.model.Manga;
import com.example.lab2weblayer.model.enums.Demographic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MangaRepo extends JpaRepository<Manga, Integer> {
    List<Manga> findByDemographic(Demographic demographic);
    List<Manga> findByIllustrator(String illustrator);
    Optional<Manga> findTopByOrderByIdDesc();
}