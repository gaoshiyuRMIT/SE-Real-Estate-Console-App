package console;

import java.io.*;
import java.util.*;

import java.time.*;

import se.*;
import exception.*;
import user.*;
import user.employee.*;
import user.customer.*;
import property.*;

public class LandlordConsole extends BaseConsole {
    private Landlord user;

    public LandlordConsole(User u, BaseConsole base) {
        super(new String[] {
            "add property",
            "view my properties",
            "log out"
        }, base);
        user = (Landlord)u;
    }


    public User getUser() {
        return user;
    }

    public void addProperty() throws InvalidInputException, InternalException {
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
        RentalProperty rentalProperty;
        try {
            rentalProperty = new RentalProperty(address, suburb, cap, type, rental,
                                                duration, user);
        } catch (InvalidParamException e) {
            throw new InvalidInputException(e);
        }
        branch.addProperty(rentalProperty);
        System.out.println("Property successfully added.");
    }

    public void viewMyProperties() {
        List<Property> pl = branch.getProperties(user);
        (new PropertyListConsole(user, pl, this)).console();
    }


    @Override
    public Property getPropertyById() throws InvalidInputException {
        Property p = super.getPropertyById();
        if (!p.getOwner().equals(user))
            throw new InvalidInputException(
                "Property specified does not belong to user."
            );
        return p;
    }

    public void console(){
        super.console();
        while (true) {
            try {
                String option = displayMenu();
                if (option.equals("add property"))
                    addProperty();
                else if (option.equals( "view my properties"))
                    viewMyProperties();
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