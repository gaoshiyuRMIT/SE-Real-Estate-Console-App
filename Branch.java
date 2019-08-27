import java.util.*;

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

}