package se;

import java.time.*;
import java.util.*;

import exception.*;
import user.customer.*;
import property.*;

public class PurchaseOffer extends ApplicationBase {
    private static int idCounter = 0;

    private double amount;
    private double commissionRate;
    private boolean depositPaid;
    private boolean settlementPaid;
    private ForSaleProperty property;

    public static String genId() {
        return String.format("po%08s", idCounter++);
    }

    public PurchaseOffer(double amount, Buyer buyer, List<ID> applicantIds)
                            throws InvalidParamException {
        super(genId(), applicantIds, buyer);
        this.amount = amount;
        depositPaid = false;
        settlementPaid = false;
        commissionRate = 0;
    }

    public double getCommissionRate() {
        if (!isFulfilled())
            return property.getCommissionRate();
        else
            return commissionRate;
    }

    public ForSaleProperty getProperty() {
        return (ForSaleProperty)super.getProperty();
    }

    public String getTextualDetail() {
        return super.getTextualDetail()
            + "\n" + String.format(
                "%-30s: %s\n"
                    + "%-30s: %s",
                "buyer", getInitiator().getId(),
                "amount", getAmount()
            );
    }

    public boolean isSecured() {
        return depositPaid;
    }

    public boolean isFulfilled() {
        return settlementPaid;
    }

    public void setSettlementPaid() throws OperationNotAllowedException {
        if (!isSecured())
            throw new OperationNotAllowedException(
                "Settlement cannot be paid before deposit is paid."
            );
        settlementPaid = true;
        commissionRate = getProperty().getCommissionRate();
    }

    public void setDepositPaid() {
        depositPaid = true;
    }

    public double getAmount() {
        return amount;
    }

    public double getCommission() {
        return amount * getCommissionRate();
    }

}