package me.aksaev.hw_springmockito;

import me.aksaev.hw_springmockito.exception.*;
import me.aksaev.hw_springmockito.model.Employee;
import me.aksaev.hw_springmockito.service.EmployeeService;
import me.aksaev.hw_springmockito.service.ValidatorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class EmployeeServiceTest {
    private final EmployeeService employeeService = new EmployeeService(new ValidatorService());

    public static Stream<Arguments> addWithIncorrectNameTestParams() {
        return Stream.of(
                Arguments.of("Иван1"),
                Arguments.of("Иван!"),
                Arguments.of("Иван@")
        );
    }

    public static Stream<Arguments> addWithIncorrectSurnameTestParams() {
        return Stream.of(
                Arguments.of("Сидоров1"),
                Arguments.of("Сидоров!"),
                Arguments.of("Сидоров@")
        );
    }

    @BeforeEach
    public void beforeEach() {
        // Добавляем сотрудников
        employeeService.add("Сергей", "Сергеев", 1, 10_000);
        employeeService.add("Иван", "Иванов", 2, 20_000);
        employeeService.add("Петр", "Петров", 3, 30_000);
    }

    @AfterEach
    public void afterEach() {
        // Получаем список сотрудников
        employeeService.getAll()
                .forEach(employee -> employeeService.remove(employee.getName(), employee.getSurname()));
    }

    @Test
    public void addTest() {
        // Проверяем какое количество сотрудников было
        int beforeCount = employeeService.getAll().size();
        // Ожидаем, что сотрудник будет добавлен
        Employee expected = new Employee("Сидр", "Сидоров", 1, 10_000);
        // Добавляем сотрудника и сравниваем его с ожидаемым
        Assertions.assertThat(employeeService.add("Сидр", "Сидоров", 1, 10_000))
                .isEqualTo(expected)
                .isIn(employeeService.getAll());
        Assertions.assertThat(employeeService.getAll()).hasSize(beforeCount + 1);
        Assertions.assertThat(employeeService.find("Сидр", "Сидоров")).isEqualTo(expected);
    }

    // Негативный вариант для Имен
    @ParameterizedTest
    @MethodSource("addWithIncorrectNameTestParams")
    public void addWithIncorrectNameTest(String incorrectName) {

        Assertions.assertThatExceptionOfType(IncorrectNameException.class)
                .isThrownBy(() -> employeeService.add(incorrectName, "Сидоров", 1, 10_000));
    }

    // Негативный вариант для Фамилий
    @ParameterizedTest
    @MethodSource("addWithIncorrectSurnameTestParams")
    public void addWithIncorrectSurnameTest(String incorrectSurname) {
        Assertions.assertThatExceptionOfType(IncorrectSurnameException.class)
                .isThrownBy(() -> employeeService.add("Сидор", incorrectSurname, 1, 10_000));
    }

    // Негативный вариант, если такой сотрудник уже есть
    @Test
    public void addWhenAlreadyExistsTest() {
        Assertions.assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(() -> employeeService.add("Сергей", "Сергеев", 1, 10_000));
    }

    // Негативный вариант, когда сотрудников больше чем лимит
    @Test
    public void addWhenStorageIsFullTest() {
        Stream.iterate(1, i -> i + 1)
                .limit(7)
                .map(number -> new Employee(
                        "Сергей" + ((char)('a' + number)),
                        "Сергеев" + ((char)('a' + number)),
                        number,
                        10_000 + number)
                )
                .forEach(employee ->
                        employeeService.add(
                                employee.getName(),
                                employee.getSurname(),
                                employee.getDepartment(),
                                employee.getSalary()
                        )
                );

        Assertions.assertThatExceptionOfType(EmployeeStorageIsFullException.class)
                .isThrownBy(() -> employeeService.add("Владимир", "Владимиров", 1, 10_000));
    }

    // Позивный вариант, удаление сотрудника
    @Test
    public void removeTest() {
        int beforeCount = employeeService.getAll().size();
        Employee expected = new Employee("Сергей", "Сергеев", 1, 10_000);

        Assertions.assertThat(employeeService.remove("Сергей", "Сергеев"))
                .isEqualTo(expected)
                .isNotIn(employeeService.getAll());
        Assertions.assertThat(employeeService.getAll()).hasSize(beforeCount - 1);
        Assertions.assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("Сергей", "Сергеев"));
    }

    // Негативный вариант, удаление сотрудника
    @Test
    public void removeWhenNotTest() {
        Assertions.assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("Лев", "Львов"));
    }

    // Позивный вариант, поиск сотрудника
    @Test
    public void findTest() {
        int beforeCount = employeeService.getAll().size();
        Employee expected = new Employee("Сергей", "Сергеев", 1, 10_000);

        Assertions.assertThat(employeeService.find("Сергей", "Сергеев"))
                .isEqualTo(expected)
                .isIn(employeeService.getAll());
        Assertions.assertThat(employeeService.getAll()).hasSize(beforeCount);
    }

    // Негативный вариант, поиск сотрудника
    @Test
    public void findWhenNotTest() {
        Assertions.assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("Лев", "Львов"));
    }

    // Позивный вариант, добавление всех сотрудников
    @Test
    public void getAllTest() {
        Assertions.assertThat(employeeService.getAll())
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        new Employee("Сергей", "Сергеев", 1, 10_000),
                        new Employee("Иван", "Иванов", 2, 20_000),
                        new Employee("Петр", "Петров", 3, 30_000)
                );
    }


}
