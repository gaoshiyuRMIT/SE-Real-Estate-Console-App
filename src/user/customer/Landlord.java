package user.customer;


import java.util.*;

import consts.*;
import se.*;
import property.*;

public class Landlord extends Customer {
    public Landlord(String email, String password) {
        super(email, password);
    }

    public String getRole() {
        return "Landlord";
    }

    public void acceptApplication(Application a) {
        a.setAccepted();
    }


}