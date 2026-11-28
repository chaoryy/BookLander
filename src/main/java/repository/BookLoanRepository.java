package repository;

import models.Book;
import models.BookLoan;
import models.enums.BookLoanStatus;
import utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookLoanRepository {
    private final List<BookLoan> loans;

    public BookLoanRepository(List<BookLoan> loans) {
        this.loans = loans == null ? new ArrayList<>() : new ArrayList<>(loans);
    }

    public List<BookLoan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public BookLoan getLoanById(int id) {
        return loans.stream().filter(bookLoan -> bookLoan.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addBookLoan(BookLoan bookLoan) throws IOException {
        try {
            loans.add(bookLoan);
            FileUtil.writeLoans(loans);
        } catch (IOException e) {
            loans.remove(bookLoan);
            throw e;
        }
    }

   public void closeLoan(int id, LocalDate realReturnDate) throws IOException {
       BookLoan target = getLoanById(id);
       if (target == null) {
           throw new IllegalArgumentException("No loan with id " + id);
       }
       BookLoanStatus oldStatus = target.getStatus();
       LocalDate oldDate = target.getRealReturnDate();
       target.setStatus(BookLoanStatus.RETURNED);
       target.setRealReturnDate(realReturnDate);
       try {
           FileUtil.writeLoans(loans);
       } catch (IOException e) {
           target.setStatus(oldStatus);
           target.setRealReturnDate(oldDate);
           throw e;
       }
   }

    public void updateLoanStatus(int id, BookLoanStatus status) throws IOException {
        BookLoan target = null;
        if (status == null) {
            throw new IllegalArgumentException("Invalid status input");
        }
        for (BookLoan loan : loans) {
            if (loan.getId() == id) {
                target = loan;
                break;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException("No book loan with id " + id);
        }
        BookLoanStatus oldStatus = target.getStatus();
        target.setStatus(status);
        try {
            FileUtil.writeLoans(loans);
        } catch (IOException e) {
            target.setStatus(oldStatus);
            throw e;
        }

    }

    public List<BookLoan> getLoansByEmployeeId(int employeeId) {
        return loans.stream().filter(loan -> loan.getEmployeeId() == employeeId)
                .toList();
    }

    public BookLoan getActiveLoanByBookId(int bookId) {
        return loans.stream().filter(loan -> loan.getBookId() == bookId && loan.getStatus() == BookLoanStatus.ACTIVE)
                .findFirst()
                .orElse(null);
    }
}
