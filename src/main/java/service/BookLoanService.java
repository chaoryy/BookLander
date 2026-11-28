package service;

import models.Book;
import models.BookLoan;
import models.Employee;
import models.enums.BookLoanStatus;
import models.enums.BookStatus;
import repository.BookLoanRepository;
import utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

    public void addBookLoan(int bookId, int employeeId) throws IOException {
        int id = bookLoanRepository.getAllLoans().stream()
                .mapToInt(BookLoan::getId)
                .max()
                .orElse(0) + 1;
        bookLoanRepository.addBookLoan(new BookLoan(id, bookId, employeeId));
    }

    public void addPastBookLoan(int bookId, int employeeId, LocalDate borrowedDate,
                                LocalDate returnDate, LocalDate realReturnDate) throws IOException {
        int id = bookLoanRepository.getAllLoans().stream()
                .mapToInt(BookLoan::getId)
                .max()
                .orElse(0) + 1;
        bookLoanRepository.addBookLoan(new BookLoan(id, bookId, employeeId, borrowedDate, returnDate, realReturnDate));
    }

    public void updateLoanStatus(int id, BookLoanStatus status) throws IOException {
        bookLoanRepository.updateLoanStatus(id, status);
    }

    public void borrowBook(int employeeId, int bookId) throws IOException {
        Book book = getBookOrThrow(bookId);
        Employee employee = getEmployeeOrThrow(employeeId);
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("This book is already taken!");
        }
        if (employee.getCurrentBooksId().size() >= 2) {
            throw new IllegalStateException("You already have 2 books!");
        }
        List<Integer> oldCurrentBooks = new ArrayList<>(employee.getCurrentBooksId());
        List<Integer> prev = new ArrayList<>(employee.getPrevBooksId());
        List<Integer> newCurrent = new ArrayList<>(oldCurrentBooks);
        newCurrent.add(bookId);
        int step = 0;
        try {
            bookService.updateBookStatus(bookId, BookStatus.TAKEN);
            step = 1;
            employeeService.updateBooks(employeeId, newCurrent, prev);
            step = 2;
            addBookLoan(bookId, employeeId);
        } catch (IOException e) {
            if (step >= 2) {
                try {
                    employeeService.updateBooks(employeeId, oldCurrentBooks, prev);
                } catch (IOException ex) {
                    e.addSuppressed(ex);
                }
            }
            if (step >= 1) {
                try {
                    bookService.updateBookStatus(bookId, BookStatus.AVAILABLE);
                } catch (IOException ex) {
                    e.addSuppressed(ex);
                }
            }
            throw e;
        }
    }

    public void returnBook(int employeeId, int bookId) throws IOException {
        Employee employee = getEmployeeOrThrow(employeeId);
        Book book = getBookOrThrow(bookId);
        if (!hasBook(employee, bookId)) {
            throw new IllegalStateException(employee + "don't have " + book);
        }
        BookLoan activeLoan = bookLoanRepository.getActiveLoanByBookId(bookId);
        if (activeLoan == null || activeLoan.getEmployeeId() != employeeId) {
            throw new IllegalStateException("Couldn't find loan with employeeID - " + employeeId + " and bookID -" + bookId);
        }
        List<Integer> oldCurrentBooks = new ArrayList<>(employee.getCurrentBooksId());
        List<Integer> oldPrevBooks = new ArrayList<>(employee.getPrevBooksId());
        List<Integer> newCurrent = new ArrayList<>(oldCurrentBooks);
        newCurrent.remove(Integer.valueOf(bookId));
        List<Integer> newPrev = new ArrayList<>(oldPrevBooks);
        newPrev.add(bookId);

        int step = 0;
        try {
            bookService.updateBookStatus(bookId, BookStatus.AVAILABLE);
            step = 1;
            employeeService.updateBooks(employeeId, newCurrent, newPrev);
            step = 2;
            bookLoanRepository.closeLoan(activeLoan.getId(), LocalDate.now(ZoneId.of("Asia/Bishkek")));
        } catch (IOException e) {
            if (step >= 2) {
                try {
                    employeeService.updateBooks(employeeId, oldCurrentBooks, oldPrevBooks);
                } catch (IOException ex) {
                    e.addSuppressed(ex);
                }
            }
            if (step >= 1) {
                try {
                    bookService.updateBookStatus(bookId, BookStatus.TAKEN);
                } catch (IOException ex) {
                    e.addSuppressed(ex);
                }
            }
            throw e;
        }

    }

    private boolean hasBook(Employee employee, int bookId) {
        return employee.getCurrentBooksId().contains(bookId);
    }

    private Employee getEmployeeOrThrow(int employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            throw new NoSuchElementException("There is no employee with id-" + employeeId);
        }
        return employee;
    }

    private Book getBookOrThrow(int bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new NoSuchElementException("There is no book with id-" + bookId);
        }
        return book;
    }
}
