package org.example.demo.controllers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.demo.hibernateControllers.GenericHibernate;
import org.example.demo.model.*;
import org.example.demo.model.enums.*;
import org.example.demo.utils.FxUtils;

import java.util.ArrayList;
import java.util.List;

public class PublicationsWindow {
    @FXML
    public TextField pubTitleField;
    @FXML
    public TextField pubAuthorNameField;
    @FXML
    public ListView<Manga> mangaListView;
    @FXML
    public TextField mangaAuthorNameField;
    @FXML
    public TextField mangaTitleField;
    @FXML
    public TextField mangaIllustratorField;
    @FXML
    public TextField mangaLanguageField;
    @FXML
    public TextField mangaVolumeField;
    @FXML
    public CheckBox mangaIsColored;
    @FXML
    public ComboBox<Demographic> mangaDemographicDropdown;
    @FXML
    public ListView<Periodical> periodicalListView;
    @FXML
    public TextField periodicalAuthorNameField;
    @FXML
    public TextField periodicalTitleField;
    @FXML
    public TextField periodicalIssueField;
    @FXML
    public TextField periodicalEditorField;
    @FXML
    public TextField periodicalPublisherField;
    @FXML
    public ComboBox<Frequency> periodicalFrequencyDropdown;
    @FXML
    public DatePicker periodicalDateField;
    @FXML
    public ListView<Book> bookListView;
    @FXML
    public TextField bookAuthorField;
    @FXML
    public TextField bookTitleField;
    @FXML
    public TextField bookPublisherField;
    @FXML
    public TextField bookISBNField;
    @FXML
    public ComboBox<Genre> bookGenreDropdown;
    @FXML
    public TextField bookPageCountField;
    @FXML
    public ComboBox<Language> bookLanguageDropdown;
    @FXML
    public TextField bookPublicationYearField;
    @FXML
    public ComboBox<Format> bookFormatDropdown;
    @FXML
    public TextArea bookDescriptionField;

    private User currentUser;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("demo");
    GenericHibernate hibernate = new GenericHibernate(entityManagerFactory);

    public void setData(User user) {
        currentUser = user;
        fillMangaList();
        fillPeriodicalList();
        fillBookList();
    }

    public void initialize() {
        mangaDemographicDropdown.setItems(FXCollections.observableArrayList(Demographic.values()));
        periodicalFrequencyDropdown.setItems(FXCollections.observableArrayList(Frequency.values()));
        bookGenreDropdown.setItems(FXCollections.observableArrayList(Genre.values()));
        bookLanguageDropdown.setItems(FXCollections.observableArrayList(Language.values()));
        bookFormatDropdown.setItems(FXCollections.observableArrayList(Format.values()));
    }


    public void fillMangaList() {
        mangaListView.getItems().clear();
        List<Manga> mangaList = hibernate.getAllRecords(Manga.class);
        List<Manga> filteredMangaList = new ArrayList<>();
        if (!mangaList.isEmpty()) {
            if (currentUser instanceof Client) {
                for (Manga b : mangaList) {
                    if (b.getPublicationStatus() != null) {
                        if (b.getPublicationStatus() != PublicationStatus.HIDDEN && b.getOwner().equals(currentUser)) {
                            filteredMangaList.add(b);
                        }
                    }
                }
            } else {
                filteredMangaList.addAll(mangaList);
            }
            mangaListView.setItems(FXCollections.observableArrayList(filteredMangaList));
        }
    }

    public void fillPeriodicalList() {
        periodicalListView.getItems().clear();
        List<Periodical> periodicalList = hibernate.getAllRecords(Periodical.class);
        List<Periodical> filteredPeriodicalList = new ArrayList<>();
        if (!periodicalList.isEmpty()) {
            if (currentUser instanceof Client) {
            for (Periodical b : periodicalList) {
                if (b != null) {
                    if (b.getPublicationStatus() != PublicationStatus.HIDDEN && b.getOwner().equals(currentUser)) {
                        filteredPeriodicalList.add(b);
                    }
                }
            }
            } else {
                filteredPeriodicalList.addAll(periodicalList);
            }
            periodicalListView.setItems(FXCollections.observableArrayList(filteredPeriodicalList));
        }
    }

    public void fillBookList() {
        bookListView.getItems().clear();
        List<Book> bookList = hibernate.getAllRecords(Book.class);
        List<Book> filteredBookList = new ArrayList<>();
        if (!bookList.isEmpty()) {
            if (currentUser instanceof Client) {
                for (Book b : bookList) {
                    if (b != null) {
                        if (b.getPublicationStatus() != PublicationStatus.HIDDEN && b.getOwner().getId() == currentUser.getId()) {
                            filteredBookList.add(b);
                        }
                    }
                }
            } else {
                filteredBookList.addAll(bookList);
            }
            bookListView.setItems(FXCollections.observableArrayList(filteredBookList));
        }
    }

