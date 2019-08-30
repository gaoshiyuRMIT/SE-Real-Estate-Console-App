package property;

import java.util.*;

import SnE.*;
import consts.*;
import user.*;

public abstract class Property {
    private String address;
    private String suburb;
    private HashMap<String, Integer> capacity;
    private PropertyType type;
    private ArrayList<Inspection> inspections;
    private HashMap<String, String> documents;
    private Owner owner;
    private EmployeeAssigned employee;
    private PropertyStatus status;

    public Property(String address, String suburb, HashMap<String, Integer> capacity,
                    PropertyType type, Owner owner, EmployeeAssigned employee) {
        this.status = PropertyStatus.NotListed;
        this.suburb = suburb.toUpperCase();
        this.type = type;
        this.owner = owner;
        this.employee = employee;
        this.capacity = capacity;
        this.address = address;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public boolean match(String address, String suburb,
                            HashMap<String, Integer> capacity,
                            PropertyStatus status,
                            PropertyType type) {
        if (address != null && !this.address.contains(address))
            return false;
        if (suburb != null && !suburb.toUpperCase().equals(this.suburb))
            return false;
        for (String k : capacity.keySet())
            if (capacity.get(k) != null && this.capacity.get(k) != capacity.get(k))
                return false;
        if (status != null && status != this.status)
            return false;
        if (type != null && type != this.type)
            return false;
        return true;
    }
}