package repository;

import models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    List<Book> books = new ArrayList<>();

    public BookRepository(List<Book> books) {
        this.books = books;
    }

    public Book getBookById(int id) {
        return books.stream().filter((book -> book.getId() == id))
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}
