package repository;

import models.Employee;
import utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EmployeeRepository {
    private final List<Employee> employees;

    public EmployeeRepository(List<Employee> employees) {
        this.employees = employees == null ? new ArrayList<>() : new ArrayList<>(employees);
    }

    public Employee getEmployeeById(int id) {
        return employees.stream().filter((employee -> employee.getId() == id))
                .findFirst()
                .orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public void addNewEmployee(Employee employee) throws IOException {
        try {
            employees.add(employee);
            FileUtil.writeEmployees(employees);
        } catch (IOException e) {
            employees.remove(employee);
            throw e;
        }
    }

    public void updateBooks(int id, List<Integer> current, List<Integer> prev) throws IOException {
        Employee target = getEmployeeById(id);
        if (target == null) {
            throw new NoSuchElementException("No employee with id " + id);
        }
        List<Integer> oldCurrent = new ArrayList<>(target.getCurrentBooksId());
        List<Integer> oldPrev = new ArrayList<>(target.getPrevBooksId());
        target.setCurrentBooksId(new ArrayList<>(current));
        target.setPrevBooksId(new ArrayList<>(prev));
        try {
            FileUtil.writeEmployees(employees);
        } catch (IOException e) {
            target.setCurrentBooksId(oldCurrent);
            target.setPrevBooksId(oldPrev);
            throw e;
        }
    }

    public Employee findEmployeeByEmail(String email) {
        if (email == null) return null;
        for (Employee employee : employees) {
            if (email.equalsIgnoreCase(employee.getEmail()))
                return employee;
        }
        return null;
    }
}
