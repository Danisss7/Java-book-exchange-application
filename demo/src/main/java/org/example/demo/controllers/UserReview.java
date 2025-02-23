package org.example.demo.controllers;

import com.sun.source.tree.Tree;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.demo.hibernateControllers.CustomHibernate;
import org.example.demo.model.Admin;
import org.example.demo.model.Client;
import org.example.demo.model.Comment;
import org.example.demo.model.User;

public class UserReview {
    @FXML
    public TreeView<Comment> userReview;
    @FXML
    public TextArea commentBody;
    @FXML
    public TextField commentTitle;
    @FXML
    public ContextMenu commentContextMenu;
    @FXML
    public MenuItem deleteItem;
    @FXML
    public Button addButton;
    @FXML
    public Button updateButton;

    private CustomHibernate hibernate;
    private User currentUser;
    private Client targetClient;

    public void setData(EntityManagerFactory entityManagerFactory, User user, Client client) {
        this.currentUser = user;
        this.hibernate = new CustomHibernate(entityManagerFactory);
        this.targetClient = client;
        fillTree();

        if (currentUser instanceof Client) {
            deleteItem.setDisable(true);
            updateButton.setVisible(false);
        } else {
            addButton.setVisible(false);
        }
    }

    private void fillTree() {
        userReview.setRoot(new TreeItem<>());
        userReview.setShowRoot(false);
        userReview.getRoot().setExpanded(true);

        Client clientFromDb = hibernate.getEntityById(Client.class, targetClient.getId());

        clientFromDb.getCommentList().stream()
                .filter(c -> c.getChat() == null && c.getClient().getId() == targetClient.getId() && c.getBookReview() == null)
                .forEach(c -> addTreeItem(c, userReview.getRoot()));
    }

    public void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        treeItem.setExpanded(true);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }

    public void loadComment() {
        Comment selectedComment = userReview.getSelectionModel().getSelectedItem().getValue();
        commentTitle.setText(selectedComment.getTitle());
        commentBody.setText(selectedComment.getBody());
    }

    public void insertComment() {
        if (currentUser instanceof Client client) {
            TreeItem<Comment> selectedTreeItem = userReview.getSelectionModel().getSelectedItem();
            Comment comment;
            if (selectedTreeItem != null && selectedTreeItem.getValue() != null) {
                Comment selectedComment = selectedTreeItem.getValue();
                comment = new Comment(commentTitle.getText(), commentBody.getText(), selectedComment, client);
            } else {
                comment = new Comment(commentTitle.getText(), commentBody.getText(), targetClient, client);
            }
            hibernate.create(comment);
            fillTree();
        } else {
            showAlert("Error", "Admins cannot add reviews.");
        }
    }

    public void updateComment() {
        Comment selectedComment = userReview.getSelectionModel().getSelectedItem().getValue();
        if (currentUser instanceof Admin) {
            selectedComment.setTitle(commentTitle.getText());
            selectedComment.setBody(commentBody.getText());
            hibernate.update(selectedComment);
            fillTree();
        }
    }

    public void deleteComment() {
        if (currentUser instanceof Client) {
            showAlert("Error", "Clients cannot delete comments.");
        } else {
            Comment selectedComment = userReview.getSelectionModel().getSelectedItem().getValue();
            hibernate.deleteComment(selectedComment.getId());
            fillTree();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
