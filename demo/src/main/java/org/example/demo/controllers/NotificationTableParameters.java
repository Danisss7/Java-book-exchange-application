package org.example.demo.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.model.Comment;
import org.example.demo.model.enums.PublicationStatus;

@Getter
@Setter
public class NotificationTableParameters {
    public SimpleIntegerProperty notificationID = new SimpleIntegerProperty();
    public SimpleStringProperty notificationInformation = new SimpleStringProperty();
    public SimpleStringProperty notificationUser = new SimpleStringProperty();
    private SimpleBooleanProperty isRead = new SimpleBooleanProperty(false); // Default to unread
    private Comment comment = new Comment();

    public NotificationTableParameters(SimpleIntegerProperty notificationID, SimpleStringProperty notificationInformation, SimpleStringProperty notificationUser) {
        this.notificationID = notificationID;
        this.notificationInformation = notificationInformation;
        this.notificationUser = notificationUser;
    }

    public NotificationTableParameters() {}

    public int getNotificationID() {
        return notificationID.get();
    }

    public SimpleIntegerProperty notificationIDProperty() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID.set(notificationID);
    }

    public String getNotificationInformation() {
        return notificationInformation.get();
    }

    public SimpleStringProperty notificationInformationProperty() {
        return notificationInformation;
    }

    public void setNotificationInformation(String notificationInformation) {
        this.notificationInformation.set(notificationInformation);
    }

    public String getNotificationUser() {
        return notificationUser.get();
    }

    public SimpleStringProperty notificationUserProperty() {
        return notificationUser;
    }

    public void setNotificationUser(String notificationUser) {
        this.notificationUser.set(notificationUser);
    }

    public boolean isIsRead() {
        return isRead.get();
    }

    public SimpleBooleanProperty isReadProperty() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead.set(isRead);
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
