package models.enums;

public enum BookLoanStatus {
    AVAILABLE ("Available"),
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
