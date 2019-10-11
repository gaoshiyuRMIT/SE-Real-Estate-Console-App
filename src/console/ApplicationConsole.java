package console;

import java.util.*;

import consts.*;
import user.*;
import user.employee.*;
import user.customer.*;
import se.*;
import exception.*;
import property.*;

public class ApplicationConsole extends BaseConsole {
    private User user;
    private ApplicationBase application;
    private Property property;

    public ApplicationConsole(User user, ApplicationBase application, BaseConsole base)
                                throws InternalException{
        super(base);
        try {
            this.property = branch.getPropertyById(application.getPropertyId());
        } catch (InvalidParamException e) {
            throw new InternalException(e);
        }
        this.user = user;
        this.application = application;
        List<String> menu = new ArrayList<String>();
        menu.add("view detail");
        menu.add("back");
        if (user instanceof Customer && !(user instanceof NonOwner)) {
            if (application.isPending()) {
                menu.add(1, "accept");
                menu.add(2, "reject");
            }
        } else if (user instanceof NonOwner) {
            if (application.isPending() || application.isAwaitingPayment()) {
                menu.add(1, "withdraw");
            }
        } else if (user instanceof Employee
                    && ((Employee)user).getRole() == EmployeeType.BranchAdmin) {
            if (application instanceof Application)
                menu.add(1, "pay rent and bond");
            else if (application instanceof PurchaseOffer)
                menu.add(1, "pay deposit");
        }
        setMenuOptions(menu);
    }

    public void viewDetail() {
        System.out.printf("====== Property %s %s, Application %s =======\n",
                            property.getId(), property.getSuburb(),
                            application.getId());
        System.out.println(application.getTextualDetail());
    }

    public void accept() throws InternalException{
        try {
            if (property instanceof RentalProperty) {
                if (!(application instanceof Application))
                    throw new InternalException(
                        "properyt type inconsistent with application type"
                    );
                ((RentalProperty)property).acceptApplication((Application)application);
            } else {
                if (!(application instanceof PurchaseOffer))
                    throw new InternalException(
                        "properyt type inconsistent with application type"
                    );
                ((ForSaleProperty)property).acceptApplication((PurchaseOffer)application);
            }
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
    }

    public void reject() throws InternalException{
        try {
            if (property instanceof RentalProperty) {
                if (!(application instanceof Application))
                    throw new InternalException(
                        "properyt type inconsistent with application type"
                    );
                ((RentalProperty)property).rejectApplication((Application)application);
            } else {
                if (!(application instanceof PurchaseOffer))
                    throw new InternalException(
                        "properyt type inconsistent with application type"
                    );
                ((ForSaleProperty)property).rejectApplication((PurchaseOffer)application);
            }
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
    }

    public void withdraw() throws InternalException{
        try {
            if (property instanceof RentalProperty) {
                if (!(application instanceof Application))
                    throw new InternalException(
                        "properyt type inconsistent with application type"
                    );
                ((RentalProperty)property).withdrawApplication((Application)application);
            } else {
                if (!(application instanceof PurchaseOffer))
                    throw new InternalException(
                        "properyt type inconsistent with application type"
                    );
                ((ForSaleProperty)property).withdrawApplication((PurchaseOffer)application);
            }
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
    }

    public void payRentAndBond() throws InternalException{
        if (!(property instanceof RentalProperty)
                || !(application instanceof Application)) {
            throw new InternalException(
                "The operation 'pay rent and bond' is inconsistent "
                    + "with property type and application type."
            );
        }
        try {
            ((RentalProperty)property).payRentBondForApplication((Application)application);
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
    }

    public void payDeposit() throws InternalException{
        if (!(property instanceof ForSaleProperty)
                || !(application instanceof PurchaseOffer)) {
            throw new InternalException(
                "The operation 'pay deposit' is inconsistent "
                    + "with property type and application type."
            );
        }
        try {
            ((ForSaleProperty)property).payDepositForApplication((PurchaseOffer)application);
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
    }

    public void console() {
        System.out.printf("Application %s by %s, %s\n", application.getId(),
                            application.getInitiator().getId(),
                            application.getStatusS());
        while (true) {
            try {
                String option = displayMenu();
                if (option.equals("view detail"))
                    viewDetail();
                else if (option.equals("accept"))
                    accept();
                else if (option.equals("reject"))
                    reject();
                else if (option.equals("withdraw"))
                    withdraw();
                else if (option.equals("pay rent and bond"))
                    payRentAndBond();
                else if (option.equals("pay deposit"))
                    payDeposit();
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