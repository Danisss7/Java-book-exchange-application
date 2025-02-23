package org.example.demo.controllers;

import jakarta.persistence.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.demo.hibernateControllers.GenericHibernate;
import org.example.demo.model.*;
import org.example.demo.utils.FxUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentWindow {
    public ListView<Comment> commentListField;
    public TextField titleField;
    public TextField bodyField;
    public DatePicker timestampField;
    public ComboBox<Chat> chatField;
    public ComboBox<Comment> parentField;
    public ComboBox<Client> clientField;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("demo");
    private GenericHibernate hibernate = new GenericHibernate(entityManagerFactory);
    private EntityManager entityManager;

    public void initialize() {
        fillCommentList();
        fillChatDropdown();
        fillParentDropdown();
        fillClientDropdown();
    }

    public void fillCommentList() {
        commentListField.getItems().clear();
        List<Comment> commentList = hibernate.getAllRecords(Comment.class);
        commentListField.getItems().addAll(commentList);
    }

    public void fillChatDropdown() {
        List<Chat> chats = hibernate.getAllRecords(Chat.class);
        chatField.setItems(FXCollections.observableArrayList(chats));
    }

    public void fillParentDropdown() {
        List<Comment> comments = hibernate.getAllRecords(Comment.class);
        parentField.setItems(FXCollections.observableArrayList(comments));
    }

    public void fillClientDropdown() {
        List<Client> clients = hibernate.getAllRecords(Client.class);
        clientField.setItems(FXCollections.observableArrayList(clients));
    }

    public void fillCommentFields(MouseEvent mouseEvent) {
        Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
        Comment commentFromDB = hibernate.getEntityById(Comment.class, selectedComment.getId());
        titleField.setText(commentFromDB.getTitle());
        bodyField.setText(commentFromDB.getBody());
        timestampField.setValue(LocalDate.from(commentFromDB.getTimestamp()));
        chatField.setValue(commentFromDB.getChat());
        parentField.setValue(commentFromDB.getParentComment());
        clientField.setValue(commentFromDB.getClient());
    }

    public void createComment(ActionEvent actionEvent) {
        Comment comment = new Comment(titleField.getText(), bodyField.getText(), LocalDateTime.from(timestampField.getValue()), parentField.getValue(), clientField.getValue(), chatField.getValue());
        hibernate.create(comment);
        fillCommentList();
    }

    public void updateComment(ActionEvent actionEvent) {
        Comment commentSelected = commentListField.getSelectionModel().getSelectedItem();
        Comment commentFromDB = hibernate.getEntityById(Comment.class, commentSelected.getId());
        commentFromDB.setTitle(titleField.getText());
        commentFromDB.setBody(bodyField.getText());
        commentFromDB.setTimestamp(timestampField.getValue().atStartOfDay());
        commentFromDB.setChat(chatField.getValue());
        commentFromDB.setParentComment(parentField.getValue());
        commentFromDB.setClient(clientField.getValue());
        hibernate.update(commentFromDB);
        fillCommentList();
    }

    public void deleteComment(ActionEvent actionEvent) {
        Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            EntityManager entityManager = null;
            EntityTransaction transaction = null;
            try {
                entityManager = entityManagerFactory.createEntityManager();
                transaction = entityManager.getTransaction();
                transaction.begin();
                String jpql = "DELETE FROM Comment c WHERE c.id = :id";
                Query query = entityManager.createQuery(jpql);
                query.setParameter("id", selectedComment.getId());
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
            }
            fillCommentList();
            clearFields();
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "GUI Error", "No comment selected for deletion.");
        }
    }


    public void clearCommentFields(ActionEvent actionEvent) {
        titleField.clear();
        bodyField.clear();
        timestampField.setValue(null);
        chatField.setValue(null);
        parentField.setValue(null);
        clientField.setValue(null);
    }

    public void clearFields() {
        titleField.clear();
        bodyField.clear();
        timestampField.setValue(null);
        chatField.setValue(null);
        parentField.setValue(null);
        clientField.setValue(null);
    }
}
