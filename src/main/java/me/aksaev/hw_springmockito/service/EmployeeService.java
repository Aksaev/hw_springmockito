package me.aksaev.hw_springmockito.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import me.aksaev.hw_springmockito.exception.EmployeeAlreadyAddedException;
import me.aksaev.hw_springmockito.exception.EmployeeNotFoundException;
import me.aksaev.hw_springmockito.exception.EmployeeStorageIsFullException;
import me.aksaev.hw_springmockito.model.Employee;

@Service
public class EmployeeService {
    private static final int LIMIT = 10;
    private final List<Employee> employees = new ArrayList<>();
    private final ValidatorService validatorService;

    public EmployeeService(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    // Добавление сотрудника
    public Employee add(String name, String surname, int department, int salary) {
        Employee employee = new Employee(
                validatorService.validateName(name),
                validatorService.validateSurname(surname),
                department,
                salary);

        if (employees.contains(employee)) {
            throw new EmployeeAlreadyAddedException();
        }
        if (employees.size() < LIMIT) {
            employees.add(employee);
            return employee;
        }
        throw new EmployeeStorageIsFullException();
    }

    // Удаление сотрудника
    public Employee remove(String name, String surname) {
        Employee employee = employees.stream()
                .filter(emp -> emp.getName().equals(name) && emp.getSurname().equals(surname))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
        employees.remove(employee);
        return employee;
    }

    public Employee find(String name, String surname) {
        return employees.stream()
                .filter(employee -> employee.getName().equals(name) && employee.getSurname().equals(surname))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> getAll() {
        return new ArrayList<>(employees);
    }

}
