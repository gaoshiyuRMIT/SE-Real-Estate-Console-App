package se;

import java.time.*;
import java.util.*;

import exception.*;
import user.customer.*;

public class PurchaseOffer extends ApplicationBase {
    private static int idCounter = 0;

    private double amount;
    private boolean depositPaid;
    private boolean settlementPaid;

    public static String genId() {
        return String.format("po%08s", idCounter++);
    }

    public PurchaseOffer(double amount, Buyer buyer, List<ID> applicantIds)
                            throws InvalidParamException {
        super(genId(), applicantIds, buyer);
        this.amount = amount;
        depositPaid = false;
        settlementPaid = false;
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
    }

    public void setDepositPaid() {
        depositPaid = true;
    }

    public double getAmount() {
        return amount;
    }
}