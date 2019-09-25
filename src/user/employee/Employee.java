package user.employee;

import user.*;

import java.util.ArrayList;
import java.util.List;

import consts.*;
import hourSubmission.HourSubmission;

public class Employee extends User {
    private static int maxMonthlyHours = 40;
    private static int idCounter = 0;
    private boolean partTime;
    private EmployeeType role;
    private double hourlySalary;
    private double salary;
    private boolean isParttimer;//default full timer
    private List<HourSubmission> hoursSubmitted;

    public List<HourSubmission> getHoursSubmitted() {
		return hoursSubmitted;
	}

	public void setHoursSubmitted(List<HourSubmission> hoursSubmitted) {
		this.hoursSubmitted = hoursSubmitted;
	}

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

	public double getBaseSalary() {
		return salary;
	}

	public void setIsParttimer(boolean isParttimer) {
		this.isParttimer = isParttimer;
	}


	public boolean getIsParttimer() {
		return isParttimer;
	}

	public void setHourlySalary(double hourlySalary) {
		this.hourlySalary = hourlySalary;
	}

	public double getHourlySalary() {
		return hourlySalary;
	}

	public void calculateBaseSalary(int hours) {
		salary = hourlySalary*hours;
	}

	public int addHourSubmission(int hours) {
		return hours;
	}

	//public List<HourSubmission> getHours() {
	// hours;
	//}

}
