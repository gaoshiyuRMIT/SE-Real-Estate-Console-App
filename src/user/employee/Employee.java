package user.employee;

import user.*;

public abstract class Employee extends User {
    public Employee(String email, String password, String id) {
        super(email, password, id);
    }
}