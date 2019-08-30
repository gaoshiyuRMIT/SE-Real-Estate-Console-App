package SnE;

import java.time.*;

import user.*;

public class Application {
    private Tenant[] applicants;
    private double weeklyRental;

    // number of months
    private int duration;
    private LocalDateTime dateReceived;
    private LocalDateTime dateAccepted;
    private boolean accepted;
    private boolean rejected;
    private boolean rentBondPaid;
    private boolean withdrawn;

    public Application(Tenant[] applicants, double weeklyRental, int duration) {
        accepted = false;
        rejected = false;
        rentBondPaid = false;
        withdrawn = false;
        dateReceived = LocalDateTime.now();
        this.applicants = applicants;
        this.weeklyRental = weeklyRental;
        this.duration = duration;
    }

    public Tenant[] getApplicants() {
		return applicants;
	}

	public void setApplicants(Tenant[] applicants) {
		this.applicants = applicants;
	}

	public double getWeeklyRental() {
		return weeklyRental;
	}

	public void setWeeklyRental(double weeklyRental) {
		this.weeklyRental = weeklyRental;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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

    public boolean isWithDrawn() {
        if (accepted && LocalDateTime.now().compareTo(dateAccepted.plusHours(24)) > 0
                        && !rentBondPaid)
            withdrawn = true;
        return withdrawn;
    }
}