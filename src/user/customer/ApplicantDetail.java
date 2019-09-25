package user.customer;

import java.util.*;

import consts.*;

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

    public ApplicantDetail(String idType, String idContent, String name, double annualIncome, String occupation,
                            List<String> employmentHistory,
                            List<String> rentalHistory) {
        this(new ID(IDType.valueOf(idType), idContent), name, annualIncome,
                occupation, employmentHistory, rentalHistory);
    }

    public ID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(ApplicantDetail d) {
        return id.equals(d.id);
    }
}