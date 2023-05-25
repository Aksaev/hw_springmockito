package me.aksaev.hw_springmockito.service;

import me.aksaev.hw_springmockito.exception.DepartmetNotFoundException;
import me.aksaev.hw_springmockito.model.Employee;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final EmployeeService employeeService;

    public DepartmentService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public int maxSalaryFromDepartment(int departmentId) {
        return employeeService.getAll().stream()
                .filter(employee -> employee.getDepartment() == departmentId)
                .mapToInt(Employee::getSalary)
                .max()
                .orElseThrow(DepartmetNotFoundException::new);
    }

    public int minSalaryFromDepartment(int departmentId) {
        return employeeService.getAll().stream()
                .filter(employee -> employee.getDepartment() == departmentId)
                .mapToInt(Employee::getSalary)
                .min()
                .orElseThrow(DepartmetNotFoundException::new);
    }

    public int sumSalaryFromDepartment(int departmentId) {
        return employeeService.getAll().stream()
                .filter(employee -> employee.getDepartment() == departmentId)
                .mapToInt(Employee::getSalary)
                .sum();
    }

    public List<Employee> employeesFromDepartment(int departmentId) {
        return employeeService.getAll().stream()
                .filter(employee -> employee.getDepartment() == departmentId)
                .collect(Collectors.toList());
    }

    public Map<Integer, List<Employee>> employeesGroupedByDepartment() {
        return employeeService.getAll().stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

}

