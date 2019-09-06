package se;

import java.time.*;
import java.util.*;

import user.customer.*;

public class Application extends ApplicationBase {
    private static int idCounter = 0;

    private double weeklyRental;

    // number of months
    private int duration;
    private boolean rentBondPaid;

    public static String genId() {
        return String.format("a%08s", idCounter++);
    }

    public Application(ArrayList<ApplicantDetail> applicants, double weeklyRental,
                        int duration, Tenant initiator) {
        super(genId(), applicants, initiator);
        this.weeklyRental = weeklyRental;
        this.duration = duration;
        rentBondPaid = false;

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

    public void setRentBondPaid() {
        this.rentBondPaid = true;
    }

    public boolean isSecured() {
        return rentBondPaid;
    }
}