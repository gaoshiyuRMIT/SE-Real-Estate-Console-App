package console;

import java.io.*;

import java.util.*;
import java.time.*;
import static java.util.AbstractMap.SimpleEntry;


import se.*;
import user.*;
import user.employee.*;
import user.customer.*;
import property.*;
import consts.*;

public class TenantConsole extends BaseConsole {
    private Tenant user;
    private ApplicantDetail ad;

    public TenantConsole(User u, Branch branch, Scanner scanner,
                        BufferedReader reader, PropertyManager pm) {
        super(new String[] {
            "browse properties",
            "add applicant detail",
            "view applicant details",
            "submit application",
            "view my applications",
            "log out"
        }, branch, scanner, reader, pm);
        user = (Tenant)u;
    }

    public void browseProperties() throws Exception{
        System.out.println("Specify filter (leave empty if there is no constraint)");
        System.out.println("Enter road/street name: ");
        String address = getLine().trim();
        if (address.equals("none"))
            address = null;
        System.out.println("Enter suburb: ");
        String suburb = getLine().trim();
        if (suburb.equals("none"))
            suburb = null;
        System.out.println("Enter type(House/Unit/Flat/Townhouse/Studio): ");
        String typeS = getLine().trim();
        PropertyType type;
        if (typeS.equals("none"))
            type = null;
        else
            type = PropertyType.valueOf(typeS);
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        System.out.print("Enter number of bedrooms: ");
        cap.put("bedroom", scanner.nextInt());
        System.out.print("Enter number of bathrooms: ");
        cap.put("bathroom", scanner.nextInt());
        System.out.print("Enter number of car spaces: ");
        cap.put("car space", scanner.nextInt());
        for (RentalProperty rp : branch.browseRentalProperties(address, suburb, cap, type, true)) {
            System.out.printf(
                "%s %s\n",
                rp.getId(), rp.getSuburb()
            );
        }
    }

    public void addApplicantDetail() throws Exception{
        System.out.print("Enter ID type (Passport/DriverLicence): ");
        String idType = scanner.next();
        System.out.print("Enter ID number: ");
        String idContent = scanner.next();
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter annual income: ");
        double annualIncome = scanner.nextDouble();
        System.out.print("Enter occupation: ");
        String occupation = scanner.next();
        System.out.println("Enter employment history(3 lines) : ");
        List<String> employmentHistory = new ArrayList<String>();
        employmentHistory.add(getLine());
        employmentHistory.add(getLine());
        employmentHistory.add(getLine());
        System.out.println("Enter rental history (3 lines): ");
        List<String> rentalHistory = new ArrayList<String>();
        rentalHistory.add(getLine());
        rentalHistory.add(getLine());
        rentalHistory.add(getLine());
        ad = new ApplicantDetail(idType, idContent, name, annualIncome,
                                occupation, employmentHistory,
                                rentalHistory);
        user.addApplicant(ad);
        System.out.println("Applicant person detail successfully added.");
    }

    public void submitApplication() throws Exception{
        RentalProperty rp = (RentalProperty)getPropertyById();
        viewApplicantDetails();
        System.out.println("Which applicant do you want to add? ");
        System.out.print("Enter id type (Passport/DriverLicence): ");
        String idType = scanner.next();
        System.out.print("Enter id number: ");
        String idContent = scanner.next();
        ID id = new ID(idType, idContent);
        System.out.print("Enter weekly rental: ");
        double rental = scanner.nextDouble();
        System.out.print("Enter duration (number of months): ");
        int duration = scanner.nextInt();
        Application a = new Application(Arrays.asList(ad.getId()), rental, duration, user);
        rp.addApplication(a);
        System.out.println("Application successfully submitted.");
    }

    public void viewMyApplications() throws Exception{
        for (SimpleEntry<RentalProperty, Application> e : branch.getApplications(user)) {
            RentalProperty p = e.getKey();
            Application a = e.getValue();
            System.out.printf(
                "Property %s %s, Application %s %s\n",
                p.getId(), p.getSuburb(), a.getId(), a.getStatusS()
            );
        }
    }

    public void viewApplicantDetails() throws Exception{
        for (ApplicantDetail ad : user.getApplicants(null)) {
            System.out.printf(
                "%s %s\n",
                ad.getId(), ad.getName()
            );
        }
    }

    public void console() throws Exception{
        while (true) {
            try {
                String option = displayMenu();
                if (option == "browse properties")
                    browseProperties();
                else if (option == "add applicant detail")
                    addApplicantDetail();
                else if (option == "submit application")
                    submitApplication();
                else if (option.equals("view my applications"))
                    viewMyApplications();
                else if (option.equals("view applicant details"))
                    viewApplicantDetails();
                else
                    break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}