package property;

import java.util.*;
import static java.util.AbstractMap.SimpleEntry;

import se.*;
import finance.*;
import consts.*;
import user.customer.*;
import user.employee.*;
import exception.*;

public class RentalProperty extends Property {
    private double weeklyRental;

    // number of months
    private int desiredDuration;

    private List<Lease> leases;
    private double managementFeeRate;
    private SimpleEntry<Double, Double> managementFeeRateRange;
    private Account account;

    public RentalProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            PropertyType type, double weeklyRental, int desiredDuration,
                            Landlord landlord)
                            throws InvalidParamException {
        super(address, suburb, capacity, type, landlord);
        this.weeklyRental = weeklyRental;
        this.desiredDuration = desiredDuration;
        this.managementFeeRate = 0.08;
        this.managementFeeRateRange = new SimpleEntry<Double, Double>(0.07, 0.08);
        this.leases = new ArrayList<Lease>();
        this.account = new Account();
    }


    public RentalProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            String typeS, double weeklyRental, int desiredDuration,
                            Landlord landlord)
                            throws InvalidParamException {
        this(address, suburb, capacity, PropertyType.valueOf(typeS), weeklyRental,
            desiredDuration, landlord);
    }

    public Application getApplicationById(String id) {
        ApplicationBase ab = super.getApplicationBaseById(id);
        if (ab == null)
            return null;
        return (Application)ab;
    }

    public int getDuration() {
        return desiredDuration;
    }

    public List<Application> getApplications() {
        List<Application> res = new ArrayList<Application>();
        for (ApplicationBase ab : super.getApplicationBases())
            res.add((Application)ab);
        return res;
    }

    public double getWeeklyRental() {
        return weeklyRental;
    }

    public void setWeeklyRental(double r) throws OperationNotAllowedException {
        if (getCurrentLease() != null)
            throw new OperationNotAllowedException(
                "Rental may only be changed when the current lease is expired."
            );
        this.weeklyRental = r;
    }

    public void setDuration(int d) {
        desiredDuration = d;
    }

    public void setManager(PropertyManager e) {
        setEmployee(e);
    }

    public PropertyManager getManager() {
        return (PropertyManager)(getEmployee());
    }

    public double getManagementFee() {
        return this.managementFeeRate * getDeFactoMonthlyRental();
    }

    public double getDeFactoMonthlyRental() {
        Lease l = getCurrentLease();
        double weeklyRental = (l == null) ? this.weeklyRental : l.getWeeklyRental();
        return weeklyRental * 365 / (12 * 7);
    }

    public void setMinManagementFeeRate(double f) {
        managementFeeRateRange = new SimpleEntry<Double, Double>(
            f, managementFeeRateRange.getValue()
        );
        if (managementFeeRate < f)
            managementFeeRate = f;
    }

    public void setMaxManagementFeeRate(double f) {
        managementFeeRateRange.setValue(f);
        if (managementFeeRate > f)
            managementFeeRate = f;
    }

    public double getManagementFeeRate() {
        return managementFeeRate;
    }

    public void setManagementFeeRate(double r) throws InvalidParamException {
        double min = managementFeeRateRange.getKey();
        double max = managementFeeRateRange.getValue();
        if (r < min || r > max)
            throw new InvalidParamException(
                String.format("The rate must be between %.2f and %.2f.", min, max)
            );
        managementFeeRate = r;
    }

    public void list() throws OperationNotAllowedException {
        if (this.getManager() == null)
            throw new OperationNotAllowedException(
                "A property manager must be assigned before the property can be listed."
            );
        super.list();
    }

    public Landlord getLandlord() {
        return (Landlord)(getOwner());
    }

    public void addApplication(Application a) throws OperationNotAllowedException{
        super.addApplicationBase(a);
    }

    public List<Application> getPendingApplications() {
        ArrayList<Application> res = new ArrayList<Application>();
        for (ApplicationBase a : super.getPendingApplicationBases())
            res.add((Application)a);
        return res;
    }

    public List<Application> getApplicationsInitiatedBy(Tenant c) {
        ArrayList<Application> res = new ArrayList<Application>();
        for (ApplicationBase a : super.getApplicationBasesInitiatedBy(c))
            res.add((Application)a);
        return res;
    }

    public void acceptApplication(Application a) throws OperationNotAllowedException {
        super.acceptApplicationBase(a);
    }

    public void rejectApplication(Application a) throws OperationNotAllowedException {
        super.rejectApplicationBase(a);
    }

    public void payRentBondForApplication(Application a)
                                        throws OperationNotAllowedException {
        if (!this.getApplications().contains(a) || !a.isAwaitingPayment())
            throw new OperationNotAllowedException();
        a.setRentBondPaid();
        setStatus(PropertyStatus.Secured);
        cancelAllInspections();
        Lease lease = new Lease(a);
        this.leases.add(lease);
        // credit account
        double credit = a.getBondAmount() + getDeFactoMonthlyRental();
        account.deposit(credit);
    }

    public void withdrawApplication(Application a) throws OperationNotAllowedException {
        super.withdrawApplicationBase(a);
    }

    public Lease getCurrentLease() {
        if (this.leases.size() == 0)
            return null;
        Lease l = this.leases.get(this.leases.size()-1);
        if (l.isExpired())
            return null;
        return l;
    }
    public void deductManagementFee() throws InsufficientBalanceException {
        double amount = getManagementFee();
        account.withdraw(amount);
        getBranch().getAccount().deposit(amount);
    }

    public void deductPropertyExpense(double amount) throws InsufficientBalanceException {
        try {
            account.withdraw(amount);
        } catch (InsufficientBalanceException e) {
            getBranch().getAccount().withdraw(amount);
        }
    }

    public Account getAccount() {
        return account;
    }

    public String getTextualDetail() {
        PropertyManager pm = getManager();
        return super.getTextualDetail()
                + "\n" + String.format(
                    "%-30s: %.2f per week\n"
                        + "%-30s: %d months\n"
                        + "%-30s: %s",
                    "desired rental", weeklyRental,
                    "desired contract duration", desiredDuration,
                    "property manager", pm != null ? getManager().getId() : "not assigned"
                );
    }

}