package org.example.demo.controllers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo.StartGUI;
import org.example.demo.hibernateControllers.CustomHibernate;
import org.example.demo.model.User;
import org.example.demo.utils.FxUtils;

import java.io.IOException;
import java.sql.*;

public class Login {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("demo");
    CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);

    public void validateUser() throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Login error", "Error: Username or Password field is empty");
            return;
        }

        User user = customHibernate.getUserByCredentials(username, password);

        if (user != null){
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("main.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            //po sios eilutes pasieksiu kontroleri
            Main main = fxmlLoader.getController();
            main.setData(entityManagerFactory, user);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Book Exchange Test");
            stage.setScene(scene);
            stage.show();
        }else{
            FxUtils.generateAlert(Alert.AlertType.ERROR, "User info", "Wrong credentials");
        }

    }


    public void loadRegistration(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("register.fxml"));
        Parent parent = fxmlLoader.load();
        Register register = fxmlLoader.getController();
        register.setData(customHibernate, entityManagerFactory);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Book Exchange Test");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
