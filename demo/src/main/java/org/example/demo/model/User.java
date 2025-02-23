package org.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
//Jei noriu client ir admin tureti atskiras lenteles
//@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(unique = true, nullable = false)
    protected String login;
    @Column(nullable = false)
    protected String password;
    protected String salt;
    protected String name;
    protected String surname;

    public User(String login, String password, String salt, String name, String surname) {
        this.login = login;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.surname = surname;
    }
}
