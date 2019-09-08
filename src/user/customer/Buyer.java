package user.customer;

import java.util.*;

import se.*;

import property.*;
import exception.*;

public class Buyer extends NonOwner {
    private HashMap<ID, ApplicantDetail> applicants;

    public Buyer(String email, String password) {
        super(email, password);
    }

    public String getRole() {
        return "Buyer";
    }
}