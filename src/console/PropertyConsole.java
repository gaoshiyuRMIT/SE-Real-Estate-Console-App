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
        System.out.println("===== property detail =======");
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
        System.out.println("======== Applications =========");
        (new ApplicationListConsole(user, rentalProperty.getApplications(), this)).console();
    }

    public void listPropertyOnMarket() throws InvalidInputException, InternalException{
        if (property instanceof RentalProperty) {
            List<PropertyManager> lpm = branch.getAllPropertyManagers();
            System.out.println("====== All Property Managers ======");
            for (PropertyManager pm : lpm) {
                System.out.printf("id %s email %s\n", pm.getId(), pm.getEmail());
            }
            System.out.println("==================================");

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
            System.out.println("====== All Sales Consultants ======");
            for (SalesConsultant sc : lsc) {
                System.out.printf("id %s email %s\n", sc.getId(), sc.getEmail());
            }
            System.out.println("==================================");

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
            System.out.printf("Property %s %s %s\n", property.getId(),
                                property.getSuburb(), property.getStatusS());
            try {
                String option = displayMenu();
                if (option.equals("view property detail"))
                    viewPropertyDetail();
                else if (option.equals("view applications"))
                    viewApplications();
                else if (option.equals("list property on market"))
                    listPropertyOnMarket();
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