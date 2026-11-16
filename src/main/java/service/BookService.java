package service;

import models.Book;
import models.enums.BookStatus;
import repository.BookRepository;

import java.util.List;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public Book getBookById(int id) {
        return bookRepository.getBookById(id);
    }

    public void addNewBook(String name, String author, String description, String cover) {
        int id = bookRepository.getAllBooks().stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0) + 1;
        bookRepository.addBook(new Book(id, name, author, description, cover));
    }

    public void updateBookStatus(int id, BookStatus status) {
        bookRepository.updateStatus(id, status);
    }
}
