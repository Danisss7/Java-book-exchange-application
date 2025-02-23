package org.example.demo.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;  // Use SimpleObjectProperty for the enum
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.model.enums.PublicationStatus;

@Getter
@Setter
public class BookTableParameters {
    public SimpleIntegerProperty publicationID = new SimpleIntegerProperty();
    public SimpleStringProperty publicationTitle = new SimpleStringProperty();
    public SimpleStringProperty publicationUser = new SimpleStringProperty();
    public PublicationStatus publicationStatus;
    public SimpleStringProperty publicationRequestDate = new SimpleStringProperty();

    public BookTableParameters(SimpleIntegerProperty publicationID, SimpleStringProperty publicationTitle, SimpleStringProperty publicationUser, PublicationStatus publicationStatus, SimpleStringProperty publicationRequestDate) {
        this.publicationID = publicationID;
        this.publicationTitle = publicationTitle;
        this.publicationUser = publicationUser;
        this.publicationStatus = publicationStatus;
        this.publicationRequestDate = publicationRequestDate;
    }

    public BookTableParameters() {}

    public int getPublicationID() {
        return publicationID.get();
    }

    public SimpleIntegerProperty publicationIDProperty() {
        return publicationID;
    }

    public void setPublicationID(int publicationID) {
        this.publicationID.set(publicationID);
    }

    public String getPublicationTitle() {
        return publicationTitle.get();
    }

    public SimpleStringProperty publicationTitleProperty() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle.set(publicationTitle);
    }

    public String getPublicationUser() {
        return publicationUser.get();
    }

    public SimpleStringProperty publicationUserProperty() {
        return publicationUser;
    }

    public void setPublicationUser(String publicationUser) {
        this.publicationUser.set(publicationUser);
    }

    public String getPublicationRequestDate() {
        return publicationRequestDate.get();
    }

    public SimpleStringProperty publicationRequestDateProperty() {
        return publicationRequestDate;
    }

    public void setPublicationRequestDate(String publicationRequestDate) {
        this.publicationRequestDate.set(publicationRequestDate);
    }
}
