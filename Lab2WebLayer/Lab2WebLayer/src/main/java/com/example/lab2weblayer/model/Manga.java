package com.example.lab2weblayer.model;

import com.example.lab2weblayer.model.enums.Demographic;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("manga")
public class Manga extends Publication{

    private String illustrator;
    private String originalLanguage;
    private int volumeNumber;
    @Enumerated
    private Demographic demographic;
    private boolean isColor;


    public Manga(String title, String author, String illustrator, String originalLanguage, int volumeNumber, Demographic demographic, boolean isColor) {
        super(title, author);
        this.illustrator = illustrator;
        this.originalLanguage = originalLanguage;
        this.volumeNumber = volumeNumber;
        this.demographic = demographic;
        this.isColor = isColor;
    }
}
