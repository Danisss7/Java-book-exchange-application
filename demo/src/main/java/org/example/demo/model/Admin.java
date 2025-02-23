package org.example.demo.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User{
    private String phoneNum;

    public Admin(int id, String login, String password, String salt, String name, String surname, String phoneNum) {
        super(id, login, password, salt, name, surname);
        this.phoneNum = phoneNum;
    }

    public Admin(String login, String password, String salt, String name, String surname, String phoneNum) {
        super(login, password, salt, name, surname);
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
