package user.customer;

import java.util.*;

import se.*;
import exception.*;
import property.*;

public class Tenant extends NonOwner {

    public Tenant(String email, String password) {
        super(email, password);
    }

    public String getRole() {
        return "Tenant";
    }



}