package org.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.demo.model.enums.PublicationStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("publication")
public class Publication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String title;
    protected String author;
    @ManyToOne
    protected Client owner;
    @ManyToOne
    protected Client client;
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    protected List<PeriodicRecord> records;
    @Enumerated(EnumType.STRING)
    protected PublicationStatus publicationStatus;
    protected LocalDate requestDate;

    public Publication(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Publication(int id, String title, String author) {
    }

    @Override
    public String toString() {
        return title;
    }
}
