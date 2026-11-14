package repository;

import models.Book;
import models.enums.BookStatus;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private List<Book> books = new ArrayList<>();

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

    public void addBook(Book book) {
        books.add(book);
    }

    public void updateStatus(int id, BookStatus status) {
        for (Book book : books) {
            if (book.getId() == id) {
                book.setStatus(status);
                break;
            }
        }
    }
}
