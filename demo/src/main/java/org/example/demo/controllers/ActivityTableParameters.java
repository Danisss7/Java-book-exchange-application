package org.example.demo.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.model.enums.PublicationStatus;

@Getter
@Setter
public class ActivityTableParameters {
    public SimpleIntegerProperty activityID = new SimpleIntegerProperty();
    public SimpleStringProperty activityBookTitle = new SimpleStringProperty();
    public SimpleStringProperty activityOwner = new SimpleStringProperty();
    public SimpleStringProperty activityStatus = new SimpleStringProperty(); // Use SimpleObjectProperty
    public SimpleStringProperty activityDate = new SimpleStringProperty();

    public ActivityTableParameters(SimpleIntegerProperty activityID, SimpleStringProperty activityBookTitle, SimpleStringProperty activityOwner, SimpleStringProperty activityStatus, SimpleStringProperty activityDate) {
        this.activityID = activityID;
        this.activityBookTitle = activityBookTitle;
        this.activityOwner = activityOwner;
        this.activityStatus = activityStatus;
        this.activityDate = activityDate;
    }

    public ActivityTableParameters(SimpleStringProperty activityBookTitle, SimpleStringProperty activityOwner, SimpleStringProperty activityStatus, SimpleStringProperty activityDate) {
        this.activityBookTitle = activityBookTitle;
        this.activityOwner = activityOwner;
        this.activityStatus = activityStatus;
        this.activityDate = activityDate;
    }

    public ActivityTableParameters() {
    }

    public int getActivityID() {
        return activityID.get();
    }

    public SimpleIntegerProperty activityIDProperty() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID.set(activityID);
    }

    public String getActivityBookTitle() {
        return activityBookTitle.get();
    }

    public SimpleStringProperty activityBookTitleProperty() {
        return activityBookTitle;
    }

    public void setActivityBookTitle(String activityBookTitle) {
        this.activityBookTitle.set(activityBookTitle);
    }

    public String getActivityOwner() {
        return activityOwner.get();
    }

    public SimpleStringProperty activityOwnerProperty() {
        return activityOwner;
    }

    public void setActivityOwner(String activityOwner) {
        this.activityOwner.set(activityOwner);
    }

    public String getActivityDate() {
        return activityDate.get();
    }

    public SimpleStringProperty activityDateProperty() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate.set(activityDate);
    }

    public SimpleStringProperty activityStatusProperty() {
        return activityStatus;
    }

    public String getActivityStatus() {
        return activityStatus.get();
    }

    public void setActivityStatus(PublicationStatus activityStatus) {
        this.activityStatus.set(String.valueOf(activityStatus));
    }
}
