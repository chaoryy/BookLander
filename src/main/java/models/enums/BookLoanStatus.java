package models.enums;

public enum BookLoanStatus {
    ACTIVE("Active"),
    RETURNED ("Returned"),
    OVERDUE ("Overdue");

    private final String status;

    BookLoanStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
