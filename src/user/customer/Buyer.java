package user.customer;

import java.util.*;

import se.*;

import property.*;
import exception.*;

public class Buyer extends Customer {
    private ArrayList<PurchaseOffer> purchaseOffers;
    private ArrayList<ApplicantDetail> applicants;

    public Buyer(String email, String password) {
        super(email, password);
        purchaseOffers = new ArrayList<PurchaseOffer>();
    }

    public String getRole() {
        return "Buyer";
    }

}