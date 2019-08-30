package user.customer;

import java.util.*;

import SnE.*;

import property.*;
import exception.*;

public class Buyer extends Consumer {
    private ArrayList<PurchaseOffer> purchaseOffers;

    public Buyer(String email, String password) {
        super(email, password);
        purchaseOffers = new ArrayList<PurchaseOffer>();
    }

    public void initiatePurchaseOffer(ForSaleProperty property, double amount)
                                        throws OperationNotAllowedException {
        PurchaseOffer po = new PurchaseOffer(amount, this);
        property.addPurchaseOffer(po);
        this.purchaseOffers.add(po);
    }
}