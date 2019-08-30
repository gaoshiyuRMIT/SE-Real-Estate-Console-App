package user.customer;

import java.util.*;

public abstract class Owner extends Customer {
    public Owner(String email, String password) {
        super(email, password);
    }
}