package SnE;

import java.time.*;
import java.util.*;

import user.customer.*;

public abstract class ApplicationBase {
    private String id;
    private ArrayList<ApplicantDetail> applicants;

    private Customer initiator;
    private LocalDateTime dateReceived;
    private LocalDateTime dateAccepted;
    private boolean accepted;
    private boolean rejected;
    private boolean withdrawn;

    public ApplicationBase(String id, ArrayList<ApplicantDetail> applicants, Customer initiator) {
        this.id = id;
        accepted = false;
        rejected = false;
        withdrawn = false;
        dateReceived = LocalDateTime.now();
        this.applicants = applicants;
        this.initiator = initiator;
    }

    public boolean initiatedBy(Customer c) {
        return this.initiator.equals(c);
    }

    public String getId() {
        return id;
    }

    public ArrayList<ApplicantDetail> getApplicants() {
        return applicants;
    }

    public abstract boolean isSecured();

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isRejected() {
        if (!accepted && !rejected
                        && LocalDateTime.now().compareTo(dateReceived.plusDays(3)) > 0)
            rejected = true;
        return rejected;
    }

    public void setAccepted() {
        accepted = true;
        dateAccepted = LocalDateTime.now();
    }

    public void setRejected() {
        rejected = true;
    }

    public void setWithdrawn() {
        withdrawn = true;
    }

    public boolean isWithdrawn() {
        if (accepted && LocalDateTime.now().compareTo(dateAccepted.plusHours(24)) > 0
                        && !isSecured())
            withdrawn = true;
        return withdrawn;
    }

    public boolean isActive() {
        return !isRejected() && !isWithdrawn();
    }

    public boolean isWithdrawable() {
        return !isSecured() && !isRejected() && !isWithdrawn();
    }

    public boolean isPending() {
        return !isAccepted() && !isRejected() && !isWithdrawn();
    }

    public boolean isAwaitingPayment() {
        return isAccepted() && !isSecured() && !isWithdrawn();
    }

    public boolean equals(ApplicationBase a) {
        return id == a.id;
    }
}