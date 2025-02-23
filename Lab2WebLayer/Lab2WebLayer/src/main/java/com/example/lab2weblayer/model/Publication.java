package com.example.lab2weblayer.model;

import com.example.lab2weblayer.model.enums.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Publication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String title;
    protected String author;
    @JsonIgnore
    @ManyToOne
    protected Client owner;
    @JsonIgnore
    @ManyToOne
    protected Client client;
    @JsonIgnore
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    protected List<PeriodicRecord> records;
    @Enumerated(EnumType.STRING)
    protected PublicationStatus publicationStatus;
    protected LocalDate requestDate;

    public Publication(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
