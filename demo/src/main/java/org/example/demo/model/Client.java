package org.example.demo.model;

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
public class Client extends User{

    private String address;
    private LocalDate birthDate;
    private String clientBio;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> commentList;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Publication> ownedPublications;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Publication> borrowedPublications;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PeriodicRecord> periodicRecords;
    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> myComments;

    public Client(String login, String password, String salt, String name, String surname, String address) {
        super(login, password, salt, name, surname);
        this.address = address;
    }

    public Client(String login, String password, String salt, String name, String surname, String address, LocalDate birthDate) {
        super(login, password, salt, name, surname);
        this.address = address;
        this.birthDate = birthDate;
    }

    public Client(String login, String password, String salt, String name, String surname) {
        super(login, password, salt, name, surname);
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}