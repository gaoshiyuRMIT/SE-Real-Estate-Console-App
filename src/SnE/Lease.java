package SnE;

import java.time.*;

import consts.*;
import user.customer.*;

public class Lease {
    private LocalDateTime startDate;
    // # months
    private int duration;
    private double weeklyRental;
    private ApplicantDetail[] tenants;
    // active, expired
    private LeaseStatus status;

    public Lease(Application a) {
        this.duration = a.getDuration();
        this.weeklyRental = a.getWeeklyRental();
        this.tenants = a.getApplicants();
        this.status = LeaseStatus.Active;
        this.startDate = LocalDateTime.now();
    }

    public LeaseStatus getStatus() {
        if (LocalDateTime.now().compareTo(startDate.plusMonths(duration)) > 0) {
            status = LeaseStatus.Expired;
        }
        return status;
    }

}