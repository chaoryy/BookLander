package models;

import models.enums.Role;

public class EmployeeRole {
    private final int id;
    private Role role;

    public EmployeeRole(int id, Role role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
