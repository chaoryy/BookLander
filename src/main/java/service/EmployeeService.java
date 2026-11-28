package service;

import models.Employee;
import models.enums.Role;
import repository.EmployeeRepository;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s-]{2,50}$");

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public Employee getEmployeeById(int id) {
        return employeeRepository.getEmployeeById(id);
    }

    public void registerNewEmployee(String name, String lastName, String email,
                                    String password, Role role) throws IllegalArgumentException, IOException {
        validate(name, lastName, email, password);
        String cleanEmail = email.trim();
        if (isEmailTaken(cleanEmail)) {
            throw new IllegalArgumentException("Employee with this email already exists");
        }
        int id = employeeRepository.getAllEmployees().stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
        employeeRepository.addNewEmployee(new Employee(id, name.trim(), lastName.trim(), cleanEmail, password.trim(), role));
    }

    private void validate(String name, String lastName,
                          String email, String password) {
        if (name == null || name.isBlank() || !NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new IllegalArgumentException("Name should contain at least 2 letters");
        }
        if (lastName == null || lastName.isBlank() || !NAME_PATTERN.matcher(lastName.trim()).matches()) {
            throw new IllegalArgumentException("Last name should contain at least 2 letters");
        }
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Incorrect format of email");
        }
        if (password == null || password.isBlank() || password.trim().length() < 4) {
            throw new IllegalArgumentException("Password should contain at least 4 characters");
        }
    }

    private boolean isEmailTaken(String email) {
        return employeeRepository.findEmployeeByEmail(email) != null;
    }

    public void updateBooks(int id, List<Integer> current, List<Integer> prev) throws IOException {
        employeeRepository.updateBooks(id, current, prev);
    }

    public Employee logIn(String email, String password) {
        if (email == null) {
            throw new IllegalArgumentException("Email can't be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password can't be null");
        }
        Employee foundEmployee = employeeRepository.findEmployeeByEmail(email);
        if (foundEmployee != null && password.equals(foundEmployee.getPassword())) {
            return foundEmployee;
        } else {
            throw new IllegalArgumentException("Couldn't log in. Invalid email or password");
        }
    }
}
