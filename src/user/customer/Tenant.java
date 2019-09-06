package user.customer;

import java.util.*;

import se.*;
import exception.*;
import property.*;

public class Tenant extends Customer {
    private HashMap<String, Application> applications;
    private ArrayList<ApplicantDetail> applicants;

    public Tenant(String email, String password) {
        super(email, password);
    }

    public String getRole() {
        return "Tenant";
    }

    public ArrayList<ApplicantDetail> getApplicants() {
        return applicants;
    }

    public void addApplicant(ApplicantDetail d) {
        this.applicants.add(d);
    }
}