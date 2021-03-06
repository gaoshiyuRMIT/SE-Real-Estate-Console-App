package property;

import java.util.*;

import se.*;
import consts.*;
import user.customer.*;
import user.employee.*;
import exception.*;

public class ForSaleProperty extends Property {
    private static final double maxCommissionRate = 0.05;
    private static final double minCommissionRate = 0.02;
    private double commissionRate;
    private double minPrice;

    public ForSaleProperty(String address, String suburb, HashMap<String, Integer> capacity,
                            PropertyType type, double minPrice, Vendor vendor)
                            throws InvalidParamException {
        super(address, suburb, capacity, type, vendor);
        this.minPrice = minPrice;
        // by default, commission is set to max
        this.commissionRate = maxCommissionRate;
    }

    public String getTextualDetail() {
        SalesConsultant sc = getConsultant();
        return super.getTextualDetail()
            + "\n" + String.format(
                "%-30s: %.2f\n"
                    + "%-30s: %s",
                "mininum price", minPrice,
                "sales consultant", sc != null ? getConsultant().getId() : "not assigned"
            );
    }

    public List<PurchaseOffer> getApplications() {
        List<PurchaseOffer> res = new ArrayList<PurchaseOffer>();
        for (ApplicationBase ab : super.getApplicationBases())
            res.add((PurchaseOffer)ab);
        return res;
    }

    public boolean hasSection32() {
        return this.getDocuments().containsKey("Section 32");
    }

    public void setMinPrice(double p) {
        minPrice = p;
    }

    public void list() throws OperationNotAllowedException {
        if (this.getConsultant() == null)
            throw new OperationNotAllowedException(
                "A sales consultant must be assigned before the property can be listed."
            );
        super.list();
    }

    public Vendor getVendor() {
        return (Vendor)(getOwner());
    }

    public void setConsultant(SalesConsultant e) {
        setEmployee(e);
    }

    public SalesConsultant getConsultant() {
        return (SalesConsultant)(getEmployee());
    }

    public void setCommissionRate(double r) throws InvalidParamException{
        if (r > maxCommissionRate || r < minCommissionRate) {
            throw new InvalidParamException(
                String.format(
                    "The rate must be between %.2f and %.2f.",
                    minCommissionRate, maxCommissionRate
                )
            );
        }
        this.commissionRate = r;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void addApplication(PurchaseOffer a) throws OperationNotAllowedException,
                                                        OfferAmountTooLowException {
        if (a.getAmount() < minPrice)
            throw new OfferAmountTooLowException();
        super.addApplicationBase(a);
    }

    public ArrayList<PurchaseOffer> getPendingApplications() {
        ArrayList<PurchaseOffer> res = new ArrayList<PurchaseOffer>();
        for (ApplicationBase a : super.getPendingApplicationBases())
            res.add((PurchaseOffer)a);
        return res;
    }

    public ArrayList<PurchaseOffer> getApplicationsInitiatedBy(Buyer c) {
        ArrayList<PurchaseOffer> res = new ArrayList<PurchaseOffer>();
        for (ApplicationBase a : super.getApplicationBasesInitiatedBy(c))
            res.add((PurchaseOffer)a);
        return res;
    }

    public void addInspection(Inspection i) throws OperationNotAllowedException {
        if (!hasSection32())
            throw new OperationNotAllowedException(
                "Before any inspection can be conducted, "
                + "section 32 must be compiled by a legal professional "
                + "and scanned into the system."
            );
        super.addInspection(i);
    }

    public void acceptApplication(PurchaseOffer a) throws OperationNotAllowedException {
        super.acceptApplicationBase(a);
    }

    public void rejectApplication(PurchaseOffer a) throws OperationNotAllowedException {
        super.rejectApplicationBase(a);
    }

    public void withdrawApplication(PurchaseOffer a) throws OperationNotAllowedException {
        super.withdrawApplicationBase(a);
    }

    public void payDepositForApplication(PurchaseOffer a)
                                        throws OperationNotAllowedException {
        if (!this.getApplications().contains(a) || !a.isAwaitingPayment())
            throw new OperationNotAllowedException();
        a.setDepositPaid();
        setStatus(PropertyStatus.Secured);
        cancelAllInspections();
    }

    public void paySettlementForApplication(PurchaseOffer a)
                                            throws OperationNotAllowedException{
        if (!this.getApplications().contains(a) || !a.isSecured())
            throw new OperationNotAllowedException();
        a.setSettlementPaid();
        // credit branch account with commission
        double commission = a.getCommission();
        Branch branch;
        branch = getBranch();
        branch.getAccount().deposit(commission);
        // create sales bonus
        branch.addSalesBonus(getConsultant(), a);
    }
}