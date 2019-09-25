package user.employee;

import user.*;
import consts.*;

public class Employee extends User {
    private static int maxMonthlyHours = 40;
    private static int idCounter = 0;
    private boolean partTime;
    private EmployeeType role;
    private double hourlySalary;

    public Employee(String email, String password, EmployeeType role) {
        super(email, password, genId());
        this.role = role;
        this.partTime = false;
    }

    public Employee(String email, String password, EmployeeType role, double hourlySalary) {
        this(email, password, role);
        this.hourlySalary = hourlySalary;
        this.partTime = true;
    }

    public static int getMaxMonthlyHours() {
        return maxMonthlyHours;
    }

    public double getHourlySalary() {
        return hourlySalary;
    }

    public void setHourlySalary(double s) {
        hourlySalary = s;
    }

    public static String genId() {
        return String.format("e%08d", Employee.idCounter++);
    }

    public EmployeeType getRole() {
        return role;
    }
}