package se;

import java.time.*;
import java.util.*;

import exception.*;
import user.customer.*;

public class Application extends ApplicationBase {
    private static int idCounter = 0;

    private double weeklyRental;

    // number of months
    private int duration;
    private boolean rentBondPaid;

    public static String genId() {
        return String.format("a%08d", idCounter++);
    }

    public Application(List<ID> applicantIds, double weeklyRental,
                        int duration, Tenant initiator)
                        throws InvalidParamException {
        super(genId(), applicantIds, initiator);
        this.weeklyRental = weeklyRental;
        this.duration = duration;
        rentBondPaid = false;

    }

	public double getWeeklyRental() {
		return weeklyRental;
	}

	public void setWeeklyRental(double weeklyRental) throws OperationNotAllowedException{
        if (isAccepted())
            throw new OperationNotAllowedException(
                "The weekly rental of an accepted application cannot be modified."
            );
		this.weeklyRental = weeklyRental;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) throws OperationNotAllowedException {
        if (isAccepted())
            throw new OperationNotAllowedException(
                "The contract duration of an accepted application cannot be modified."
            );
		this.duration = duration;
	}

    public void setRentBondPaid() {
        this.rentBondPaid = true;
    }

    public boolean isSecured() {
        return rentBondPaid;
    }
}