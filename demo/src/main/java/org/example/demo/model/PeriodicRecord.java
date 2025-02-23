package org.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.demo.model.enums.PublicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PeriodicRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Client user;
    @ManyToOne
    private Publication publication;
    private LocalDate transactionDate;
    private LocalDate returnDate;
    private boolean isRead;
    @Enumerated(EnumType.STRING)
    private PublicationStatus status;

    public PeriodicRecord(Client user, Publication publication, LocalDate transactionDate, LocalDate returnDate, PublicationStatus status) {
        this.user = user;
        this.publication = publication;
        this.transactionDate = transactionDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public PeriodicRecord(Client user, Publication publication, LocalDate transactionDate, PublicationStatus status) {
        this.user = user;
        this.publication = publication;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return publication.getTitle();
    }
}
