package se;

import java.time.*;
import java.util.*;

import exception.*;
import user.customer.*;

public abstract class ApplicationBase {
    private String id;
    private List<ID> applicantIds;

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
    }

    public boolean initiatedBy(Customer c) {
        return this.initiator.equals(c);
    }

    public String getId() {
        return id;
    }

    public List<ApplicantDetail> getApplicants() {
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
