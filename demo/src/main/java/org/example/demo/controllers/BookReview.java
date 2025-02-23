package org.example.demo.controllers;

import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.hibernateControllers.CustomHibernate;
import org.example.demo.model.*;

public class BookReview {
    @FXML
    public TreeView<Comment> bookReview;
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
    private Book book;

    public void setData(EntityManagerFactory entityManagerFactory, User user, Client client, Book book) {
        this.currentUser = user;
        this.hibernate = new CustomHibernate(entityManagerFactory);
        this.targetClient = client;
        this.book = book;
        fillTree();

        if(currentUser instanceof Client) {
            deleteItem.setDisable(true);
            updateButton.setVisible(false);
        }
    }

    private void fillTree() {
        bookReview.setRoot(new TreeItem<>());
        bookReview.setShowRoot(false);
        bookReview.getRoot().setExpanded(true);
        if (currentUser instanceof Admin){
            addButton.setVisible(false);
        }
        book.getReviewList().stream()
                .filter(c -> c.getChat() == null && c.getParentComment() == null && c.getBookReview().getId() == book.getId())
                .forEach(c -> addTreeItem(c, bookReview.getRoot()));
    }

    public void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        treeItem.setExpanded(true);
        comment.getReplies().forEach(reply -> addTreeItem(reply, treeItem));
    }

    public void loadComment() {
        Comment selectedComment = bookReview.getSelectionModel().getSelectedItem().getValue();
        commentTitle.setText(selectedComment.getTitle());
        commentBody.setText(selectedComment.getBody());
    }

    public void insertComment() {
        TreeItem<Comment> selectedTreeItem = bookReview.getSelectionModel().getSelectedItem();
        Comment newComment;

        if (currentUser instanceof Client client) {
            if (selectedTreeItem != null && selectedTreeItem.getValue() != null) {
                Comment parentComment = selectedTreeItem.getValue();
                newComment = new Comment(commentTitle.getText(), commentBody.getText(), parentComment, client);
            } else {
                newComment = new Comment(commentTitle.getText(), commentBody.getText(), targetClient, client);
            }
        } else {
            if (selectedTreeItem != null && selectedTreeItem.getValue() != null) {
                Comment parentComment = selectedTreeItem.getValue();
                newComment = new Comment(commentTitle.getText(), commentBody.getText(), parentComment, (Client) currentUser);
            } else {
                newComment = new Comment(commentTitle.getText(), commentBody.getText(), targetClient, (Client) currentUser);
            }
        }

        newComment.setBookReview(book);
        hibernate.create(newComment);
        book = hibernate.getEntityById(Book.class, book.getId());
        fillTree();
    }

    public void updateComment() {
        Comment selectedComment = bookReview.getSelectionModel().getSelectedItem().getValue();
        if (currentUser instanceof Admin) {
            selectedComment.setTitle(commentTitle.getText());
            selectedComment.setBody(commentBody.getText());
            hibernate.update(selectedComment);
            book = hibernate.getEntityById(Book.class, book.getId());
            fillTree();
        }
    }

    public void deleteComment() {
        Comment selectedComment = bookReview.getSelectionModel().getSelectedItem().getValue();
        hibernate.deleteComment(selectedComment.getId());
        book = hibernate.getEntityById(Book.class, book.getId());
        bookReview.setRoot(null);
        fillTree();
    }
}