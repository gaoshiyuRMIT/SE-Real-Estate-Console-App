package user.customer;

import java.util.*;

public class ApplicantDetail {
    private String name;
    // uniquely identifies a person (like passport number or driver license number)
    private String id;
    private double annualIncome;
    private String occupation;
    private ArrayList<String> employmentHistory;
    private ArrayList<String> rentalHistory;

    public void copyFrom(ApplicantDetail d) {
        this.name = d.name;
        this.id = d.id;
        this.annualIncome = d.annualIncome;
        this.occupation = d.occupation;
        this.employmentHistory = d.employmentHistory;
        this.rentalHistory = d.rentalHistory;
    }
}