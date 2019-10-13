package user.employee;

import consts.*;

public class PropertyManager extends Employee {
    public PropertyManager(String email, String password) {
        super(email, password,  EmployeeType.PropertyManager);
    }
}