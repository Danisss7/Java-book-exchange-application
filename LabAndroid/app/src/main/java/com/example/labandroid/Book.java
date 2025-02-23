package com.example.labandroid;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String genre;
    private int pageCount;
    private String language;
    private int publicationYear;
    private String format;
    private String summary;
    private int ownerId;

    public Book() {
    }

    public Book(int id, String publisher, String title, String author) {
        this.id = id;
        this.publisher = publisher;
        this.title = title;
        this.author = author;
    }

    public Book(String title, String author, String publisher) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(String title, String author, String publisher, String isbn, String genre, int pageCount, String language, int publicationYear, String format, String summary) {
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String updatedBook) {
        title = updatedBook;
    }

    public void setAuthor(String updatedAuthor) {
        author = updatedAuthor;
    }

    public int getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre='" + genre + '\'' +
                ", pageCount=" + pageCount +
                ", language='" + language + '\'' +
                ", publicationYear=" + publicationYear +
                ", format='" + format + '\'' +
                ", summary='" + summary + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}

