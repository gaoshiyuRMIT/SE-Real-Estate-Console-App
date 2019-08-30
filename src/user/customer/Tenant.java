package user.customer;

import java.util.*;

import SnE.*;
import exception.*;
import property.*;

public class Tenant extends Consumer {
    private HashMap<String, Application> applications;

    public Tenant(String email, String password) {
        super(email, password);
    }

    public void initiateApplication(RentalProperty property, double weeklyRental,
                                    int duration, ApplicantDetail[] details)
                                    throws OperationNotAllowedException {
        Application a = new Application(details, weeklyRental, duration);
        property.addApplication(a);
        this.applications.put(a.getId(), a);
    }

    public void setApplicantDetail(ApplicantDetail detail, ApplicantDetail newDetail) {
        detail.copyFrom(newDetail);
    }
}