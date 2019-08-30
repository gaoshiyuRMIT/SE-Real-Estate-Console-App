import java.util.*;

import const.*;

public class Branch {
    private String name;
    private User currUser;
    private ArrayList<RentalProperty> rentalProps;
    private ArrayList<ForSaleProperty> forSaleProps;
    private HashMap<String, Customer> customers;
    private HashMap<String, Employee> employees;

    public Branch(String name) {
        this.name = name;

        this.rentalProps = new ArrayList<RentalProperty>();
        this.forSaleProps = new ArrayList<ForSaleProperty>();
        this.customers = new HashMap<String, Customer>();
        this.employees = new HashMap<String, Employee>();
    }

    public void addEmployee(Employee e) {
        this.employees.put(e.getId(), e);
    }

    /*
    determine customer or employee
    iterate through all customers/employees, call authenticate
    */
    public boolean login(String id, String passwd) {
    }

    public void logout() {
    }

    /*
    :param role: one of (landlord, vendor, renter, buyer)
    need to check (email, role) is unique
    create customer, add to `customers`, return userID starting with "c"
    */
    public String register(String email, String password, String role) throws CustomerExistException {
    }

    /*
    :param capacity: key: bedroom, bathroom, carSpace
    */
    public ArrayList<Property> getProperties(String address, String suburb,
                                            HashMap<String, Integer> capacity,
                                            PropertyStatus status,
                                            PropertyType type,
                                            boolean forSale) {
        for (String key : capacity.keySet())
            if (capacity.get(key) == null)
                capacity.remove(key);
        ArrayList<Property> ret = new ArrayList<Property>();
        ArrayList<Property> properties = forSale ? this.forSaleProps : this.rentalProps;
        for (Property p : properties)
            if (p.match(address, suburb, capacity, status, type))
                ret.add(p);
        return ret;
    }
}