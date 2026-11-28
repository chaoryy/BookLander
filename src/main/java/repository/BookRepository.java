package repository;

import models.Book;
import models.enums.BookStatus;
import utils.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final List<Book> books;

    public BookRepository(List<Book> books) {
        this.books = books == null ? new ArrayList<>() : new ArrayList<>(books);
    }

    public Book getBookById(int id) {
        return books.stream().filter((book -> book.getId() == id))
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public void addBook(Book book) throws IOException {
        try {
            books.add(book);
            FileUtil.writeBooks(books);
        } catch (IOException e) {
            books.remove(book);
            throw e;
        }
    }

    public void updateStatus(int id, BookStatus status) throws IOException {
        if (status == null) {
            throw new IllegalArgumentException("Status can not be null");
        }
        Book target = null;
        for (Book book : books) {
            if (book.getId() == id) {
                target = book;
                break;
            }
        }
        if (target == null) {
            throw new IllegalStateException("No book with id " + id);
        }
        BookStatus oldStatus = target.getStatus();
        target.setStatus(status);
        try {
            FileUtil.writeBooks(books);
        } catch (IOException e) {
            target.setStatus(oldStatus);
            throw e;
        }
    }
}
