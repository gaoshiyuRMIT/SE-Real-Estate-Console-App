package se;

import java.time.*;
import java.util.*;

import consts.*;
import user.customer.*;

public class Lease {
    private LocalDateTime startDate;
    private int duration;
    private ArrayList<ApplicantDetail> tenants;
    private double weeklyRental;

    public Lease(Application a) {
        this.startDate = LocalDateTime.now();
        this.duration = a.getDuration();
        this.tenants = new ArrayList<ApplicantDetail>();
        for (ApplicantDetail d : a.getApplicants())
            this.tenants.add(new ApplicantDetail(d));
        this.weeklyRental = a.getWeeklyRental();
    }

    public double getWeeklyRental() {
        return weeklyRental;
    }

    public boolean isExpired() {
        return LocalDateTime.now().compareTo(startDate.plusMonths(duration)) > 0;
    }

}