package user.employee;

import consts.*;

public class SalesConsultant extends Employee {
    public SalesConsultant(String email, String password , int hours) {
        super(email, password,  EmployeeType.SalesConsultant, hours);
    }
}