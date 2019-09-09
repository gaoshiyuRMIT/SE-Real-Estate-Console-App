package user.customer;

import java.util.*;

public class ApplicantDetail {
    private ID id;
    private String name;
    private double annualIncome;
    private String occupation;
    private List<String> employmentHistory;
    private List<String> rentalHistory;

    public ApplicantDetail(ApplicantDetail d) {
        this.id = d.id;
        this.name = d.name;
        this.annualIncome = d.annualIncome;
        this.occupation = d.occupation;
        this.employmentHistory = d.employmentHistory;
        this.rentalHistory = d.rentalHistory;
    }

    public ApplicantDetail(ID id, String name, double annualIncome, String occupation,
                            List<String> employmentHistory,
                            List<String> rentalHistory) {
        this.id = id;
        this.name = name;
        this.annualIncome = annualIncome;
        this.occupation = occupation;
        this.employmentHistory = employmentHistory;
        this.rentalHistory = rentalHistory;
    }

    public ID getId() {
        return id;
    }

    public boolean equals(Object o) {
        if (o instanceof ApplicantDetail) {
            ApplicantDetail d = (ApplicantDetail)o;
            return id.equals(d.id);
        }
        return false;
    }
}