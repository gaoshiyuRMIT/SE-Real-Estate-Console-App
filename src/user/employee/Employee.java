package user.employee;

import user.*;
import consts.*;

public class Employee extends User {
    private static int idCounter = 0;
    private static final double maxMonthlyHours = 40;

    private EmployeeType role;
    private double hourlySalary;
    private double salary;
    private boolean workerType;

    public Employee(String email, String password, EmployeeType role, boolean workerType) {
        super(email, password, genId());
        this.role = role;
        this.workerType = workerType;
    }
    
    public static String genId() {
        return String.format("e%08d", Employee.idCounter++);
    }

    public EmployeeType getRole() {
        return role;
    }
	
	public double getSalary() {
		return salary;
	}
	
	public boolean getWorkerType() {
		return workerType;
	}
	
	public void calculateSalary(int hours) {
		salary = hourlySalary*hours;
	}
}