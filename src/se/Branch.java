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
import util.*;

public class Branch {
    private String name;
    private HashMap<String, RentalProperty> rentalProps;
    private HashMap<String, ForSaleProperty> forSaleProps;
    private HashMap<String, Customer> customers;
    private HashMap<String, Employee> employees;
    private HashMap<LocalDateTime, HashMap<String, PayrollItem>> payroll;
    private HashMap<Employee, Double> preparedEmployeePayout;
    private HashMap<Landlord, Double> preparedLandlordPayout;

    private Account account;

    public Branch(String name) {
        this.name = name;

        this.rentalProps = new HashMap<String, RentalProperty>();
        this.forSaleProps = new HashMap<String, ForSaleProperty>();
        this.customers = new HashMap<String, Customer>();
        this.employees = new HashMap<String, Employee>();
        this.account = new Account(10000);
        this.payroll = new HashMap<LocalDateTime, HashMap<String, PayrollItem>>();
        this.payroll.put(LocalDateTimeUtil.extractMonth(LocalDateTime.now()),
                        new HashMap<String, PayrollItem>());
        preparedEmployeePayout = new HashMap<Employee, Double>();
        preparedLandlordPayout = new HashMap<Landlord, Double>();
    }

    public Account getAccount() {
        return account;
    }


    public HashMap<Employee, Double> getPreparedEmployeePayout() {
        return preparedEmployeePayout;
    }

    public HashMap<Landlord, Double> getPreparedLandlordPayout() {
        return preparedLandlordPayout;
    }

    public void runPayroll(LocalDateTime date) throws InsufficientBalanceException{
        date = LocalDateTimeUtil.extractMonth(date);
        // pay salary and bonus
        HashMap<Employee, Double> ep = preparedEmployeePayout;
        for (Employee e : ep.keySet()) {
            double amount = ep.get(e);
            account.printCheck(amount, "payment to employee");
            ep.remove(e);
        }
        // pay landlord
        HashMap<Landlord, Double> lp = preparedLandlordPayout;
        for (Landlord l : lp.keySet()) {
            double amount = ep.get(l);
            account.printCheck(amount, "payment to landlord");
            ep.remove(l);
        }
        // set up next month's full time base salaries
        payroll.putIfAbsent(date.plusMonths(1),
                            new HashMap<String, PayrollItem>());
        for (Employee e : employees.values()) {
            if (!e.isPartTime()) {
                addPayrollItem(new FullTimeBaseSalary(e),
                                date.plusMonths(1));
            }
        }
    }

    public void prepareEmployeePayOut(LocalDateTime date) {
        HashMap<Employee, Double> ret = preparedEmployeePayout;
        for (PayrollItem pi : getMonthlyPayroll(date).values()) {
            if ((pi instanceof PartTimeBaseSalary
                    && !((PartTimeBaseSalary)pi).isApproved()
                    ) || pi.isPaid())
                continue;
            Employee e = pi.getEmployee();
            double amount = pi.getAmount();
            ret.put(e, amount + ret.getOrDefault(e, 0.0));
            pi.setPaid();
        }
    }

    public void prepareLandlordPayout() throws InsufficientBalanceException{
        HashMap<Landlord, Double> ret = preparedLandlordPayout;
        for (RentalProperty p : rentalProps.values()) {
            double balance = p.getAccount().getBalance();
            if (balance == 0)
                continue;
            p.getAccount().transferTo(account, balance);
            Landlord l = p.getLandlord();
            ret.put(l, balance + ret.getOrDefault(l, 0.0));
        }
    }

    public void addSalesBonus(SalesConsultant consultant, PurchaseOffer po)
                                    throws OperationNotAllowedException{
        addPayrollItem(new SalesBonus(consultant, po));
    }

    public void addPayrollItem(PayrollItem pi) {
        getThisMonthPayroll().put(pi.getId(), pi);
    }

    public void addPayrollItem(PayrollItem pi, LocalDateTime date) {
        getMonthlyPayroll(date).put(pi.getId(), pi);
    }

    public void submitHours(Employee e, int nHour) throws OperationNotAllowedException{
        PartTimeBaseSalary pbs = new PartTimeBaseSalary(nHour, e);
        addPayrollItem(pbs);
    }

    public List<PartTimeBaseSalary> getPendingHourSubmissions() {
        List<PartTimeBaseSalary> res = new ArrayList<PartTimeBaseSalary>();
        for (PayrollItem pri : getThisMonthPayroll().values()) {
            if (pri instanceof PartTimeBaseSalary) {
                PartTimeBaseSalary ptbs = (PartTimeBaseSalary)pri;
                if (ptbs.isPending())
                    res.add(ptbs);
            }
        }
        return res;
    }

    public PartTimeBaseSalary getHourSubmission(LocalDateTime date, Employee e) {
        String payrollItemId = PartTimeBaseSalary.formatId(e);
        return (PartTimeBaseSalary)getMonthlyPayroll(date).get(payrollItemId);
    }

