package models;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private final int id;
    private String name;
    private String lastName;
    private List<Integer> currentBooksId = new ArrayList<>();
    private List<Integer> prevBooksId = new ArrayList<>();

    public Employee(int id, String name, String lastName, List<Integer> currentBooksId, List<Integer> prevBooksId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.currentBooksId = currentBooksId;
        this.prevBooksId = prevBooksId;
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
        this.currentBooksId = currentBooksId;
    }

    public List<Integer> getPrevBooksId() {
        return prevBooksId;
    }

    public void setPrevBooksId(List<Integer> prevBooksId) {
        this.prevBooksId = prevBooksId;
    }
}
