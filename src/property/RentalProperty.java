package property;

import java.util.*;

import SnE.*;

public class RentalProperty extends Property {
    private double weeklyRental;

    // number of months
    private int contractDuration;

    private ArrayList<Application> applications;
    private ArrayList<Lease> leases;
    private double managementFee;
}