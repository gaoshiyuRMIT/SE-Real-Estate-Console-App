package property;

import java.util.*;

import SnE.*;
import consts.*;
import user.customer.*;
import exception.*;

public class ForSaleProperty extends Property {

    private double commissionRate;
    private double minimumPrice;
    private boolean sold;
    private ArrayList<PurchaseOffer> purchaseOffers;

    public ForSaleProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            PropertyType type, Vendor vendor) {
        super(address, suburb, capacity, type, vendor);
    }


    public void addPurchaseOffer(PurchaseOffer po) throws OperationNotAllowedException {
        if (this.getStatus() != PropertyStatus.ApplicationOpen)
            throw new OperationNotAllowedException();
        this.purchaseOffers.add(po);
    }
}