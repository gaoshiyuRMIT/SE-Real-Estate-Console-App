package user.employee;

import consts.*;

public class SalesConsultant extends Employee {
    public SalesConsultant(String email, String password) {
        super(email, password,  EmployeeType.SalesConsultant);
    }

}