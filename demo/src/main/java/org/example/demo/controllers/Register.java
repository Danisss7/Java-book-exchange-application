package org.example.demo.controllers;

import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.hibernateControllers.CustomHibernate;
import org.example.demo.model.Admin;
import org.example.demo.model.Client;
import org.example.demo.model.PasswordHasher;
import org.example.demo.utils.FxUtils;

public class Register {

    @FXML
    public TextField loginField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public DatePicker birthDate;
    @FXML
    public TextField addressField;
    @FXML
    public TextField phoneNumField;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton adminRadio;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextArea bioField;

    private CustomHibernate hibernate;
    EntityManagerFactory entityManagerFactory;

    public void setData(CustomHibernate customHibernate, EntityManagerFactory entityManagerFactory){
        hibernate = customHibernate;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void initialize(){
        clientRadioClick();
    }

    public void createNewUser() {
        String salt = PasswordHasher.generateSalt();
        if (clientRadio.isSelected()) {
            Client client = new Client(loginField.getText(), PasswordHasher.hashPassword(passwordField.getText(), salt), salt, nameField.getText(), surnameField.getText(), addressField.getText(), birthDate.getValue());
            client.setClientBio(bioField.getText());
            hibernate.create(client);
            FxUtils.generateAlert(Alert.AlertType.CONFIRMATION, "User Generated", "A new user has been generated!");
        } else {
            Admin admin = new Admin(loginField.getText(), PasswordHasher.hashPassword(passwordField.getText(), salt), salt, nameField.getText(), surnameField.getText(), phoneNumField.getText());
            hibernate.create(admin);
            FxUtils.generateAlert(Alert.AlertType.CONFIRMATION, "User Generated", "A new user has been generated!");
        }
    }

    public void clientRadioClick() {
        if (clientRadio.isSelected()) {
            phoneNumField.setDisable(true);
            birthDate.setDisable(false);
            addressField.setDisable(false);
            bioField.setDisable(false);
            adminRadio.setSelected(false);
        }
    }

    public void adminRadioClick() {
        if (adminRadio.isSelected()) {
            birthDate.setDisable(true);
            addressField.setDisable(true);
            phoneNumField.setDisable(false);
            bioField.setDisable(true);
            clientRadio.setSelected(false);
        }
    }
}
