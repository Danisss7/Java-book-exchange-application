package com.example.lab2weblayer.repos;

import com.example.lab2weblayer.model.*;
import com.example.lab2weblayer.model.enums.Demographic;
import com.example.lab2weblayer.model.enums.Frequency;
import com.example.lab2weblayer.model.enums.Genre;
import com.example.lab2weblayer.model.enums.PublicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepo extends JpaRepository<Publication, Integer> {
    List<Publication> findAllByPublicationStatusEquals(PublicationStatus status);
}
