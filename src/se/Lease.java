package se;

import java.time.*;
import java.util.*;

import consts.*;
import user.customer.*;

public class Lease {
    private LocalDateTime startDate;
    private int duration;
    private List<ApplicantDetail> tenants;
    private double weeklyRental;

    public Lease(Application a) {
        this.startDate = LocalDateTime.now();
        this.duration = a.getDuration();
        this.tenants = a.getApplicants();
        this.weeklyRental = a.getWeeklyRental();
    }

    public double getWeeklyRental() {
        return weeklyRental;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isExpired() {
        return LocalDateTime.now().compareTo(startDate.plusMonths(duration)) > 0;
    }

    public List<ApplicantDetail> getTenants() {
        return tenants;
    }
}