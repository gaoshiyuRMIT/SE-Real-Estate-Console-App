package property;

import java.util.*;

import SnE.*;
import consts.*;
import user.customer.*;
import exception.*;

public class RentalProperty extends Property {
    private double weeklyRental;

    // number of months
    private int contractDuration;

    private ArrayList<Application> applications;
    private ArrayList<Lease> leases;
    private double managementFee;

    public RentalProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            PropertyType type, Landlord landlord) {
        super(address, suburb, capacity, type, landlord);
    }

    public void addApplication(Application a) throws OperationNotAllowedException{
        if (this.getStatus() != PropertyStatus.ApplicationOpen)
            throw new OperationNotAllowedException();
        this.applications.add(a);
    }
}