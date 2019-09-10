package user.employee;

import consts.*;

public class BranchManager extends Employee {
    public BranchManager(String email, String password,int hours) {
        super(email, password, EmployeeType.BranchManager,hours);
    }
}