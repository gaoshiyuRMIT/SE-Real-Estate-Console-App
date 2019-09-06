package property;

import java.util.*;
import static java.util.AbstractMap.SimpleEntry;

import se.*;
import consts.*;
import user.customer.*;
import user.employee.*;
import exception.*;

public class RentalProperty extends Property {
    private double weeklyRental;

    // number of months
    private int desiredDuration;

    private ArrayList<Lease> leases;
    private double managementFeeRate;
    private SimpleEntry<Double, Double> managementFeeRateRange;

    public RentalProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            PropertyType type, double weeklyRental, int desiredDuration,
                            Landlord landlord)
                            throws InvalidParamException {
        super(address, suburb, capacity, type, landlord);
        this.weeklyRental = weeklyRental;
        this.desiredDuration = desiredDuration;
        this.managementFeeRate = 0.8;
        this.managementFeeRateRange = new SimpleEntry<Double, Double>(0.7, 0.8);
    }

    public void setWeeklyRental(double r) throws OperationNotAllowedException {
        if (getCurrentLease() != null)
            throw new OperationNotAllowedException(
                "Rental may only be changed when the current lease is expired."
            );
        this.weeklyRental = r;
    }

    public void setManager(PropertyManager e) {
        setEmployee(e);
    }

    public PropertyManager getManager() {
        return (PropertyManager)(getEmployee());
    }

    public double getManagementFee() {
        Lease l = getCurrentLease();
        double weeklyRental = (l == null) ? this.weeklyRental : l.getWeeklyRental();
        return this.managementFeeRate * weeklyRental * 365 / (12 * 7);
    }

    public void setMinManagementFeeRate(double f) {
        managementFeeRateRange = new SimpleEntry<Double, Double>(
            f, managementFeeRateRange.getValue()
        );
    }

    public void setMaxManagementFeeRate(double f) {
        managementFeeRateRange.setValue(f);
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

    public ArrayList<Application> getPendingApplications() {
        ArrayList<Application> res = new ArrayList<Application>();
        for (ApplicationBase a : super.getPendingApplicationBases())
            res.add((Application)a);
        return res;
    }

    public ArrayList<Application> getApplicationsInitiatedBy(Tenant c) {
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
}