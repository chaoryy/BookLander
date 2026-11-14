package repository;

import models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public Employee getEmployeeById(int id) {
        return employees.stream().filter((employee -> employee.getId() == id))
                .findFirst()
                .orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public void registerNewEmployee(Employee employee) {
        employees.add(employee);
    }
}
