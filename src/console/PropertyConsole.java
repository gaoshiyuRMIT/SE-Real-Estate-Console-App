package console;

import java.util.*;

import user.*;
import user.customer.*;
import user.employee.*;
import property.*;
import se.*;
import exception.*;

public class PropertyConsole extends BaseConsole {
    private User user;
    private Property property;

    public PropertyConsole(User user, Property property, BaseConsole base) {
        super(base);
        this.user = user;
        this.property = property;
        List<String> menu = new ArrayList<String>();
        menu.add("view property detail");
        menu.add("back");
        if (!(user instanceof NonOwner)) {
            if (property instanceof RentalProperty) {
                menu.add(1, "view applications");
                menu.add(2, "view leases");
                if (user instanceof Landlord) {
                    menu.add(3, "edit rental and duration");
                }
            } else {
                menu.add(1, "view purchase offers");
                if (user instanceof Vendor) {
                    menu.add(2, "edit minimum price");
                }
            }
            if (user instanceof Customer) {
                menu.add(1, "edit property detail");
            }
            if (user instanceof BranchManager) {
                menu.add(1, "list property on market");
            }
        }
        setMenuOptions(menu);
    }

    public void viewPropertyDetail() {
        System.out.println(util.getPageBreak("Property Detail"));
        System.out.println(property.getTextualDetail());
        if (!(user instanceof NonOwner)) {
            if (property instanceof RentalProperty) {
                System.out.printf(
                    "%-30s: %.2f\n",
                    "management fee rate", ((RentalProperty)property).getManagementFeeRate()
                );
            } else if (property instanceof ForSaleProperty) {
                System.out.printf(
                    "%-30s: %.2f\n",
                    "commission rate", ((ForSaleProperty)property).getCommissionRate()
                );
            }

        }
    }

    public void viewApplications() {
        RentalProperty rentalProperty = (RentalProperty)property;
        (new ApplicationListConsole(user, rentalProperty.getApplications(), this)).console();
    }

    public void listPropertyOnMarket() throws InvalidInputException, InternalException{
        if (property instanceof RentalProperty) {
            List<PropertyManager> lpm = branch.getAllPropertyManagers();
            System.out.println(util.getPageBreak("All Property Managers"));
            for (PropertyManager pm : lpm) {
                System.out.printf("id %s email %s\n", pm.getId(), pm.getEmail());
            }
            System.out.println(util.getPageBreak());

            System.out.println("Please assign a property manager to the rental property.");
            Employee manager = getEmployeeById(lpm);
            ((RentalProperty)property).setManager((PropertyManager)manager);
            try {
                property.list();
            } catch (OperationNotAllowedException e) {
                throw new InternalException(e);
            }
        } else if (property instanceof ForSaleProperty) {
            List<SalesConsultant> lsc = branch.getAllSaleConsultants();
            System.out.println(util.getPageBreak("All Sales Consultants"));

            for (SalesConsultant sc : lsc) {
                System.out.printf("id %s email %s\n", sc.getId(), sc.getEmail());
            }
            System.out.println(util.getPageBreak());

            System.out.println("Please assign a sales consultant to the for-sale property.");
            Employee consultant = getEmployeeById(lsc);
            ((ForSaleProperty)property).setConsultant((SalesConsultant)consultant);
            try {
                property.list();
            } catch (OperationNotAllowedException e) {
                throw new InternalException(e);
            }
        }
        branch.sendNotifForListedProperty(property);
        System.out.println("Successfully listed property.");
    }

    public void editRentalAndDuration() throws InvalidInputException, InternalException {
        viewPropertyDetail();
        System.out.println("Fill in the fields you want to change (leave blank if no change):");
        System.out.println("Enter weekly rental: ");
        try {
            ((RentalProperty)property).setWeeklyRental(getDouble());
        } catch (EmptyInputException e) {}
        catch (OperationNotAllowedException e) {
            throw new InvalidInputException(e);
        }
        System.out.println("Enter desired contract duration (number of months): ");
        try {
            ((RentalProperty)property).setDuration(getInt());
        } catch (EmptyInputException e) {}
        System.out.println("Change saved.");
    }

    public void editPropertyDetail() throws InvalidInputException, InternalException{
        viewPropertyDetail();
        System.out.println("Fill in the fields you want to change (leave blank if no change):");
        System.out.println("Enter address: ");
        String address = getLine();
        if (!address.isEmpty())
            property.setAddress(address);
        System.out.println("Enter suburb: ");
        String suburb = getLine();
        if (!suburb.isEmpty())
            property.setSuburb(suburb);
        System.out.println("Enter type (House/Unit/Flat/Townhouse/Studio): ");
        String type = getLine();
        if (!type.isEmpty())
            property.setPropertyType(type);
        HashMap<String, Integer> cap = property.getCapacity();
        System.out.println("Enter capacity,");
        System.out.println("Enter number of bedrooms: ");
        try {
            cap.put("bedroom", getInt());
        } catch (EmptyInputException e) {}
        System.out.println("Enter number of bathrooms: ");
        try {
            cap.put("bathroom", getInt());
        } catch (EmptyInputException e) {}
        System.out.println("Enter number of car spaces: ");
        try {
            cap.put("car space", getInt());
        } catch (EmptyInputException e) {}
        System.out.println("Change saved.");
    }


    public Employee getEmployeeById(List<? extends Employee> l) throws InvalidInputException{
        System.out.print("Enter employee id: ");
        String eid = scanner.next();
        for (Employee e : l) {
            if (e.getId().equals(eid)) {
                return e;
            }
        }
        throw new InvalidInputException("Specified employee is not in the list above.");
    }

    public void console() {
        while (true) {
            System.out.println(util.getPageBreak(
                String.format("Property %s %s %s", property.getId(),
                                property.getSuburb(), property.getStatusS())
            ));
            try {
                String option = displayMenu();
                if (option.equals("view property detail"))
                    viewPropertyDetail();
                else if (option.equals("view applications"))
                    viewApplications();
                else if (option.equals("list property on market"))
                    listPropertyOnMarket();
                else if (option.equals("edit property detail"))
                    editPropertyDetail();
                else if (option.equals("edit rental and duration"))
                    editRentalAndDuration();
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

