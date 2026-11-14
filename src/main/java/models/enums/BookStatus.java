package models.enums;

public enum BookStatus {
    AVAILABLE("Available"),
    TAKEN("Taken");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
