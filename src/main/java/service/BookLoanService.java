package service;

import models.Book;
import models.BookLoan;
import models.Employee;
import models.enums.BookStatus;
import repository.BookLoanRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

public class BookLoanService {
    private final BookLoanRepository bookLoanRepository;
    private final BookService bookService;
    private final EmployeeService employeeService;

    public BookLoanService(BookLoanRepository bookLoanRepository,
                           BookService bookService,
                           EmployeeService employeeService) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookService = bookService;
        this.employeeService = employeeService;
    }

    public List<BookLoan> getAllLoans() {
        return bookLoanRepository.getAllLoans();
    }

    public BookLoan getLoanById(int id) {
        BookLoan loan = bookLoanRepository.getLoanById(id);
        if (loan == null) {
            throw new NoSuchElementException("Loan with ID - " + id + " is not found.");
        }
        return loan;
    }

    public void addBookLoan(int bookId, int employeeId) {
        int id = bookLoanRepository.getAllLoans().stream()
                .mapToInt(BookLoan::getId)
                .max()
                .orElse(0) + 1;
        bookLoanRepository.addBookLoan(new BookLoan(id, bookId, employeeId, date));
    }

    public void addPastBookLoan(int bookId,
                                int employeeId,
                                LocalDate borrowedDate,
                                LocalDate returnDate,
                                LocalDate realReturnDate
    ) {
        int id = bookLoanRepository.getAllLoans().stream()
                .mapToInt(BookLoan::getId)
                .max()
                .orElse(0) + 1;
        bookLoanRepository.addBookLoan(new BookLoan(id, bookId, employeeId, borrowedDate, returnDate, realReturnDate));
    }

    public void borrowBook(int employeeId, int bookId) {
        Book book = bookService.getBookById(bookId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (isAvailable(employee, book)) {
            employee.getCurrentBooksId().add(bookId);
            bookService.updateBookStatus(bookId, BookStatus.TAKEN);
            addBookLoan(bookId, employeeId);
        }
    }

    public void returnBook(int employeeId, int bookId) {
        Book book = bookService.getBookById(bookId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (!hasBook(employee, book)) {
            throw new InputMismatchException(employee + "don't have " + book);
        }
        employee.getPrevBooksId().add(bookId);
        employee.getCurrentBooksId().remove(bookId);
    }

    public boolean isAvailable(Employee employee, Book book) {
        return book.getStatus() == BookStatus.AVAILABLE
                && employee.getCurrentBooksId().size() <= 2;
    }

    public boolean hasBook(Employee employee, Book book) {
        return employee.getCurrentBooksId().contains(book);
    }
}
