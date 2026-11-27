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

    public static Role getRoleByName(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) {
            throw new IllegalArgumentException("Invalid employee role can not be empty");
        }
        String cleanStr = roleStr.trim();
        for (Role role : Role.values()) {
            if (role.getRoleName().equalsIgnoreCase(cleanStr) ) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown employee role: " + roleStr.trim());
    }
}
