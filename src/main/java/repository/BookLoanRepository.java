package repository;

import models.BookLoan;
import models.enums.BookLoanStatus;

import java.util.ArrayList;
import java.util.List;

public class BookLoanRepository {
    private List<BookLoan> loans = new ArrayList<>();

    public BookLoanRepository(List<BookLoan> loans) {
        this.loans = loans;
    }

    public List<BookLoan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public BookLoan getLoanById(int id) {
        return loans.stream().filter(bookLoan -> bookLoan.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addBookLoan(BookLoan bookLoan) {
        loans.add(bookLoan);
    }

    public void updateLoanStatus(int id, BookLoanStatus status) {
        for (BookLoan loan : loans) {
            if (loan.getId() == id) {
                loan.setStatus(status);
                break;
            }
        }
    }

    public List<BookLoan> getLoansByEmployeeId(int employeeId) {
        return loans.stream().filter(loan -> loan.getEmployeeId() == employeeId)
                .toList();
    }

    public BookLoan getActiveLoanByBookId(int bookId) {
        return loans.stream().filter(loan -> loan.getBookId() == bookId && loan.getStatus() == BookLoanStatus.AVAILABLE)
                .findFirst()
                .orElse(null);
    }
}
