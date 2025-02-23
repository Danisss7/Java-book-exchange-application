package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.Client;
import com.example.lab2weblayer.model.Periodical;
import com.example.lab2weblayer.model.enums.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PeriodicalRepo extends JpaRepository<Periodical, Integer> {
    List<Periodical> findByFrequency(Frequency frequency);
    List<Periodical> findByPublicationDate(LocalDate publicationDate);
    Optional<Periodical> findTopByOrderByIdDesc();
}