    public void clearPubFields() {
        pubTitleField.clear();
        pubAuthorNameField.clear();
    }

    public void mangaDemographicSelected(ActionEvent actionEvent) {
    }

    public void clearMangaFields() {
        mangaAuthorNameField.clear();
        mangaTitleField.clear();
        mangaIllustratorField.clear();
        mangaLanguageField.clear();
        mangaVolumeField.clear();
        mangaIsColored.isDisabled();
        mangaDemographicDropdown.setItems(null);
    }

    public void getSelectedManga() {
        clearPubFields();
        Manga selectedManga = mangaListView.getSelectionModel().getSelectedItem();
        Manga mangaFromDB = hibernate.getEntityById(Manga.class, selectedManga.getId());
        mangaAuthorNameField.setText(mangaFromDB.getAuthor());
        mangaTitleField.setText(mangaFromDB.getTitle());
        mangaIllustratorField.setText(mangaFromDB.getIllustrator());
        mangaLanguageField.setText(mangaFromDB.getOriginalLanguage());
        mangaVolumeField.setText(String.valueOf(mangaFromDB.getVolumeNumber()));
        mangaIsColored.setSelected(mangaFromDB.isColor());
        mangaDemographicDropdown.setValue(mangaFromDB.getDemographic());
    }

    public void createManga() {
        Manga manga = new Manga(mangaTitleField.getText(), mangaAuthorNameField.getText(), mangaIllustratorField.getText(), mangaLanguageField.getText(), Integer.parseInt(mangaVolumeField.getText()), mangaDemographicDropdown.getSelectionModel().getSelectedItem(), mangaIsColored.isSelected());
        manga.setOwner((Client) currentUser);
        manga.setPublicationStatus(PublicationStatus.AVAILABLE);
        hibernate.create(manga);
        fillMangaList();
    }

    public void updateManga() {
        Manga selectedManga = mangaListView.getSelectionModel().getSelectedItem();
        Manga mangaFromDB = hibernate.getEntityById(Manga.class, selectedManga.getId());

        mangaFromDB.setAuthor(mangaAuthorNameField.getText());
        mangaFromDB.setTitle(mangaTitleField.getText());
        mangaFromDB.setIllustrator(mangaIllustratorField.getText());
        mangaFromDB.setOriginalLanguage(mangaLanguageField.getText());
        mangaFromDB.setVolumeNumber(Integer.parseInt(mangaVolumeField.getText()));
        mangaFromDB.setDemographic(mangaDemographicDropdown.getSelectionModel().getSelectedItem());
        mangaFromDB.setColor(mangaIsColored.isSelected());

        hibernate.update(mangaFromDB);
        fillMangaList();
    }

