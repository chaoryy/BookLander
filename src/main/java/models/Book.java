package models;

import models.enums.BookStatus;

public class Book {
    private final int id;
    private String name;
    private String author;
    private String cover;
    private BookStatus status;

    public Book(int id, String name, String author, String cover) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.cover = cover;
        this.status = BookStatus.AVAILABLE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
