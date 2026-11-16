package models;

import models.enums.BookLoanStatus;

import java.time.LocalDate;

public class BookLoan {
    private final int id;
    private int bookId;
    private int employeeId;
    private LocalDate borrowedDate;
    private LocalDate returnDate;
    private LocalDate realReturnDate;
    private BookLoanStatus status;

    public BookLoan(int id,
                    int bookId,
                    int employeeId,
                    LocalDate borrowedDate,
                    LocalDate returnDate,
                    LocalDate realReturnDate
    ) {
        this.id = id;
        this.bookId = bookId;
        this.employeeId = employeeId;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
        this.realReturnDate = realReturnDate;
        this.status = BookLoanStatus.RETURNED;
    }

    public BookLoan(int id, int bookId, int employeeId, LocalDate borrowedDate) {
        this.id = id;
        this.bookId = bookId;
        this.employeeId = employeeId;
        this.borrowedDate = borrowedDate;
        this.returnDate = borrowedDate.plusDays(14);
        this.realReturnDate = null;
        this.status = BookLoanStatus.ACTIVE;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getRealReturnDate() {
        return realReturnDate;
    }

    public void setRealReturnDate(LocalDate realReturnDate) {
        this.realReturnDate = realReturnDate;
    }

    public BookLoanStatus getStatus() {
        return status;
    }

    public void setStatus(BookLoanStatus status) {
        this.status = status;
    }
}
