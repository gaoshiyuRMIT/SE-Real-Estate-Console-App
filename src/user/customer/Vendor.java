package user.customer;

import java.util.*;

import property.*;
import SnE.*;
import consts.*;

public class Vendor extends Owner {
    public Vendor(String email, String passwd) {
        super(email, passwd);
    }

    public ArrayList<ForSaleProperty> getProperties(Branch branch) {
        ArrayList<ForSaleProperty> ret = new ArrayList<ForSaleProperty>();
        for (Property p : branch.getProperties(null, this, true)) {
            ret.add((ForSaleProperty)p);
        }
        return ret;
    }

    public void addProperty(Branch branch, String address, String suburb,
                            HashMap<String, Integer> capacity, PropertyType type) {
        ForSaleProperty p = new ForSaleProperty(address, suburb, capacity, type, this);
        branch.addProperty(p);
    }
}