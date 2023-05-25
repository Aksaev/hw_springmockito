package me.aksaev.hw_springmockito.controller;

import org.springframework.web.bind.annotation.*;

import me.aksaev.hw_springmockito.model.Employee;
import me.aksaev.hw_springmockito.service.DepartmentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(value = "/{id}/employees")
    public List<Employee> employeesFromDepertment(@PathVariable int id) {
        return departmentService.employeesFromDepartment(id);
    }

    @GetMapping("/{id}/salary/sum")
    public int sumSalaryFromDepertment(@PathVariable int id) {
        return departmentService.sumSalaryFromDepartment(id);
    }

    @GetMapping("/{id}/salary/max")
    public int maxSalaryFromDepertment(@PathVariable int id) {
        return departmentService.maxSalaryFromDepartment(id);
    }

    @GetMapping("/{id}/salary/min")
    public int minSalaryFromDepertment(@PathVariable int id) {
        return departmentService.minSalaryFromDepartment(id);
    }

    @GetMapping("/employees")
    public Map<Integer, List<Employee>> employeesGroupedByDepartment() {
        return departmentService.employeesGroupedByDepartment();
    }
}
