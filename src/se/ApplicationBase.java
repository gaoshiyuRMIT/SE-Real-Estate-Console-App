package se;

import java.time.*;
import java.util.*;

import exception.*;
import user.customer.*;

public abstract class ApplicationBase {
    private String id;
    private List<ID> applicantIds;
    private List<ApplicantDetail> applicants;

    private NonOwner initiator;
    private LocalDateTime dateReceived;
    private LocalDateTime dateAccepted;
    private boolean accepted;
    private boolean rejected;
    private boolean withdrawn;

    public ApplicationBase(String id, List<ID> applicantIds, NonOwner initiator)
                            throws InvalidParamException{
        this.id = id;
        accepted = false;
        rejected = false;
        withdrawn = false;
        dateReceived = LocalDateTime.now();
        if (applicantIds == null || applicantIds.size() == 0)
            throw new InvalidParamException("Applicant details not specified.");
        for (ID adid : applicantIds)
            if (!initiator.hasApplicant(adid))
                throw new InvalidParamException(
                    "The applicant ids supplied are inconsistent with the initiator."
                );
        this.applicantIds = applicantIds;
        this.initiator = initiator;
        this.applicants = null;
    }

    public String getStatusS() {
        if (isPending())
            return "pending";
        if (isAwaitingPayment())
            return "awaiting payment";
        if (isRejected())
            return "rejected";
        if (isWithdrawn())
            return "withdrawn";
        if (isSecured())
            return "secured";
        return "other";
    }

    public boolean initiatedBy(Customer c) {
        return this.initiator.equals(c);
    }

    public String getId() {
        return id;
    }

    public List<ApplicantDetail> getApplicants() {
        if (this.applicants != null)
            return this.applicants;
        return this.initiator.getApplicants(applicantIds);
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
        // cannot edit applicant detail after application is accepted
        List<ApplicantDetail> res = new ArrayList<ApplicantDetail>();
        for (ApplicantDetail d : this.getApplicants())
            res.add(new ApplicantDetail(d));
        this.applicants = res;
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

    public boolean equals(Object o) {
        if (o instanceof ApplicationBase) {
            ApplicationBase a = (ApplicationBase)o;
            return id == a.id;
        }
        return false;
    }
}
