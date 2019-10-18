package console;

import java.io.*;

import java.util.*;
import java.time.*;
import static java.util.AbstractMap.SimpleEntry;


import se.*;
import exception.*;
import user.*;
import user.employee.*;
import user.customer.*;
import property.*;
import consts.*;

public class TenantConsole extends BaseConsole {
    private Tenant user;
    private ApplicantDetail ad;

    public TenantConsole(User u, BaseConsole base) {
        super(new String[] {
            "browse properties",
            "add applicant detail",
            "view applicant details",
            "submit application",
            "view my applications",
            "edit suburbs of interest",
            "view active notifications",
            "view dismissed notifications",
            "log out"
        }, base);
        user = (Tenant)u;
    }

    public void browseProperties() throws InternalException, InvalidInputException{
        System.out.println("Specify filter (leave empty if there is no constraint)");
        System.out.println("Enter road/street name: ");
        String address = getLine();
        System.out.println("Enter suburb: ");
        String suburb = getLine();
        System.out.println("Enter type(House/Unit/Flat/Townhouse/Studio): ");
        String typeS = getLine();

        PropertyType type;
        if (typeS.isEmpty())
            type = null;
        else {
            try {
                type = PropertyType.valueOf(typeS);
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException("Invalid input for property type!");
            }
        }

        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        System.out.println("Enter number of bedrooms: ");
        String nBedroomS = getLine();
        System.out.println("Enter number of bathrooms: ");
        String nBathroomS = getLine();
        System.out.println("Enter number of car spaces: ");
        String nCarSpaceS = getLine();

        try {
            if (!nBedroomS.isEmpty())
                cap.put("bedroom", Integer.parseInt(nBedroomS));

            if (!nBathroomS.isEmpty())
                cap.put("bathroom", Integer.parseInt(nBathroomS));

            if (!nCarSpaceS.isEmpty())
                cap.put("car space", Integer.parseInt(nCarSpaceS));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input format for capacity!");
        }
        for (RentalProperty rp : branch.browseRentalProperties(address, suburb, cap, type, true)) {
            System.out.printf(
                "%s %s\n",
                rp.getId(), rp.getSuburb()
            );
        }
    }

    public void addApplicantDetail() throws InternalException, InvalidInputException{
        System.out.print("Enter ID type (Passport/DriverLicence): ");
        String idType = scanner.next();
        System.out.print("Enter ID number: ");
        String idContent = scanner.next();
        System.out.println("Enter name: ");
        String name = getLine();
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
        try {
            user.addApplicant(ad);
        } catch (ApplicantExistException e) {
            throw new InternalException(e);
        }
        System.out.println("Applicant person detail successfully added.");
    }

    public void submitApplication() throws InvalidInputException, InternalException{
        RentalProperty rp = (RentalProperty)getPropertyById();
        System.out.println("Which applicant do you want to add? ");
        viewApplicantDetails();
        System.out.print("Enter id type (Passport/DriverLicence): ");
        String idType = scanner.next();
        System.out.print("Enter id number: ");
        String idContent = scanner.next();
        ID id = new ID(idType, idContent);
        System.out.print("Enter weekly rental: ");
        double rental = scanner.nextDouble();
        System.out.print("Enter duration (number of months): ");
        int duration = scanner.nextInt();
        Application a;
        try {
            a = new Application(Arrays.asList(id), rental, duration, user);
        } catch (InvalidParamException e) {
            throw new InvalidInputException(e);
        }
        try {
            rp.addApplication(a);
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
        System.out.println("Application successfully submitted.");
    }

    public void viewMyApplications() {
        List<Application> appList = new ArrayList<Application>();
        for (Application a : branch.getApplications(user)) {
            appList.add(a);
        }
        (new ApplicationListConsole(user, appList, this)).console();
    }

    public void viewApplicantDetails() {
        System.out.println(util.getPageBreak("Applicants"));
        for (ApplicantDetail ad : user.getApplicants()) {
            System.out.println(ad.getTextualDetail());
            System.out.println(util.getPageBreak());
        }
    }

    public void editSuburbsOfInterest() throws InternalException{
        System.out.printf("Suburbs of interest: %s\n",
                            String.join(", ", user.getSuburbsOfInterest()));
        System.out.println("Enter suburbs of interest (seperated by space):");
        String line = getLine();
        String[] suburbs = line.split("\\s+");
        user.setSuburbsOfInterest(Arrays.asList(suburbs));
    }

    public void viewActiveNotifications() {
        (new NotificationListConsole(
                user,
                user.getNotifications(NotifStatus.Active),
                this
            )
        ).console();
    }

    public void viewDismissedNotifications() {
        (new NotificationListConsole(
                user,
                user.getNotifications(NotifStatus.Archived),
                this
            )
        ).console();
    }

    public User getUser() {
        return user;
    }


    public void console() {
        super.console();
        while (true) {
            try {
                String option = displayMenu();
                if (option.equals("browse properties"))
                    browseProperties();
                else if (option.equals("add applicant detail"))
                    addApplicantDetail();
                else if (option.equals("submit application"))
                    submitApplication();
                else if (option.equals("view my applications"))
                    viewMyApplications();
                else if (option.equals("view applicant details"))
                    viewApplicantDetails();
                else if (option.equals("edit suburbs of interest"))
                    editSuburbsOfInterest();
                else if (option.equals("view active notifications"))
                    viewActiveNotifications();
                else if (option.equals("view dismissed notifications"))
                    viewDismissedNotifications();
                else
                    break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (InternalException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}