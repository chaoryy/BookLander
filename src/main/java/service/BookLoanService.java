package service;

import models.Book;
import models.BookLoan;
import models.Employee;
import models.enums.BookLoanStatus;
import models.enums.BookStatus;
import repository.BookLoanRepository;

import java.time.LocalDate;
import java.time.ZoneId;
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
        bookLoanRepository.addBookLoan(new BookLoan(id, bookId, employeeId));
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

    public void updateLoanStatus(int id, BookLoanStatus status) {
        bookLoanRepository.updateLoanStatus(id, status);
    }

    public void borrowBook(int employeeId, int bookId) {
        Book book = getBookOrThrow(bookId);
        Employee employee = getEmployeeOrThrow(employeeId);
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("This book is already taken!");
        }
        if (employee.getCurrentBooksId().size() >= 2) {
            throw new IllegalStateException("You already have 2 books!");
        }

        employee.getCurrentBooksId().add(bookId);
        bookService.updateBookStatus(bookId, BookStatus.TAKEN);
        addBookLoan(bookId, employeeId);
    }

    public void returnBook(int employeeId, int bookId) {
        Employee employee = getEmployeeOrThrow(employeeId);
        Book book = getBookOrThrow(bookId);
        if (!hasBook(employee, bookId)) {
            throw new IllegalStateException(employee + "don't have " + book);
        }
        BookLoan activeLoan = bookLoanRepository.getActiveLoanByBookId(bookId);
        if (activeLoan == null) {
            throw new IllegalStateException("Couldn't find loan with employeeID - " + employeeId + " and bookID -" + bookId);
        }
        employee.getPrevBooksId().add(bookId);
        employee.getCurrentBooksId().remove(Integer.valueOf(bookId));
        bookService.updateBookStatus(bookId, BookStatus.AVAILABLE);
        activeLoan.setRealReturnDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        updateLoanStatus(activeLoan.getId(), BookLoanStatus.RETURNED);
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