    public PartTimeBaseSalary getThisMonthHourSubmission(Employee e) {
        return getHourSubmission(LocalDateTime.now(), e);
    }

    public HashMap<String, PayrollItem> getThisMonthPayroll() {
        return getMonthlyPayroll(LocalDateTime.now());
    }

    public HashMap<String, PayrollItem> getMonthlyPayroll(LocalDateTime date) {
        date = LocalDateTimeUtil.extractMonth(date);
        return payroll.get(date);
    }

    public void addEmployee(Employee e) {
        // store employee
        this.employees.put(e.getId(), e);
        // add to payroll
        if (!e.isPartTime()) {
            FullTimeBaseSalary pi = new FullTimeBaseSalary(e);
            addPayrollItem(pi);
        }
    }

    public List<Application> getApplications(Tenant t) {
        List<Application> res = new ArrayList<Application>();
        for (RentalProperty p : this.rentalProps.values())
            for (Application a : p.getApplicationsInitiatedBy(t))
                res.add(a);
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

    public void addProperty(Property p) {
        if (p instanceof RentalProperty) {
            this.rentalProps.put(p.getId(), (RentalProperty)p);
            List<Property> ps = getProperties(
                p.getOwner()
            );
            if (ps.size() >= 2)
                for (Property p_ : ps) {
                    RentalProperty rp = (RentalProperty)p_;
                    rp.setMinManagementFeeRate(0.06);
                    rp.setMaxManagementFeeRate(0.07);
                }
        } else {
            this.forSaleProps.put(p.getId(), (ForSaleProperty)p);
        }
        p.setBranch(this);
    }

    /*
    notify non-owners whose suburbs of interest includes this property's suburb
    */
    public void sendNotifForListedProperty(Property p) {
        boolean rental = p instanceof RentalProperty;
        String pSuburb = p.getSuburb();
        String pid = p.getId();
        // send notification
        for (Customer c : customers.values()) {
            if (!(c instanceof NonOwner))
                continue;
            if ((rental && c instanceof Tenant) || (!rental && c instanceof Buyer)) {
                NonOwner nonOwner = (NonOwner)c;
                if (nonOwner.getSuburbsOfInterest().contains(pSuburb)) {
                    Notification notif = new Notification(
                        String.format(
                            "A %s property in %s has just been listed. \nProperty id: %s.",
                            rental ? "rental" : "for-sale", pSuburb, pid
                        ),
                        pid
                    );
                    nonOwner.addNotif(notif);
                }
            }
        }
    }

    /*
    notify non-owners whose suburbs of interest includes this inspection's suburb
    */
    public void sendNotifForNewInspection(Property p, Inspection i) {
        String pSuburb = p.getSuburb();
        String pid = p.getId();
        boolean rental = p instanceof RentalProperty;
        for (Customer c : customers.values()) {
            if (!(c instanceof NonOwner))
                continue;
            if ((rental && c instanceof Tenant) || (!rental && c instanceof Buyer)) {
                NonOwner nonOwner = (NonOwner)c;
                if (nonOwner.getSuburbsOfInterest().contains(pSuburb)) {
                    Notification notif = new Notification(
                        String.format(
                            "An inspection is scheduled for a %s property in %s. "
                                + "\nTime: %s. \nProperty id: %s.",
                            rental ? "rental" : "for-sale",
                            pSuburb, i.getDateTimeS(), pid
                        ),
                        pid
                    );
                    nonOwner.addNotif(notif);
                }
            }
        }
    }

    /*
    notify non-owners whose suburbs of interest includes this inspection's suburb
    */
    public void sendNotifForCancelledInspection(Property p, Inspection i) {
        String pSuburb = p.getSuburb();
        String pid = p.getId();
        boolean rental = p instanceof RentalProperty;
        for (Customer c : customers.values()) {
            if (!(c instanceof NonOwner))
                continue;
            if ((rental && c instanceof Tenant) || (!rental && c instanceof Buyer)) {
                NonOwner nonOwner = (NonOwner)c;
                if (nonOwner.getSuburbsOfInterest().contains(pSuburb)) {
                    Notification notif = new Notification(
                        String.format(
                            "The inspection:\n\tTime: %s\n\tProperty id: %s\nhas been cancelled.",
                            i.getDateTimeS(), pid
                        ),
                        pid
                    );
                    nonOwner.addNotif(notif);
                }
            }
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

    public List<Property> getProperties(Customer owner) {
        return getProperties(null, owner);
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

    public List<Property> getProperties(Employee e) {
        return getProperties(null, e);
    }

    public List<NonOwner> getNonOwnerWithApplicant(ID id) {
        List<NonOwner> res = new ArrayList<NonOwner>();
        for (Customer c : customers) {
            if (c instanceof NonOwner) {
                if (((NonOwner)c).hasApplicant(id))
                    res.add((NonOwner)c);
            }
        }
        return res;
    }
}