package property;

import java.util.*;

import SnE.*;
import consts.*;
import user.customer.*;
import user.employee.*;
import exception.*;

public class RentalProperty extends Property {
    private double weeklyRental;

    // number of months
    private int desiredDuration;

    private ArrayList<Lease> leases;
    private double managementFee;

    public RentalProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            PropertyType type, Landlord landlord)
                            throws InvalidParamException {
        super(address, suburb, capacity, type, landlord);
    }

    public void setManager(PropertyManager e) {
        setEmployee(e);
    }

    public PropertyManager getManager() {
        return (PropertyManager)(getEmployee());
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
        Lease lease = new Lease(a);
        this.leases.add(lease);
    }

    public void withdrawApplication(Application a) throws OperationNotAllowedException {
        super.withdrawApplicationBase(a);
    }
}