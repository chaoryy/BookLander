package models.enums;

public enum Role {
    ADMIN ("Admin"),
    USER ("User");

    private final String roleName;

    Role(String role) {
        this.roleName = role;
    }

    public String getRoleName() {
        return roleName;
    }
}
