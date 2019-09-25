package se;

import java.util.*;
import java.time.*;
import static java.util.AbstractMap.SimpleEntry;

import consts.*;
import finance.*;
import exception.*;
import user.customer.*;
import user.employee.*;
import user.*;
import property.*;

public class Branch {
    private String name;
    private HashMap<String, RentalProperty> rentalProps;
    private HashMap<String, ForSaleProperty> forSaleProps;
    private HashMap<String, Customer> customers;
    private HashMap<String, Employee> employees;
    private HashMap<LocalDateTime, HashMap<String, PayrollItem>> payroll;
	public double branchMoney;

    public double getBranchMoney() {
		return branchMoney;
	}

	public void setBranchMoney(double branchMoney) {
		this.branchMoney = branchMoney;
	}

	public Branch(String name) {
        this.name = name;

        this.rentalProps = new HashMap<String, RentalProperty>();
        this.forSaleProps = new HashMap<String, ForSaleProperty>();
        this.customers = new HashMap<String, Customer>();
        this.employees = new HashMap<String, Employee>();
    }

    public void submitHours(Employee e, int nHour) {
        PartTimeBaseSalary pbs = new PartTimeBaseSalary(nHour, e);
        HashMap<String, PayrollItem> monthlyPayroll = payroll.get(pbs.getDate());
        monthlyPayroll.put(pbs.getId(), pbs);
    }

    public void addEmployee(Employee e) {
        this.employees.put(e.getId(), e);
    }

    public List<SimpleEntry<RentalProperty, Application>> getApplications(Tenant t) {
        ArrayList<SimpleEntry<RentalProperty, Application>> res
                = new ArrayList<SimpleEntry<RentalProperty, Application>>();
        for (RentalProperty p : this.rentalProps.values())
            for (Application a : p.getApplicationsInitiatedBy(t))
                res.add(new SimpleEntry<RentalProperty, Application>(p, a));
        return res;
    }

    public List<RentalProperty> getOwnedRentalProperty(Landlord c) {
        ArrayList<RentalProperty> res = new ArrayList<RentalProperty>();
        for (Property p : getProperties(null, c))
            res.add((RentalProperty)p);
        return res;
    }

    public List<RentalProperty> getManagedRentalProperty(PropertyManager e) {
        ArrayList<RentalProperty> res = new ArrayList<RentalProperty>();
        for (Property p : getProperties(null, e))
            res.add((RentalProperty)p);
        return res;
    }

    public List<ForSaleProperty> getManagedForSaleProperty(SalesConsultant e) {
        ArrayList<ForSaleProperty> res = new ArrayList<ForSaleProperty>();
        for (Property p : getProperties(null, e))
            res.add((ForSaleProperty)p);
        return res;
    }

    public List<ForSaleProperty> getOwnedForSaleProperty(Vendor c) {
        ArrayList<ForSaleProperty> res = new ArrayList<ForSaleProperty>();
        for (Property p : getProperties(null, c))
            res.add((ForSaleProperty)p);
        return res;
    }

    public List<SalesConsultant> getAllSaleConsultants() {
        ArrayList<SalesConsultant> res = new ArrayList<SalesConsultant>();
        for (Employee e : this.employees.values())
            if (e instanceof SalesConsultant)
                res.add((SalesConsultant)e);
        return res;
    }

    public List<PropertyManager> getAllPropertyManagers() {
        ArrayList<PropertyManager> res = new ArrayList<PropertyManager>();
        for (Employee e : this.employees.values())
            if (e instanceof PropertyManager)
                res.add((PropertyManager)e);
        return res;
    }

    public Property getPropertyById(String pid) throws InvalidParamException {
        Property p;
        p = this.rentalProps.get(pid);
        if (p == null)
            p = this.forSaleProps.get(pid);
        if (p == null)
            throw new InvalidParamException("No property exists with the specified id.");
        return p;
    }

    public List<Property> getNewlyAddedProperties() {
        ArrayList<Property> res = new ArrayList<Property>();
        for (RentalProperty p : rentalProps.values())
            if (p.getStatus() == PropertyStatus.NotListed)
                res.add(p);
        for (ForSaleProperty p : forSaleProps.values())
            if (p.getStatus() == PropertyStatus.NotListed)
                res.add(p);
        return res;
    }

    /*
    determine customer or employee
    iterate through all customers/employees, call authenticate
    */
    public User login(String id, String passwd) {
        User u = null;
        if (id.startsWith("e")) {
            if (this.employees.containsKey(id)) {
                u = this.employees.get(id);
                if (!u.authenticate(passwd))
                    u = null;
            }
        }
        else if (id.startsWith("c")) {
            if (this.customers.containsKey(id)) {
                u = this.customers.get(id);
                if (!u.authenticate(passwd))
                    u = null;
            }
        }
        return u;
    }

