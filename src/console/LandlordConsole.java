package console;

import java.io.*;

import java.util.*;
import java.time.*;

import se.*;
import user.*;
import user.employee.*;
import user.customer.*;
import property.*;

public class LandlordConsole extends BaseConsole {
    private Landlord user;

    public LandlordConsole(User u, Branch branch, Scanner scanner,
                            BufferedReader reader, PropertyManager pm) {
        super(new String[] {
            "add property",
            "view my properties",
            "view applications",
            "accept application",
            "log out"
        }, branch, scanner, reader, pm);
        user = (Landlord)u;
    }

    public User getUser() {
        return user;
    }

    public void addProperty() throws Exception {
        System.out.println("Enter address: ");
        String address = getLine();
        System.out.print("Enter suburb: ");
        String suburb = scanner.next();
        System.out.print("Enter type (House/Unit/Flat/Townhouse/Studio): ");
        String type = scanner.next();
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        System.out.println("Enter capacity,");
        System.out.print("Enter number of bedrooms: ");
        cap.put("bedroom", scanner.nextInt());
        System.out.print("Enter number of bathrooms: ");
        cap.put("bathroom", scanner.nextInt());
        System.out.print("Enter number of car spaces: ");
        cap.put("car space", scanner.nextInt());
        System.out.print("Enter weekly rental: ");
        double rental = scanner.nextDouble();
        System.out.print("Enter desired contract duration (number of months): ");
        int duration = scanner.nextInt();

        RentalProperty rentalProperty = new RentalProperty(address, suburb, cap, type, rental,
                                            duration, user);
        branch.addProperty(rentalProperty);
        System.out.println("Property successfully added.");
    }

    public void viewMyProperties() {
        for (RentalProperty rp : branch.getOwnedRentalProperty(user)) {
            System.out.printf(
                "%s %s %s\n",
                rp.getId(), rp.getSuburb(), rp.getStatusS()
            );
        }
    }

    public void viewApplications() throws Exception{
        RentalProperty rp = (RentalProperty)getPropertyById();
        for (Application a : rp.getApplications()) {
            System.out.printf(
                "%s %s\n",
                a.getId(), a.getStatusS()
            );
        }
    }

    public void acceptApplication() throws Exception {
        RentalProperty rp = (RentalProperty)getPropertyById();
        System.out.print("Enter application id: ");
        String aid = scanner.next();
        Application a = rp.getApplicationById(aid);
        rp.acceptApplication(a);
        System.out.println("Application accepted!");
    }

    public void console() throws Exception{
        super.console();
        while (true) {
            try {
                String option = displayMenu();
                if (option == "add property")
                    addProperty();
                else if (option == "view my properties")
                    viewMyProperties();
                else if (option == "view applications")
                    viewApplications();
                else if (option == "accept application")
                    acceptApplication();
                else
                    break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}