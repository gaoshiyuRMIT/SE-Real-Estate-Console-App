package user.employee;

import user.*;
import consts.*;

public class Employee extends User {
    private static int idCounter = 0;
    private static final double maxMonthlyHours = 40;

    private EmployeeType role;
    private double hourlySalary;
    private int hours;
    private double salary;

    public Employee(String email, String password, EmployeeType role, int hours ,double salary) {
        super(email, password, genId());
        this.role = role;
        this.hours = hours;
        this.salary = salary;
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
	
	public double getSalary() {
		return salary;
	}

}