    public void deleteManga() {
        Manga selectedManga = mangaListView.getSelectionModel().getSelectedItem();
        if (selectedManga != null) {
            Manga mangaFromDB = hibernate.getEntityById(Manga.class, selectedManga.getId());
            mangaFromDB.setPublicationStatus(PublicationStatus.HIDDEN);
            hibernate.update(mangaFromDB);
            fillMangaList();
            clearMangaFields();
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "GUI Error", "No manga selected for deletion.");
        }
    }

    public void clearPeriodicalFields() {
        periodicalAuthorNameField.clear();
        periodicalTitleField.clear();
        periodicalIssueField.clear();
        periodicalEditorField.clear();
        periodicalPublisherField.clear();
        periodicalFrequencyDropdown.setValue(null);
        periodicalDateField.setValue(null);
    }

    public void getSelectedPeriodical(MouseEvent mouseEvent) {
        clearPeriodicalFields();
        Periodical selectedPeriodical = periodicalListView.getSelectionModel().getSelectedItem();
        Periodical periodicalFromDB = hibernate.getEntityById(Periodical.class, selectedPeriodical.getId());
        periodicalAuthorNameField.setText(periodicalFromDB.getAuthor());
        periodicalTitleField.setText(periodicalFromDB.getTitle());
        periodicalIssueField.setText(String.valueOf(periodicalFromDB.getIssueNumber()));
        periodicalEditorField.setText(periodicalFromDB.getEditor());
        periodicalPublisherField.setText(periodicalFromDB.getPublisher());
        periodicalFrequencyDropdown.setValue(periodicalFromDB.getFrequency());
        periodicalDateField.setValue(periodicalFromDB.getPublicationDate());
    }

    public void createPeriodical() {
        Periodical periodical = new Periodical(periodicalTitleField.getText(),
                periodicalAuthorNameField.getText(),
                Integer.parseInt(periodicalIssueField.getText()),
                periodicalDateField.getValue(),
                periodicalEditorField.getText(),
                periodicalFrequencyDropdown.getValue(),
                periodicalPublisherField.getText());
        periodical.setOwner((Client) currentUser);
        periodical.setPublicationStatus(PublicationStatus.AVAILABLE);
        hibernate.create(periodical);
        fillPeriodicalList();
    }

    public void updatePeriodical() {
        Periodical selectedPeriodical = periodicalListView.getSelectionModel().getSelectedItem();
        Periodical periodicalFromDB = hibernate.getEntityById(Periodical.class, selectedPeriodical.getId());

        periodicalFromDB.setTitle(periodicalTitleField.getText());
        periodicalFromDB.setAuthor(periodicalAuthorNameField.getText());
        periodicalFromDB.setIssueNumber(Integer.parseInt(periodicalIssueField.getText()));
        periodicalFromDB.setPublicationDate(periodicalDateField.getValue());
        periodicalFromDB.setEditor(periodicalEditorField.getText());
        periodicalFromDB.setFrequency(periodicalFrequencyDropdown.getValue());
        periodicalFromDB.setPublisher(periodicalPublisherField.getText());

        hibernate.update(periodicalFromDB);
        fillPeriodicalList();
    }

    public void deletePeriodical() {
        Periodical selectedPeriodical = periodicalListView.getSelectionModel().getSelectedItem();
        if (selectedPeriodical != null) {
            Periodical periodicalFromDB = hibernate.getEntityById(Periodical.class, selectedPeriodical.getId());
            periodicalFromDB.setPublicationStatus(PublicationStatus.HIDDEN);
            hibernate.update(periodicalFromDB);
            fillPeriodicalList();
            clearPeriodicalFields();
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "GUI Error", "No periodical selected for deletion.");
        }
    }

    public void clearPeriodicalButton() {
        clearPeriodicalFields();
    }

    public void clearBookFields() {
        bookAuthorField.clear();
        bookTitleField.clear();
        bookPublisherField.clear();
        bookISBNField.clear();
        bookGenreDropdown.setValue(null);
        bookPageCountField.clear();
        bookLanguageDropdown.setValue(null);
        bookPublicationYearField.clear();
        bookFormatDropdown.setValue(null);
        bookDescriptionField.clear();
    }

    public void getSelectedBook() {
        clearBookFields();
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        Book bookFromDB = hibernate.getEntityById(Book.class, selectedBook.getId());
        bookAuthorField.setText(bookFromDB.getAuthor());
        bookTitleField.setText(bookFromDB.getTitle());
        bookPublisherField.setText(bookFromDB.getPublisher());
        bookISBNField.setText(bookFromDB.getIsbn());
        bookGenreDropdown.setValue(bookFromDB.getGenre());
        bookPageCountField.setText(String.valueOf(bookFromDB.getPageCount()));
        bookLanguageDropdown.setValue(bookFromDB.getLanguage());
        bookPublicationYearField.setText(String.valueOf(bookFromDB.getPublicationYear()));
        bookFormatDropdown.setValue(bookFromDB.getFormat());
        bookDescriptionField.setText(bookFromDB.getSummary());
    }

    public void createBook() {
        Book book = new Book(bookTitleField.getText(),
                bookAuthorField.getText(),
                bookPublisherField.getText(),
                bookISBNField.getText(),
                bookGenreDropdown.getValue(),
                Integer.parseInt(bookPageCountField.getText()),
                bookLanguageDropdown.getValue(),
                Integer.parseInt(bookPublicationYearField.getText()),
                bookFormatDropdown.getValue(),
                bookDescriptionField.getText());
        book.setOwner((Client) currentUser);
        book.setPublicationStatus(PublicationStatus.AVAILABLE);
        hibernate.create(book);
        fillBookList();
    }

    public void updateBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        Book bookFromDB = hibernate.getEntityById(Book.class, selectedBook.getId());

        bookFromDB.setTitle(bookTitleField.getText());
        bookFromDB.setAuthor(bookAuthorField.getText());
        bookFromDB.setPublisher(bookPublisherField.getText());
        bookFromDB.setIsbn(bookISBNField.getText());
        bookFromDB.setGenre(bookGenreDropdown.getValue());
        bookFromDB.setPageCount(Integer.parseInt(bookPageCountField.getText()));
        bookFromDB.setLanguage(bookLanguageDropdown.getValue());
        bookFromDB.setPublicationYear(Integer.parseInt(bookPublicationYearField.getText()));
        bookFromDB.setFormat(bookFormatDropdown.getValue());
        bookFromDB.setSummary(bookDescriptionField.getText());

        hibernate.update(bookFromDB);
        fillBookList();
    }

    public void deleteBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Book bookFromDB = hibernate.getEntityById(Book.class, selectedBook.getId());
            bookFromDB.setPublicationStatus(PublicationStatus.HIDDEN);
            hibernate.update(bookFromDB);
            fillBookList();
            clearBookFields();
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "GUI Error", "No book selected for deletion.");
        }
    }

    public void clearBookButton() {
        clearBookFields();
    }
}
