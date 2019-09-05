package user.customer;

import java.util.*;

import property.*;
import SnE.*;
import consts.*;

public class Vendor extends Customer {
    public Vendor(String email, String passwd) {
        super(email, passwd);
    }

    public String getRole() {
        return "Vendor";
    }

}