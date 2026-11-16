package service;

import models.Employee;
import models.enums.Role;
import repository.EmployeeRepository;

import java.util.List;

public class EmployeeService {
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

    public void registerNewEmployee(String name, String lastName, Role role) {
        int id = employeeRepository.getAllEmployees().stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
        employeeRepository.addNewEmployee(new Employee(id, name, lastName, role));
    }


}
