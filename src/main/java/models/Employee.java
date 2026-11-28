package models;

import models.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {
    private final int id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private List<Integer> currentBooksId;
    private List<Integer> prevBooksId;
    private Role role;

    public Employee(int id, String name, String lastName,
                    String email, String password,
                    List<Integer> currentBooksId, List<Integer> prevBooksId, Role role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.currentBooksId = currentBooksId == null ? new ArrayList<>() : new ArrayList<>(currentBooksId);
        this.prevBooksId = prevBooksId == null ? new ArrayList<>() : new ArrayList<>(prevBooksId);
        this.role = role;
    }

    public Employee(int id, String name, String lastName, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.currentBooksId = new ArrayList<>();
        this.prevBooksId = new ArrayList<>();
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Integer> getCurrentBooksId() {
        return currentBooksId;
    }

    public void setCurrentBooksId(List<Integer> currentBooksId) {
        this.currentBooksId = Objects.requireNonNullElseGet(currentBooksId, ArrayList::new);
    }

    public List<Integer> getPrevBooksId() {
        return prevBooksId;
    }

    public void setPrevBooksId(List<Integer> prevBooksId) {
        this.prevBooksId = Objects.requireNonNullElseGet(prevBooksId, ArrayList::new);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", currentBooksId=" + currentBooksId +
                ", prevBooksId=" + prevBooksId +
                ", role=" + role +
                '}';
    }
}
