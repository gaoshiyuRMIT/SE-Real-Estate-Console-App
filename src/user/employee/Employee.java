package user.employee;

import user.*;
import consts.*;

public class Employee extends User {
    private static int idCounter = 0;
    private static final double maxMonthlyHours = 40;

    private EmployeeType role;
    private double hourlySalary;
    private int hours;

    public Employee(String email, String password, EmployeeType role, int hours) {
        super(email, password, genId());
        this.role = role;
        this.hours = hours;
    }

    public static String genId() {
        return String.format("e%08d", Employee.idCounter++);
    }

    public EmployeeType getRole() {
        return role;
    }

	public int getHours() {
		return hours;
	}
}