package org.example.demo.controllers;

import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.hibernateControllers.CustomHibernate;
import org.example.demo.model.*;

import java.time.LocalDate;
import java.util.List;

public class ChatWindow {
    public ContextMenu commentContextMenu;
    @FXML
    private TreeView<Comment> commentWindow;
    @FXML
    private TextArea commentBody;
    @FXML
    public Button addCommentButton;

    private CustomHibernate hibernate;
    private User currentUser;
    private User targetUser;
    private Book targetBook;
    private int chatID;

    public void setData(EntityManagerFactory entityManagerFactory, User user, Book targetBook) {
        this.currentUser = user;
        this.hibernate = new CustomHibernate(entityManagerFactory);
        this.targetBook = targetBook;

        if (targetBook.getOwner().getId() == currentUser.getId()) {
            this.targetUser = hibernate.getChatByBook(targetBook).getMessages().stream()
                    .map(Comment::getCommentOwner)
                    .filter(commentOwner -> commentOwner.getId() != currentUser.getId())
                    .findFirst()
                    .orElse(null);
        } else {
            this.targetUser = targetBook.getOwner();
        }

        Chat existingChat = hibernate.getChatByBook(targetBook);
        if (existingChat == null) {
            existingChat = new Chat(targetBook, LocalDate.now());
            hibernate.create(existingChat);
        }

        chatID = existingChat.getId();

        fillTree(existingChat);
    }

    private boolean isRelevantComment(Comment comment) {
        return (comment.getClient().getId() == currentUser.getId() &&
                comment.getCommentOwner().getId() == targetUser.getId()) ||
                (comment.getClient().getId() == targetUser.getId() &&
                        comment.getCommentOwner().getId() == currentUser.getId());
    }

    private void fillTree(Chat chat) {
        commentWindow.setRoot(new TreeItem<>(null));
        commentWindow.setShowRoot(false);
        commentWindow.getRoot().setExpanded(true);
        commentWindow.getRoot().getChildren().clear();

        if (chat.getMessages() != null) {
            List<Comment> relevantComments = chat.getMessages().stream()
                    .filter(this::isRelevantComment)
                    .toList();
            relevantComments.forEach(comment -> addTreeItem(comment, commentWindow.getRoot()));
        }
        commentWindow.refresh();
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentItem) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);

        if (comment.getCommentOwner().getId() == currentUser.getId()) {
            treeItem.setGraphic(createStyledLabel(comment));
        } else {
            treeItem.setGraphic(createStyledLabel(comment));
        }

        parentItem.getChildren().add(treeItem);

        if (comment.getReplies() != null) {
            comment.getReplies().forEach(reply -> addTreeItem(reply, treeItem));
        }
    }


    private Label createStyledLabel(Comment comment) {
        Label label = new Label(comment.getBody());

        if (!comment.isRead() && comment.getClient().getId() == currentUser.getId()) {
            label.setStyle("-fx-text-fill: darkgray; -fx-font-weight: bold; -fx-alignment: baseline-left;");
        } else {
            if (comment.getCommentOwner().getId() == currentUser.getId()) {
                label.setStyle("-fx-text-fill: blue; -fx-alignment: baseline-left;");
            } else {
                label.setStyle("-fx-text-fill: green; -fx-alignment: baseline-right;");
            }
        }

        return label;
    }


    @FXML
    private void insertComment() {
        if (currentUser instanceof Client) {
            Comment newComment = new Comment(
                    "Message",
                    commentBody.getText(),
                    (Client) targetUser,
                    (Client) currentUser,
                    hibernate.getEntityById(Chat.class, chatID)
            );

            Chat chat = hibernate.getChatByBook(targetBook);
            chat.getMessages().add(newComment);
            hibernate.update(chat);

            fillTree(chat);

            commentBody.clear();
        }
    }

    public void loadComment() {
        Comment selectedComment = commentWindow.getSelectionModel().getSelectedItem().getValue();
        if (!selectedComment.isRead() && selectedComment.getCommentOwner().getId() != currentUser.getId()) {
            Comment selectedCommentFromDB = hibernate.getEntityById(Comment.class, selectedComment.getId());
            selectedCommentFromDB.setRead(true);
            hibernate.update(selectedCommentFromDB);
            fillTree(hibernate.getChatByBook(targetBook));
        }
    }
}
