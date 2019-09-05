package user.employee;

import user.*;
import consts.*;

public abstract class Employee extends User {
    private static int idCounter = 0;
    private static final double maxMonthlyHours = 40;

    private EmployeeType role;
    private double hourlySalary;

    public Employee(String email, String password, EmployeeType role) {
        super(email, password, genId());
        this.role = role;
    }

    public static String genId() {
        return String.format("e%08d", Employee.idCounter++);
    }

    public EmployeeType getRole() {
        return role;
    }
}