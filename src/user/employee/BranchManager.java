package user.employee;

import consts.*;

public class BranchManager extends Employee {
    public BranchManager(String email, String password) {
        super(email, password, EmployeeType.BranchManager);
    }


    public BranchManager(String email, String password, double hourlySalary) {
        super(email, password, EmployeeType.BranchManager, hourlySalary);
    }
}