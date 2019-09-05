package SnE;

import java.time.*;
import java.util.*;

import user.customer.*;

public class PurchaseOffer extends ApplicationBase {
    private static int idCounter = 0;

    private double amount;
    private boolean depositPaid;
    private boolean settlementPaid;

    public static String genId() {
        return String.format("po%08s", idCounter++);
    }

    public PurchaseOffer(double amount, Buyer buyer, ArrayList<ApplicantDetail> applicants) {
        super(genId(), applicants, buyer);
        this.amount = amount;
        depositPaid = false;
        settlementPaid = false;
    }

    public boolean isSecured() {
        return depositPaid;
    }

    public void setSettlementPaid() {
        settlementPaid = true;
    }

    public void setDepositPaid() {
        depositPaid = true;
    }


}