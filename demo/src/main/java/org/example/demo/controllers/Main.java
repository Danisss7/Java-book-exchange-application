package org.example.demo.controllers;

import jakarta.persistence.EntityManagerFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.demo.StartGUI;
import org.example.demo.hibernateControllers.CustomHibernate;
import org.example.demo.model.*;
import org.example.demo.model.enums.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main implements Initializable {
    private CustomHibernate hibernate;
    EntityManagerFactory entityManagerFactory;
    private User currentUser;

    //<editor-fold desc="notificationTab">
    @FXML
    public Tab clientNotificationTab;
    @FXML
    public TableView<NotificationTableParameters> clientNotificationTable;
    @FXML
    public TableColumn<NotificationTableParameters, String> colNotificationInformation;
    @FXML
    public TableColumn<NotificationTableParameters, String> colNotificationUser;
    @FXML
    public TableColumn colNotificationAction;
    @FXML
    public TableColumn colChatAction;
    public ObservableList<NotificationTableParameters> notificationData = FXCollections.observableArrayList();
    //</editor-fold>

    //<editor-fold desc="Main exchange tab fields">
    @FXML
    public ListView<Publication> availableBookList;
    @FXML
    public ComboBox<Types> typeFilterMain;
    @FXML
    public ComboBox<Format> formatFilterMain;
    @FXML
    public ComboBox<Genre> genreFilterMain;
    @FXML
    public ComboBox<Language> languageFilterMain;
    @FXML
    public TextArea aboutBook;
    @FXML
    public TextArea ownerBio;
    @FXML
    public Label ownerInfo;
    @FXML
    public Button buyBookButton;

//</editor-fold>

    //<editor-fold desc="UsersList">
    @FXML
    public ListView<User> allUsersListView;
    @FXML
    public TextField loginField;
    @FXML
    public TextField passwordField;
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
    public ToggleGroup userType;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton adminRadio;

    //</editor-fold>

    //<editor-fold desc="Tableview">
    @FXML
    public TableView<UserTableParameters> userTable;
    @FXML
    public TableColumn<UserTableParameters, Integer> cold;
    @FXML
    public TableColumn<UserTableParameters, String> colLogin;
    @FXML
    public TableColumn<UserTableParameters, String> colPassword;
    @FXML
    public TableColumn<UserTableParameters, String> colName;
    @FXML
    public TableColumn<UserTableParameters, String> colSurname;
    @FXML
    public TableColumn<UserTableParameters, String> colBirthDate;
    @FXML
    public TableColumn<UserTableParameters, String> colPhoneNumber;
    @FXML
    public TableColumn<UserTableParameters, String> colAddress;
    @FXML
    public TableColumn dummyCol;

    public ObservableList<UserTableParameters> data = FXCollections.observableArrayList();
    @FXML
    public TabPane allTabs;
    @FXML
    public Tab bookExchangeTab;
    @FXML
    public Tab userManagementTab;
    @FXML
    public Tab publicationManagementTab;
    @FXML
    public Tab alternativeUserManagementTab;
    @FXML
    public Tab clientBookManagementTab;
    @FXML
    public Button leaveReviewButton;
    @FXML
    public Button addMessageButton;
    @FXML
    public Button publicationActionButton;
    @FXML
    public ComboBox<PublicationStatus> borrowedBookStatusCombobox;

    //</editor-fold>

    //<editor-fold desc="BookTableParam">
    @FXML
    public TableView<BookTableParameters> bookTable;
    @FXML
    public TableColumn<BookTableParameters, Integer> colId;
    @FXML
    public TableColumn<BookTableParameters, String> colTitle;
    @FXML
    public TableColumn<BookTableParameters, String> colUser;
    @FXML
    public TableColumn colStatus;
    @FXML
    public TableColumn<BookTableParameters, String> colRequestDate;
    public ObservableList<BookTableParameters> bookTableData = FXCollections.observableArrayList();
    //</editor-fold>

    //<editor-fold desc="clientActivityHistory">
    @FXML
    public Tab clientActivityHistory;
    @FXML
    public Tab activityOverviewTab;
    public ObservableList<ActivityTableParameters> activityHistoryTableData = FXCollections.observableArrayList();
    @FXML
    public TableView<ActivityTableParameters> activityHistoryTable;
    @FXML
    public TableColumn<ActivityTableParameters, String> colActivityTitle;
    @FXML
    public TableColumn<ActivityTableParameters, String> colActivityOwner;
    @FXML
    public TableColumn<ActivityTableParameters, String> colActivityStatus;
    @FXML
    public TableColumn<ActivityTableParameters, String> colActivityDate;
    @FXML
    public TableColumn colActivityAction;
    //</editor-fold>

    //<editor-fold desc="currentClientManagement">
    @FXML
    public Tab currentClientManagementTab;
    @FXML
    public TextField currentClientLoginField;
    @FXML
    public TextField currentClientNameField;
    @FXML
    public TextField currentClientSurnameField;
    @FXML
    public DatePicker currentClientBirthDate;
    @FXML
    public TextField currentClientAddressField;
    @FXML
    public PasswordField currentClientPasswordField;
    @FXML
    public TextArea currentClientBioField;
    //</editor-fold>

    //<editor-fold desc="overviewTab">
    @FXML
    public ComboBox<Types> publicationFilterSelect;
    @FXML
    public DatePicker fromFilterSelect;
    @FXML
    public DatePicker toFilterSelect;
    @FXML
    public ComboBox<Format> formatTypeFilter;
    @FXML
    public ComboBox<Genre> genreFilterSelect;
    @FXML
    public ComboBox<Language> languageFilterSelect;
    @FXML
    public TextField soldCountField;
    @FXML
    public TextField borrowedCountField;
    @FXML
    public TextField borrowedToCountField;
    @FXML
    public TextField soldToCountField;
    @FXML
    public ListView<Publication> overviewPublicationList;
    @FXML
    public RadioButton formatRadialButton;
    @FXML
    public RadioButton publicationRadialButton;
    @FXML
    public RadioButton dateRadialButton;
    @FXML
    public RadioButton genreRadialButton;
    @FXML
    public RadioButton languageRadialButton;
    //</editor-fold>

    //<editor-fold desc="statisticsTab">
    @FXML
    public Tab statisticsTab;
    @FXML
    public TextField totalUsersField;
    @FXML
    public TextField totalBorrowedPublicationField;
    @FXML
    public TextField totalSoldPublicationField;
    @FXML
    public LineChart statisticChart;
    //</editor-fold>

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientRadioClick();
        formatTypeFilter.setItems(FXCollections.observableArrayList(Format.values()));
        publicationFilterSelect.setItems(FXCollections.observableArrayList(Types.values()));
        genreFilterSelect.setItems(FXCollections.observableArrayList(Genre.values()));
        languageFilterSelect.setItems(FXCollections.observableArrayList(Language.values()));
        typeFilterMain.setItems(FXCollections.observableArrayList(Types.values()));
        formatFilterMain.setItems(FXCollections.observableArrayList(Format.values()));
        genreFilterMain.setItems(FXCollections.observableArrayList(Genre.values()));
        languageFilterMain.setItems(FXCollections.observableArrayList(Language.values()));
        formatFilterMain.setDisable(true);
        genreFilterMain.setDisable(true);
        languageFilterMain.setDisable(true);
        dateRadialClick();
        formatRadialClick();
        genreRadialClick();
        languageRadialClick();
        publicationRadialClick();

        //<editor-fold desc="mainPublicationTab">
        ObservableList<PublicationStatus> publicationStatuses = FXCollections.observableArrayList();
        publicationStatuses.add(PublicationStatus.AVAILABLE);
        publicationStatuses.add(PublicationStatus.REQUESTED);
        publicationStatuses.add(PublicationStatus.TAKEN);
        borrowedBookStatusCombobox.setItems(publicationStatuses);
        publicationActionButton.setText("Select a filter");
        //</editor-fold>

        //<editor-fold desc="notificationTable">
        colNotificationInformation.setCellValueFactory(new PropertyValueFactory<>("notificationInformation"));
        colNotificationUser.setCellValueFactory(new PropertyValueFactory<>("notificationUser"));

        colNotificationInformation.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    NotificationTableParameters rowData = getTableView().getItems().get(getIndex());
                    if (rowData.getIsRead().getValue()) {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: gray;");
                    } else {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    }
                }
            }
        });

        Callback<TableColumn<NotificationTableParameters, Void>, TableCell<NotificationTableParameters, Void>> callbackNotificationRead = param -> {
            final TableCell<NotificationTableParameters, Void> cell = new TableCell<>() {
                private final Button readButton = new Button("Mark As Read");

                {
                    readButton.setOnAction(event -> {
                        NotificationTableParameters row = getTableView().getItems().get(getIndex());
                        if (row != null) {
                            Comment comment = row.getComment();
                            if (comment.getTitle() == null) {
                                PeriodicRecord periodicRecord = hibernate.getEntityById(PeriodicRecord.class, row.getNotificationID());
                                periodicRecord.setRead(true);
                                if (periodicRecord.getStatus() == PublicationStatus.REQUESTED) {
                                    periodicRecord.setStatus(PublicationStatus.TAKEN);
                                } else if (periodicRecord.getStatus() == PublicationStatus.RESERVED) {
                                    periodicRecord.setStatus(PublicationStatus.SOLD);
                                }
                                hibernate.update(periodicRecord);
                            } else if (!comment.isRead()) {
                                Comment dbComment = hibernate.getEntityById(Comment.class, comment.getId());
                                dbComment.setRead(true);
                                hibernate.update(dbComment);
                            }
                            notificationData.clear();
                            fillNotificationTable();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(readButton);
                    }
                }
            };
            return cell;
        };

        colNotificationAction.setCellFactory(callbackNotificationRead);

        Callback<TableColumn<NotificationTableParameters, Void>, TableCell<NotificationTableParameters, Void>> callbackNotificationChat = param -> {
            final TableCell<NotificationTableParameters, Void> cell = new TableCell<>() {
                private final Button chatButton = new Button("Chat");

                {
                    chatButton.setOnAction(event -> {
                        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("chatWindow.fxml"));
                        Parent parent = null;
                        try {
                            parent = fxmlLoader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        NotificationTableParameters row = getTableView().getItems().get(getIndex());
                        ChatWindow chatWindow = fxmlLoader.getController();
                        chatWindow.setData(entityManagerFactory, currentUser, row.getComment().getChat().getBook());
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setTitle(row.getComment().getCommentOwner().getName() + " " + row.getComment().getCommentOwner().getSurname());
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        NotificationTableParameters row = getTableView().getItems().get(getIndex());
                        if (row.getComment().getChat() == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(chatButton);
                        }
                    }
                }
            };
            return cell;
        };

        colChatAction.setCellFactory(callbackNotificationChat);
        //</editor-fold>

        //<editor-fold desc="activityTable">
        activityHistoryTable.setEditable(true);
        colActivityTitle.setCellValueFactory(new PropertyValueFactory<>("activityBookTitle"));
        colActivityOwner.setCellValueFactory(new PropertyValueFactory<>("activityOwner"));
        colActivityDate.setCellValueFactory(new PropertyValueFactory<>("activityDate"));
        colActivityStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        colActivityStatus.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getActivityStatus()));

        colActivityStatus.setOnEditCommit(event -> {
            String newStatusString = event.getNewValue();
            try {
                PublicationStatus newStatus = PublicationStatus.valueOf(newStatusString.toUpperCase());
                int activityID = event.getTableView().getItems().get(event.getTablePosition().getRow()).getActivityID();
                PeriodicRecord activity = hibernate.getEntityById(PeriodicRecord.class, activityID);
                activity.setStatus(newStatus);
                hibernate.update(activity);
                activityHistoryTableData.clear();
                fillActivityHistoryTable();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status: " + newStatusString);
            }
        });

        Callback<TableColumn<ActivityTableParameters, Void>, TableCell<ActivityTableParameters, Void>> activityCallback = param -> {
            final TableCell<ActivityTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ActivityTableParameters row = getTableView().getItems().get(getIndex());
                        hibernate.delete(PeriodicRecord.class, row.getActivityID());
                        activityHistoryTableData.clear();
                        fillActivityHistoryTable();
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        colActivityAction.setCellFactory(activityCallback);
        //</editor-fold>

        //<editor-fold desc="UserTable">
        userTable.setEditable(true);
        cold.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //Jei noriu padaryt redaguojamas celes
        colLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        colLogin.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setLogin(event.getNewValue());
            hibernate.update(user);
        });

        colPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        colPassword.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setPassword(PasswordHasher.hashPassword(event.getNewValue(), user.getSalt()));
            hibernate.update(user);
        });

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setName(event.getNewValue());
            hibernate.update(user);
        });

        colSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        colSurname.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSurname(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setSurname(event.getNewValue());
            hibernate.update(user);
        });

        //Jei turesite lentele, kurioje saugosime ir Admin ir Customer, tiems stulpeliams, kur yra specifiniai pagal klases
        //Reikia pasitikrinti koks ten tas User
        colAddress.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setAddress(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            if (user instanceof Client client) {
                client.setAddress(event.getNewValue());
                hibernate.update(user);
            }
        });

        colPhoneNumber.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPhoneNumber(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            if (user instanceof Admin admin) {
                admin.setPhoneNum(event.getNewValue());
                hibernate.update(admin);
            }
        });

        colBirthDate.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setBirthDate(event.getNewValue());
            User user = hibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            if (user instanceof Client client) {
                client.setBirthDate(LocalDate.parse(event.getNewValue()));
                hibernate.update(client);
            }
        });

        //Cia dabar bus knopke
        Callback<TableColumn<UserTableParameters, Void>, TableCell<UserTableParameters, Void>> callback = param -> {
            final TableCell<UserTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        UserTableParameters row = getTableView().getItems().get(getIndex());
                        hibernate.delete(User.class, row.getId());
                        data.clear();
                        fillUserTable();
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        dummyCol.setCellFactory(callback);
        //</editor-fold>

        //<editor-fold desc="BookTable">
        bookTable.setEditable(true);

        colId.setCellValueFactory(new PropertyValueFactory<>("publicationID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("publicationTitle"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("publicationUser"));

        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colTitle.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPublicationTitle(event.getNewValue());
            Publication publication = hibernate.getEntityById(Publication.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getPublicationID());
            publication.setTitle(event.getNewValue());
            hibernate.update(publication);
        });

        Callback<TableColumn<BookTableParameters, Void>, TableCell<BookTableParameters, Void>> callbackBookStatus = param -> {
            final TableCell<BookTableParameters, Void> cell = new TableCell<>() {
                private final ChoiceBox<PublicationStatus> publicationStatus = new ChoiceBox<>();

                {
                    publicationStatus.getItems().addAll(PublicationStatus.values());
                    publicationStatus.setOnAction(event -> {
                        BookTableParameters rowData = getTableRow().getItem();
                        if (rowData != null) {
                            rowData.setPublicationStatus(publicationStatus.getValue());
                            Publication publication = hibernate.getEntityById(Publication.class, rowData.getPublicationID());
                            publication.setPublicationStatus(publicationStatus.getValue());
                            hibernate.update(publication);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        BookTableParameters rowData = getTableRow().getItem();
                        publicationStatus.setValue(rowData.getPublicationStatus());
                        setGraphic(publicationStatus);
                    }
                }
            };
            return cell;
        };

        colStatus.setCellFactory(callbackBookStatus);
        //</editor-fold>
    }

    private void fillNotificationTable() {
        notificationData.clear();

        List<PeriodicRecord> allRecords = hibernate.getAllRecords(PeriodicRecord.class);

        for (PeriodicRecord p : allRecords) {
            if (p.getPublication().getOwner().getId() == currentUser.getId()) {
                NotificationTableParameters notificationTableParameters = new NotificationTableParameters();
                notificationTableParameters.setNotificationID(p.getId());
                notificationTableParameters.setIsRead(p.isRead());
                notificationTableParameters.setNotificationUser(p.getUser().getName() + " " + p.getUser().getSurname());
                if (p.getStatus() == PublicationStatus.REQUESTED) {
                    notificationTableParameters.setNotificationInformation(
                            "Your publication: " + p.getPublication().getTitle() + " has been requested to borrow");
                } else if (p.getStatus() == PublicationStatus.AVAILABLE) {
                    notificationTableParameters.setNotificationInformation(
                            "Your publication: " + p.getPublication().getTitle() + " has been returned");
                } else if (p.getStatus() == PublicationStatus.RESERVED) {
                    notificationTableParameters.setNotificationInformation(
                            "Your publication: " + p.getPublication().getTitle() + " has been bought");
                }
                if (notificationTableParameters.getNotificationInformation() != null) {
                    notificationData.add(notificationTableParameters);
                }
            }
        }

        List<Comment> unreadMessages = hibernate.getUnreadMessages((Client) currentUser);

        for (Comment comment : unreadMessages) {
            NotificationTableParameters notificationTableParameters = new NotificationTableParameters();
            notificationTableParameters.setNotificationID(comment.getId());
            notificationTableParameters.setIsRead(comment.isRead());
            if (comment.getParentComment() == null) {
                notificationTableParameters.setNotificationInformation(
                        "New review from: " + comment.getCommentOwner().getName() + "\n" + comment.getBody());
            } else {
                notificationTableParameters.setNotificationInformation(
                        "New message from: " + comment.getCommentOwner().getName());
            }
            notificationTableParameters.setNotificationUser(comment.getCommentOwner().getName());
            notificationTableParameters.setComment(comment);
            notificationData.add(notificationTableParameters);
        }

        clientNotificationTable.setItems(notificationData);
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.hibernate = new CustomHibernate(entityManagerFactory);
        loadData();

        //priklausomai nuo prisijungusio, apribojam matomuma
        enableVisibility();
    }

    public void fillActivityHistoryTable() {
        activityHistoryTableData.clear();
        activityHistoryTable.getItems().clear();
        List<PeriodicRecord> periodicRecords = hibernate.getAllRecordsSorted(PeriodicRecord.class, "transactionDate", true);
        for (PeriodicRecord p : periodicRecords) {
            ActivityTableParameters activityTableParameters = new ActivityTableParameters();
            activityTableParameters.setActivityID(p.getId());
            activityTableParameters.setActivityDate(String.valueOf(p.getTransactionDate()));
            activityTableParameters.setActivityOwner(p.getPublication().getOwner().toString());
            activityTableParameters.setActivityBookTitle(p.getPublication().getTitle());
            activityTableParameters.setActivityStatus(p.getStatus());
            activityHistoryTableData.add(activityTableParameters);
        }
        activityHistoryTable.setItems(activityHistoryTableData);
    }

    private void enableVisibility() {
        if (currentUser instanceof Client client) {
            allTabs.getTabs().remove(publicationManagementTab);
            allTabs.getTabs().remove(userManagementTab);
            allTabs.getTabs().remove(alternativeUserManagementTab);
            allTabs.getTabs().remove(clientActivityHistory);
            buyBookButton.setVisible(false);
            addMessageButton.setVisible(false);
        } else {
            allTabs.getTabs().remove(currentClientManagementTab);
            allTabs.getTabs().remove(clientBookManagementTab);
            allTabs.getTabs().remove(clientNotificationTab);
            allTabs.getTabs().remove(activityOverviewTab);
            publicationActionButton.setVisible(false);
            buyBookButton.setVisible(false);
            addMessageButton.setVisible(false);
        }
    }

    //<editor-fold desc="user window">

    private void fillUserTable() {
        List<User> allUsers = hibernate.getAllRecords(User.class);
        for (User u : allUsers) {
            UserTableParameters userTableParameters = new UserTableParameters();
            userTableParameters.setId(u.getId());
            userTableParameters.setLogin(u.getLogin());
            userTableParameters.setPassword(u.getPassword());
            userTableParameters.setName(u.getName());
            userTableParameters.setSurname(u.getSurname());
            if (u instanceof Admin admin) {
                userTableParameters.setPhoneNumber(admin.getPhoneNum());
            } else if (u instanceof Client client) {
                userTableParameters.setAddress(client.getAddress());
                userTableParameters.setBirthDate(String.valueOf(client.getBirthDate()));
            }
            data.add(userTableParameters);
        }
        userTable.setItems(data);
    }

    public void fillUserList() {
        allUsersListView.getItems().clear();
        List<User> userList = hibernate.getAllRecords(User.class);
        allUsersListView.getItems().addAll(userList);
    }

    public void createNewUser() {
        String salt = PasswordHasher.generateSalt();
        if (clientRadio.isSelected()) {
            Client client = new Client(loginField.getText(), PasswordHasher.hashPassword(passwordField.getText(), salt), salt, nameField.getText(), surnameField.getText(), addressField.getText(), birthDate.getValue());
            hibernate.create(client);
        } else {
            Admin admin = new Admin(loginField.getText(), PasswordHasher.hashPassword(passwordField.getText(), salt), salt, nameField.getText(), surnameField.getText(), phoneNumField.getText());
            hibernate.create(admin);
        }
        fillUserList();
        data.clear();
        fillUserTable();
    }

    public void clientRadioClick() {
        if (clientRadio.isSelected()) {
            phoneNumField.setDisable(true);
            birthDate.setDisable(false);
            addressField.setDisable(false);
        }
    }

    public void adminRadioClick() {
        if (adminRadio.isSelected()) {
            birthDate.setDisable(true);
            addressField.setDisable(true);
            phoneNumField.setDisable(false);
        }
    }

    public void loadSelectedUser() {
        clearFields();
        User selectedUser = allUsersListView.getSelectionModel().getSelectedItem();

        User userInfoFromDb = hibernate.getEntityById(User.class, selectedUser.getId());

        loginField.setText(userInfoFromDb.getLogin());
        passwordField.setText(userInfoFromDb.getPassword());
        nameField.setText(userInfoFromDb.getName());
        surnameField.setText(userInfoFromDb.getSurname());

        if (userInfoFromDb instanceof Client client) {
            clientRadio.setSelected(true);
            birthDate.setValue(client.getBirthDate());
            addressField.setText(client.getAddress());
            clientRadioClick();
        } else {
            Admin admin = (Admin) userInfoFromDb;
            adminRadio.setSelected(true);
            phoneNumField.setText(admin.getPhoneNum());
            adminRadioClick();
        }
    }

    public void updateSelectedUser() {
        User selectedUser = allUsersListView.getSelectionModel().getSelectedItem();
        User userInfoFromDb = hibernate.getEntityById(User.class, selectedUser.getId());

        userInfoFromDb.setName(nameField.getText());
        userInfoFromDb.setSurname(surnameField.getText());
        userInfoFromDb.setLogin(loginField.getText());
        userInfoFromDb.setPassword(passwordField.getText());
        if (userInfoFromDb instanceof Client) {
            Client client = (Client) userInfoFromDb;
            clientRadio.setSelected(true);
            birthDate.setValue(client.getBirthDate());
            addressField.setText(client.getAddress());
        } else if (userInfoFromDb instanceof Admin) {
            Admin admin = (Admin) userInfoFromDb;
            adminRadio.setSelected(true);
            phoneNumField.setText(admin.getPhoneNum());
        }

        hibernate.update(userInfoFromDb);
        fillUserList();
    }

    public void deleteSelectedUser() {
        User selectedUser = allUsersListView.getSelectionModel().getSelectedItem();
        hibernate.delete(User.class, selectedUser.getId());
        fillUserList();
    }

    public void clearFields() {
        nameField.clear();
        surnameField.clear();
        phoneNumField.clear();
        addressField.clear();
        birthDate.setValue(null);
        loginField.clear();
        passwordField.clear();
        clientRadio.setSelected(false);
        adminRadio.setSelected(false);
        addressField.setDisable(true);
        birthDate.setDisable(true);
        phoneNumField.setDisable(true);
    }

    public void goToPublications(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("publicationsWindow.fxml"));
        Parent parent = fxmlLoader.load();
        PublicationsWindow publicationsWindow = fxmlLoader.getController();
        publicationsWindow.setData(currentUser);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Book Exchange Test");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        fillBookTable();
    }

    //</editor-fold>

    public void reserveBook() {
        Publication publication = availableBookList.getSelectionModel().getSelectedItem();
        if (publication != null) {
            if (borrowedBookStatusCombobox.getSelectionModel().getSelectedItem() == PublicationStatus.AVAILABLE) {
                Publication publicationFromDB = hibernate.getEntityById(Publication.class, publication.getId());
                publicationFromDB.setPublicationStatus(PublicationStatus.REQUESTED);
                publicationFromDB.setRequestDate(LocalDate.now());
                publicationFromDB.setClient((Client) currentUser);
                hibernate.update(publicationFromDB);
                if (currentUser instanceof Client) {
                    PeriodicRecord periodicRecord = new PeriodicRecord((Client) currentUser, publicationFromDB, LocalDate.now(), PublicationStatus.REQUESTED);
                    periodicRecord.setRead(false);
                    hibernate.create(periodicRecord);
                }
            } else if (borrowedBookStatusCombobox.getSelectionModel().getSelectedItem() == PublicationStatus.REQUESTED) {
                Publication publicationFromDB = hibernate.getEntityById(Publication.class, publication.getId());
                publicationFromDB.setPublicationStatus(PublicationStatus.TAKEN);
                hibernate.update(publicationFromDB);
            } else if (borrowedBookStatusCombobox.getSelectionModel().getSelectedItem() == PublicationStatus.TAKEN) {
                Publication publicationFromDB = hibernate.getEntityById(Publication.class, publication.getId());
                publicationFromDB.setPublicationStatus(PublicationStatus.AVAILABLE);
                publicationFromDB.setClient(null);
                hibernate.update(publicationFromDB);
                if (currentUser instanceof Client) {
                    PeriodicRecord periodicRecord = hibernate.getPeriodicRecordByClientAndPublication(hibernate.getEntityById(Client.class, currentUser.getId()), hibernate.getEntityById(Publication.class, publicationFromDB.getId()));
                    periodicRecord.setRead(false);
                    periodicRecord.setStatus(PublicationStatus.TAKEN);
                    periodicRecord.setReturnDate(LocalDate.now());
                    hibernate.update(periodicRecord);
                }
                aboutBook.clear();
                ownerBio.clear();
                ownerInfo.setText("Owner info:");
            }
        }
        fillAvailableBooksList();
    }

    public void buyAvailableBook() {
        Publication publication = availableBookList.getSelectionModel().getSelectedItem();
        if (publication != null) {
            if (borrowedBookStatusCombobox.getSelectionModel().getSelectedItem() == PublicationStatus.AVAILABLE) {
                Publication publicationFromDB = hibernate.getEntityById(Publication.class, publication.getId());
                publicationFromDB.setPublicationStatus(PublicationStatus.SOLD);
                publicationFromDB.setRequestDate(LocalDate.now());
                publicationFromDB.setClient((Client) currentUser);
                hibernate.update(publicationFromDB);
                if (currentUser instanceof Client) {
                    PeriodicRecord periodicRecord = new PeriodicRecord((Client) currentUser, publicationFromDB, LocalDate.now(), PublicationStatus.RESERVED);
                    hibernate.create(periodicRecord);
                }
            }
        }
    }

    public void chatWithOwner() throws IOException {
        if (availableBookList.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("chatWindow.fxml"));
            Parent parent = fxmlLoader.load();
            ChatWindow chatWindow = fxmlLoader.getController();
            chatWindow.setData(entityManagerFactory, currentUser, (Book) availableBookList.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setTitle(availableBookList.getSelectionModel().getSelectedItem().getTitle() + ": " + availableBookList.getSelectionModel().getSelectedItem().getOwner().getName() + " " + availableBookList.getSelectionModel().getSelectedItem().getOwner().getSurname());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    public void loadData() {
        //ai spaudziam ant tab, tai tik tam tab buildinam duomenis
        if (userManagementTab.isSelected()) {
            fillUserList();
        } else if (bookExchangeTab.isSelected()) {
            fillAvailableBooksList();
        } else if (clientNotificationTab.isSelected()) {
            fillNotificationTable();
        } else if (alternativeUserManagementTab.isSelected()) {
            userTable.getItems().clear();
            fillUserTable();
        } else if (clientBookManagementTab.isSelected()) {
            fillBookTable();
        } else if (clientActivityHistory.isSelected()) {
            fillActivityHistoryTable();
        } else if (currentClientManagementTab.isSelected()) {
            fillCurrentUserInfo();
        } else if (statisticsTab.isSelected()) {
            fillStatistics();
        }
    }

    private void fillAvailableBooksList() {
        availableBookList.getItems().clear();
        List<Publication> publications = hibernate.getAllRecords(Publication.class);

        PublicationStatus selectedStatus = borrowedBookStatusCombobox.getSelectionModel().getSelectedItem();
        Types selectedType = typeFilterMain.getSelectionModel().getSelectedItem();
        Format selectedFormat = formatFilterMain.getSelectionModel().getSelectedItem();
        Genre selectedGenre = genreFilterMain.getSelectionModel().getSelectedItem();
        Language selectedLanguage = languageFilterMain.getSelectionModel().getSelectedItem();

        boolean isAdmin = currentUser instanceof Admin;

        if (selectedStatus != null) {
            if (selectedStatus == PublicationStatus.AVAILABLE) {
                publicationActionButton.setText("Request publication");
                if (currentUser instanceof Client) {
                    buyBookButton.setVisible(true);
                }

                for (Publication p : publications) {
                    boolean matchesFilters = true;

                    if (selectedType == Types.BOOK) {
                        if (p instanceof Book book) {
                            matchesFilters = (selectedFormat == null || book.getFormat() == selectedFormat) &&
                                    (selectedGenre == null || book.getGenre() == selectedGenre) &&
                                    (selectedLanguage == null || book.getLanguage() == selectedLanguage);
                        } else {
                            matchesFilters = false;
                        }
                    } else if (selectedType != null) {
                        matchesFilters = false;
                    }

                    if (isAdmin) {
                        if (matchesFilters && p.getOwner() != null && p.getOwner().getId() != currentUser.getId() && p.getPublicationStatus() == PublicationStatus.AVAILABLE) {
                            availableBookList.getItems().add(p);
                        }
                    } else {
                        if (matchesFilters && p.getOwner() != null && p.getOwner().getId() != currentUser.getId() && selectedStatus == p.getPublicationStatus()) {
                            availableBookList.getItems().add(p);
                        }
                    }
                }
            } else if (selectedStatus == PublicationStatus.REQUESTED) {
                buyBookButton.setVisible(false);
                publicationActionButton.setText("Received publication");

                for (Publication p : publications) {
                    boolean matchesFilters = true;

                    if (selectedType == Types.BOOK) {
                        if (p instanceof Book book) {
                            matchesFilters = (selectedFormat == null || book.getFormat() == selectedFormat) &&
                                    (selectedGenre == null || book.getGenre() == selectedGenre) &&
                                    (selectedLanguage == null || book.getLanguage() == selectedLanguage);
                        } else {
                            matchesFilters = false;
                        }
                    } else if (selectedType != null) {
                        matchesFilters = false;
                    }

                    if (isAdmin) {
                        if (matchesFilters && p.getPublicationStatus() == PublicationStatus.REQUESTED && (p.getClient() != null && p.getClient().getId() != currentUser.getId())) {
                            availableBookList.getItems().add(p);
                        }
                    } else {
                        if (matchesFilters && p.getClient() != null && p.getClient().getId() == currentUser.getId() && selectedStatus == p.getPublicationStatus()) {
                            availableBookList.getItems().add(p);
                        }
                    }
                }
            } else if (selectedStatus == PublicationStatus.TAKEN) {
                buyBookButton.setVisible(false);
                publicationActionButton.setText("Return publication");

                for (Publication p : publications) {
                    boolean matchesFilters = true;

                    if (selectedType == Types.BOOK) {
                        if (p instanceof Book book) {
                            matchesFilters = (selectedFormat == null || book.getFormat() == selectedFormat) &&
                                    (selectedGenre == null || book.getGenre() == selectedGenre) &&
                                    (selectedLanguage == null || book.getLanguage() == selectedLanguage);
                        } else {
                            matchesFilters = false;
                        }
                    } else if (selectedType != null) {
                        matchesFilters = false;
                    }

                    if (isAdmin) {
                        if (matchesFilters && p.getPublicationStatus() == PublicationStatus.TAKEN && (p.getClient() != null && p.getClient().getId() != currentUser.getId())) {
                            availableBookList.getItems().add(p);
                        }
                    } else {
                        if (matchesFilters && p.getClient() != null && p.getClient().getId() == currentUser.getId() && selectedStatus == p.getPublicationStatus()) {
                            availableBookList.getItems().add(p);
                        }
                    }
                }
            } else {
                publicationActionButton.setText("Select a filter");
            }
        }
    }

    private void fillCurrentUserInfo() {
        Client client = (Client) hibernate.getEntityById(User.class, currentUser.getId());
        currentClientLoginField.setText(client.getLogin());
        currentClientPasswordField.setText(client.getPassword());
        currentClientNameField.setText(client.getName());
        currentClientSurnameField.setText(client.getSurname());
        currentClientAddressField.setText(client.getAddress());
        currentClientBirthDate.setValue(client.getBirthDate());
        currentClientBioField.setText(client.getClientBio());
    }

    public void loadPublicationInfo() {
        Publication publication = availableBookList.getSelectionModel().getSelectedItem();
        Publication publicationFromDb = hibernate.getEntityById(Publication.class, publication.getId());

        if (publicationFromDb instanceof Book book) {
            aboutBook.setText("Title: " + book.getTitle() + "\n" + "Year: " + book.getPublicationYear());
            if (currentUser instanceof Client) {
                buyBookButton.setVisible(true);
            }
        } else if (publicationFromDb instanceof Manga manga) {
            aboutBook.setText("Illustrator: " + manga.getIllustrator() + "\n" + "Volume number: " + manga.getVolumeNumber());
        } else if (publicationFromDb instanceof Periodical periodical) {
            aboutBook.setText("Publisher: " + periodical.getPublisher() + "\n" + "Issue number: " + periodical.getIssueNumber());
        }
        ownerInfo.setText(publicationFromDb.getOwner().getName() + " " + publicationFromDb.getOwner().getSurname());
        ownerBio.setText(publicationFromDb.getOwner().getClientBio());
        if (currentUser instanceof Client) {
            addMessageButton.setVisible(true);
        }
    }

    public void loadReviewWindow() throws IOException {
        if (availableBookList.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("userReview.fxml"));
            Parent parent = fxmlLoader.load();
            UserReview userReview = fxmlLoader.getController();
            userReview.setData(entityManagerFactory, currentUser, availableBookList.getSelectionModel().getSelectedItem().getOwner());
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setTitle(availableBookList.getSelectionModel().getSelectedItem().getOwner().getName() + " " +
                    availableBookList.getSelectionModel().getSelectedItem().getOwner().getSurname() + " reviews");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    private void fillBookTable() {
        bookTableData.clear();
        bookTable.getItems().clear();
        List<Publication> allRecords = hibernate.getOwnPublications(currentUser);
        if (currentUser instanceof Client) {
            for (Publication p : allRecords) {
                if (currentUser.getId() == p.getOwner().getId() && p.getPublicationStatus() != PublicationStatus.HIDDEN) {
                    BookTableParameters bookTableParameters = new BookTableParameters();
                    bookTableParameters.setPublicationID(p.getId());
                    bookTableParameters.setPublicationTitle(p.getTitle());
                    bookTableParameters.setPublicationUser(p.getOwner().getName() + " " + p.getOwner().getSurname());
                    bookTableParameters.setPublicationStatus(p.getPublicationStatus());
                    PeriodicRecord latestRecord = hibernate.getLatestPeriodicRecordByPublication(p);
                    if (latestRecord != null){
                        bookTableParameters.setPublicationRequestDate(String.valueOf(latestRecord.getTransactionDate()));
                    }
                    bookTableData.add(bookTableParameters);
                }
            }
        } else {
            for (Publication p : allRecords) {
                BookTableParameters bookTableParameters = new BookTableParameters();
                bookTableParameters.setPublicationID(p.getId());
                bookTableParameters.setPublicationTitle(p.getTitle());
                bookTableParameters.setPublicationUser(p.getOwner().getName() + " " + p.getOwner().getSurname());
                bookTableParameters.setPublicationStatus(p.getPublicationStatus());
                PeriodicRecord latestRecord = hibernate.getLatestPeriodicRecordByPublication(p);
                if (latestRecord != null){
                    bookTableParameters.setPublicationRequestDate(String.valueOf(latestRecord.getTransactionDate()));
                }
                bookTableData.add(bookTableParameters);
            }
        }
        bookTable.setItems(bookTableData);
    }

    public void openPublicationSubmissionWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("publicationsWindow.fxml"));
        Parent parent = fxmlLoader.load();
        PublicationsWindow publicationsWindow = fxmlLoader.getController();
        publicationsWindow.setData(currentUser);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Book Exchange Test");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        fillBookTable();
    }

    public void calculateOverview() {
        List<PeriodicRecord> allRecords = hibernate.getAllRecords(PeriodicRecord.class);
        List<Publication> filteredPublications = new ArrayList<>();
        int borrowedCount = 0;
        int soldCount = 0;
        int borrowedToCount = 0;
        int soldToCount = 0;
        if (dateRadialButton.isSelected() && fromFilterSelect.getValue() != null && toFilterSelect.getValue() != null) {
            LocalDate fromDate = fromFilterSelect.getValue();
            LocalDate toDate = toFilterSelect.getValue();
            allRecords = allRecords.stream()
                    .filter(record -> record.getTransactionDate() != null &&
                            !record.getTransactionDate().isBefore(fromDate) &&
                            !record.getTransactionDate().isAfter(toDate))
                    .collect(Collectors.toList());
        }
        if (borrowedCountField.getText() != null && !borrowedCountField.getText().isEmpty()) {
            int borrowedCountFilter = Integer.parseInt(borrowedCountField.getText());
            allRecords = allRecords.stream()
                    .filter(record -> record.getStatus() == PublicationStatus.TAKEN)
                    .collect(Collectors.groupingBy(PeriodicRecord::getPublication))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().size() >= borrowedCountFilter)
                    .flatMap(entry -> entry.getValue().stream())
                    .collect(Collectors.toList());
        }
        if (soldCountField.getText() != null && !soldCountField.getText().isEmpty()) {
            int soldCountFilter = Integer.parseInt(soldCountField.getText());
            allRecords = allRecords.stream()
                    .filter(record -> record.getStatus() == PublicationStatus.SOLD)
                    .collect(Collectors.groupingBy(PeriodicRecord::getPublication))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().size() >= soldCountFilter)
                    .flatMap(entry -> entry.getValue().stream())
                    .collect(Collectors.toList());
        }
        if (formatRadialButton.isSelected() && formatTypeFilter.getSelectionModel().getSelectedItem() != null) {
            Format selectedFormat = formatTypeFilter.getSelectionModel().getSelectedItem();
            allRecords = allRecords.stream()
                    .filter(record -> record.getPublication() instanceof Book &&
                            ((Book) record.getPublication()).getFormat() == selectedFormat)
                    .collect(Collectors.toList());
        }
        if (genreRadialButton.isSelected() && genreFilterSelect.getSelectionModel().getSelectedItem() != null) {
            Genre selectedGenre = genreFilterSelect.getSelectionModel().getSelectedItem();
            allRecords = allRecords.stream()
                    .filter(record -> record.getPublication() instanceof Book &&
                            ((Book) record.getPublication()).getGenre() == selectedGenre)
                    .collect(Collectors.toList());
        }
        if (languageRadialButton.isSelected() && languageFilterSelect.getSelectionModel().getSelectedItem() != null) {
            Language selectedLanguage = languageFilterSelect.getSelectionModel().getSelectedItem();
            allRecords = allRecords.stream()
                    .filter(record -> record.getPublication() instanceof Book &&
                            ((Book) record.getPublication()).getLanguage() == selectedLanguage)
                    .collect(Collectors.toList());
        }
        borrowedCount = (int) allRecords.stream()
                .filter(record -> record.getStatus() == PublicationStatus.TAKEN &&
                        record.getUser().getId() == currentUser.getId())
                .count();
        borrowedToCount = (int) allRecords.stream()
                .filter(record -> record.getStatus() == PublicationStatus.TAKEN &&
                        record.getPublication().getOwner().getId() == currentUser.getId())
                .count();
        soldCount = (int) allRecords.stream()
                .filter(record -> record.getStatus() == PublicationStatus.SOLD &&
                        record.getUser().getId() == currentUser.getId())
                .count();
        soldToCount = (int) allRecords.stream()
                .filter(record -> record.getStatus() == PublicationStatus.SOLD &&
                        record.getPublication().getOwner().getId() == currentUser.getId())
                .count();

        borrowedCountField.setText(String.valueOf(borrowedCount));
        soldCountField.setText(String.valueOf(soldCount));
        borrowedToCountField.setText(String.valueOf(borrowedToCount));
        soldToCountField.setText(String.valueOf(soldToCount));

        filteredPublications = allRecords.stream()
                .map(PeriodicRecord::getPublication)
                .distinct()
                .collect(Collectors.toList());

        ObservableList<Publication> observableFilteredPublications = FXCollections.observableArrayList(filteredPublications);
        overviewPublicationList.setItems(observableFilteredPublications);
    }

    public void selectedPublicationTypeFilter() {
        if (publicationFilterSelect.getSelectionModel().getSelectedItem() != Types.BOOK) {
            genreRadialButton.setSelected(false);
            genreRadialClick();
            languageRadialButton.setSelected(false);
            languageRadialClick();
            formatRadialButton.setSelected(false);
            formatRadialClick();
            genreRadialButton.setDisable(true);
            languageRadialButton.setDisable(true);
            formatRadialButton.setDisable(true);
        } else {
            genreRadialButton.setSelected(false);
            genreRadialClick();
            languageRadialButton.setSelected(false);
            languageRadialClick();
            formatRadialButton.setSelected(false);
            formatRadialClick();
            genreRadialButton.setDisable(false);
            languageRadialButton.setDisable(false);
            formatRadialButton.setDisable(false);
        }
    }

    public void formatRadialClick() {
        if (!formatRadialButton.isSelected()) {
            formatTypeFilter.setDisable(true);
        } else {
            formatTypeFilter.setDisable(false);
        }
    }

    public void dateRadialClick() {
        if (!dateRadialButton.isSelected()) {
            fromFilterSelect.setDisable(true);
            toFilterSelect.setDisable(true);
        } else {
            fromFilterSelect.setDisable(false);
            toFilterSelect.setDisable(false);
        }
    }

    public void genreRadialClick() {
        if (!genreRadialButton.isSelected()) {
            genreFilterSelect.setDisable(true);
        } else {
            genreFilterSelect.setDisable(false);
        }
    }

    public void publicationRadialClick() {
        if (!publicationRadialButton.isSelected()) {
            publicationFilterSelect.setDisable(true);
        } else {
            publicationFilterSelect.setDisable(false);
        }
    }

    public void languageRadialClick() {
        if (!languageRadialButton.isSelected()) {
            languageFilterSelect.setDisable(true);
        } else {
            languageFilterSelect.setDisable(false);
        }
    }

    public void updateCurrentClient() {
        Client clientFromDb = (Client) hibernate.getEntityById(User.class, currentUser.getId());
        clientFromDb.setLogin(currentClientLoginField.getText());
        if (!currentClientPasswordField.getText().equalsIgnoreCase(clientFromDb.getPassword())) {
            clientFromDb.setPassword(PasswordHasher.hashPassword(currentClientPasswordField.getText(), clientFromDb.getSalt()));
        }
        clientFromDb.setName(currentClientNameField.getText());
        clientFromDb.setAddress(currentClientAddressField.getText());
        clientFromDb.setBirthDate(currentClientBirthDate.getValue());
        clientFromDb.setClientBio(currentClientBioField.getText());
        hibernate.update(clientFromDb);
    }

    public void loadBorrowedBooksByStatus() {
        fillAvailableBooksList();
    }

    public void loadReviewOnBookWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("bookReview.fxml"));
        Parent parent = fxmlLoader.load();
        BookReview bookReview = fxmlLoader.getController();
        bookReview.setData(entityManagerFactory, currentUser, availableBookList.getSelectionModel().getSelectedItem().getOwner(), (Book) availableBookList.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle(availableBookList.getSelectionModel().getSelectedItem().getTitle());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void selectedTypeFilterInMain() {
        if (typeFilterMain.getSelectionModel().getSelectedItem() != Types.BOOK) {
            genreFilterMain.setDisable(true);
            formatFilterMain.setDisable(true);
            languageFilterMain.setDisable(true);
            fillAvailableBooksList();
        } else {
            genreFilterMain.setDisable(false);
            formatFilterMain.setDisable(false);
            languageFilterMain.setDisable(false);
            fillAvailableBooksList();
        }
    }

    public void selectedFormatFilterInMain() {
        fillAvailableBooksList();
    }

    public void selectedGenreFilterInMain() {
        fillAvailableBooksList();
    }

    public void selectedLanguageFilterInMain() {
        fillAvailableBooksList();
    }

    private void fillStatistics() {
        long totalUsers = hibernate.getTotalUsers();
        long totalBorrowed = hibernate.getTotalBorrowedPublications();
        long totalSold = hibernate.getTotalSoldPublications();

        totalUsersField.setText(String.valueOf(totalUsers));
        totalBorrowedPublicationField.setText(String.valueOf(totalBorrowed));
        totalSoldPublicationField.setText(String.valueOf(totalSold));

        Map<String, Long> borrowedStats = hibernate.getMonthlyStatsByStatus(PublicationStatus.TAKEN);
        Map<String, Long> soldStats = hibernate.getMonthlyStatsByStatus(PublicationStatus.SOLD);

        XYChart.Series<String, Number> borrowedSeries = new XYChart.Series<>();
        borrowedSeries.setName("Borrowed Publications");

        XYChart.Series<String, Number> soldSeries = new XYChart.Series<>();
        soldSeries.setName("Sold Publications");

        Map<Integer, Long> monthlyData = new TreeMap<>();
        for (int i = 1; i <= 12; i++) {
            String month = String.format("%02d", i);
            long borrowedCount = borrowedStats.getOrDefault(month, 0L);
            long soldCount = soldStats.getOrDefault(month, 0L);

            borrowedSeries.getData().add(new XYChart.Data<>(month, borrowedCount));
            soldSeries.getData().add(new XYChart.Data<>(month, soldCount));
        }

        statisticChart.getData().clear();
        statisticChart.getData().addAll(borrowedSeries, soldSeries);

        NumberAxis yAxis = (NumberAxis) statisticChart.getYAxis();
        yAxis.setTickUnit(1);

        CategoryAxis xAxis = (CategoryAxis) statisticChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList(
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        ));
    }

    public void goToAllComments() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("commentWindow.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("All comments");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
