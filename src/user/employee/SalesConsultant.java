package user.employee;

import consts.*;

public class SalesConsultant extends Employee {
    public SalesConsultant(String email, String password) {
        super(email, password,  EmployeeType.SalesConsultant);
    }


    public SalesConsultant(String email, String password, double hourlySalary) {
        super(email, password,  EmployeeType.SalesConsultant, hourlySalary);
    }
}