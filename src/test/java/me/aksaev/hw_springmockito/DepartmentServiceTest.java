package me.aksaev.hw_springmockito;

import me.aksaev.hw_springmockito.exception.DepartmetNotFoundException;
import me.aksaev.hw_springmockito.model.Employee;
import me.aksaev.hw_springmockito.service.DepartmentService;
import me.aksaev.hw_springmockito.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DepartmentService departmentService;

    public static Stream<Arguments> maxSalaryFromDepartmentParams() {
        return Stream.of(
                Arguments.of(1, 15_000),
                Arguments.of(2, 17_000),
                Arguments.of(3, 20_000)
        );
    }

    public static Stream<Arguments> minSalaryFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(1, 10_000),
                Arguments.of(2, 15_000),
                Arguments.of(3, 20_000)
        );
    }

    public static Stream<Arguments> sumSalaryFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(1, 25_000),
                Arguments.of(2, 32_000),
                Arguments.of(3, 20_000),
                Arguments.of(4, 0)
        );
    }

    public static Stream<Arguments> employeesFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(
                        1,
                        List.of(
                                new Employee("Иван", "Иванов", 1, 10_000),
                                new Employee("Петр", "Петров", 1, 15_000)
                        )
                ),
                Arguments.of(
                        2,
                        List.of(
                                new Employee("Мария", "Козлова", 2, 15_000),
                                new Employee("Анна", "Сергеева", 2, 17_000)
                        )
                ),
                Arguments.of(
                        3,
                        Collections.singletonList(new Employee("Данил", "Данилов", 3, 20_000))
                ),
                Arguments.of(
                        4,
                        Collections.emptyList()
                )
        );
    }

    @BeforeEach
    public void beforeEach() {
        Mockito.when(employeeService.getAll()).thenReturn(
                List.of(
                        new Employee("Иван", "Иванов", 1, 10_000),
                        new Employee("Мария", "Козлова", 2, 15_000),
                        new Employee("Петр", "Петров", 1, 15_000),
                        new Employee("Анна", "Сергеева", 2, 17_000),
                        new Employee("Данил", "Данилов", 3, 20_000)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("maxSalaryFromDepartmentParams")
    public void maxSalaryFromDepartmentTest(int departmentId, int expected) {
        Assertions.assertThat(departmentService.maxSalaryFromDepartment(departmentId))
                .isEqualTo(expected);
    }

    @Test
    public void maxSalaryFromDepartmentWhenNotFoundTest() {
        Assertions.assertThatExceptionOfType(DepartmetNotFoundException.class)
                .isThrownBy(() -> departmentService.maxSalaryFromDepartment(4));
    }

    @ParameterizedTest
    @MethodSource("minSalaryFromDepartmentTestParams")
    public void minSalaryFromDepartmentTest(int departmentId, int expected) {
        Assertions.assertThat(departmentService.minSalaryFromDepartment(departmentId))
                .isEqualTo(expected);
    }

    @Test
    public void minSalaryFromDepartmentWhenNotFoundTest() {
        Assertions.assertThatExceptionOfType(DepartmetNotFoundException.class)
                .isThrownBy(() -> departmentService.minSalaryFromDepartment(4));
    }


    @ParameterizedTest
    @MethodSource("sumSalaryFromDepartmentTestParams")
    public void sumSalaryFromDepartmentTest(int departmentId, int expected) {
        Assertions.assertThat(departmentService.sumSalaryFromDepartment(departmentId))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("employeesFromDepartmentTestParams")
    public void employeesFromDepartmentTest(int departmentId, List<Employee> expected) {
        Assertions.assertThat(departmentService.employeesFromDepartment(departmentId))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void employeesGroupedByDepartmentTest() {
        Map<Integer, List<Employee>> expected = Map.of(
                1,
                List.of(
                        new Employee("Иван", "Иванов", 1, 10_000),
                        new Employee("Петр", "Петров", 1, 15_000)
                ),
                2,
                List.of(
                        new Employee("Мария", "Козлова", 2, 15_000),
                        new Employee("Анна", "Сергеева", 2, 17_000)
                ),
                3,
                        Collections.singletonList(new Employee("Данил", "Данилов", 3, 25_000))
        );
        Assertions.assertThat(departmentService.employeesGroupedByDepartment())
                .containsExactlyInAnyOrderEntriesOf(expected);
    }

}