    /*
    :param role: one of (landlord, vendor, renter, buyer)
    need to check (email, role) is unique
    create customer, add to `customers`, return userID starting with "c"
    */
    public String register(String email, String password, String role)
                            throws CustomerExistException, InvalidParamException {
        for (Customer c : this.customers.values()) {
            if (c.getEmail().equals(email) && c.getRole().equals(role))
                throw new CustomerExistException();
        }
        Customer newbie;
        if (role.equals("Vendor"))
            newbie = new Vendor(email, password);
        else if (role.equals("Landlord"))
            newbie = new Landlord(email, password);
        else if (role.equals("Buyer"))
            newbie = new Buyer(email, password);
        else if (role.equals("Tenant"))
            newbie = new Tenant(email, password);
        else
            throw new InvalidParamException(
                "Customer role must be either Vendor, Landlord, Buyer or Tenant."
            );
        this.customers.put(newbie.getId(), newbie);
        return newbie.getId();
    }

    public void addProperty(Property p) {        boolean rental;
        if (p instanceof RentalProperty) {
            rental = true;
            this.rentalProps.put(p.getId(), (RentalProperty)p);
            List<RentalProperty> ps = getOwnedRentalProperty(
                ((RentalProperty)p).getLandlord()
            );
            if (ps.size() >= 2)
                for (RentalProperty rp : ps) {
                    rp.setMinManagementFeeRate(0.6);
                    rp.setMaxManagementFeeRate(0.7);
                }
        } else {
            rental = false;
            this.forSaleProps.put(p.getId(), (ForSaleProperty)p);
        }
    }

    public List<RentalProperty> browseRentalProperties(String address, String suburb,
                                                    HashMap<String, Integer> capacity,
                                                    PropertyType type,
                                                    boolean onMarketOnly) {
        List<RentalProperty> res = new ArrayList<RentalProperty>();
        for (Property p : getProperties(address, suburb, capacity, null, type, false)){
            if (onMarketOnly && (p.getStatus() == PropertyStatus.NotListed
                                 || p.getStatus() == PropertyStatus.Secured))
                continue;
            res.add((RentalProperty)p);
        }
        return res;
    }

    public List<ForSaleProperty> browseForSaleProperties(String address, String suburb,
                                                        HashMap<String, Integer> capacity,
                                                        PropertyType type,
                                                        boolean onMarketOnly) {
        List<ForSaleProperty> res = new ArrayList<ForSaleProperty>();
        for (Property p : getProperties(address, suburb, capacity,null, type, true)){
            if (onMarketOnly && (p.getStatus() == PropertyStatus.NotListed
                                 || p.getStatus() == PropertyStatus.Secured))
                continue;
            res.add((ForSaleProperty)p);
        }
        return res;
    }

    /*
    :param capacity: key: bedroom, bathroom, carSpace
    */
    public List<Property> getProperties(String address, String suburb,
                                            HashMap<String, Integer> capacity,
                                            PropertyStatus status,
                                            PropertyType type,
                                            boolean forSale) {
        if (capacity != null)
            for (String key : capacity.keySet())
                if (capacity.get(key) == null)
                    capacity.remove(key);
        ArrayList<Property> ret = new ArrayList<Property>();

        if (forSale) {
            for (ForSaleProperty sp : this.forSaleProps.values())
                if (sp.match(address, suburb, capacity, status, type))
                    ret.add(sp);
        } else {
            for (RentalProperty rp : this.rentalProps.values())
                if (rp.match(address, suburb, capacity, status, type))
                    ret.add(rp);
        }
        return ret;
    }

    public List<Property> getProperties(PropertyStatus status, Customer owner) {
        ArrayList<Property> ret = new ArrayList<Property>();
        if (owner instanceof Vendor) {
            for (Property sp : this.forSaleProps.values())
                if (sp.match(status, owner))
                    ret.add(sp);
        } else if (owner instanceof Landlord) {
            for (Property rp : this.rentalProps.values())
                if (rp.match(status, owner))
                    ret.add(rp);
        }
        return ret;
    }

    public List<Property> getProperties(PropertyStatus st, Employee e) {
        ArrayList<Property> ret = new ArrayList<Property>();
        if (e.getRole() == EmployeeType.SalesConsultant) {
            for (Property sp : this.forSaleProps.values())
                if (sp.match(st, e))
                    ret.add(sp);
        } else if (e.getRole() == EmployeeType.PropertyManager) {
            for (Property rp : this.rentalProps.values())
                if (rp.match(st, e))
                    ret.add(rp);
        }
        return ret;
    }
}