package user.customer;


import java.util.*;

import consts.*;
import SnE.*;
import property.*;

public class Landlord extends Owner {
    public Landlord(String email, String password) {
        super(email, password);
    }

    public ArrayList<RentalProperty> getProperties(Branch branch) {
        ArrayList<RentalProperty> ret = new ArrayList<RentalProperty>();
        for (Property p : branch.getProperties(null, this, false)) {
            ret.add((RentalProperty)p);
        }
        return ret;
    }

    public void acceptApplication(Application a) {
        a.setAccepted();
    }

    public void addProperty(Branch branch, String address, String suburb,
                            HashMap<String, Integer> capacity, PropertyType type) {
        RentalProperty p = new RentalProperty(address, suburb, capacity, type, this);
        branch.addProperty(p);
    }
}