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

public class BranchManagerConsole extends BaseConsole {
    private BranchManager user;

    public BranchManagerConsole(User user, BaseConsole base) {
        super(new String[] {
            "view newly-added properties",
            "list property on market",
            "log out"
        }, base);
        this.user = (BranchManager)user;
    }

    public void viewNewlyAddedProperties() {
        System.out.println("======== Newly Added Properties ========");
        (new PropertyListConsole(user, branch.getNewlyAddedProperties(), this)).console();
    }

    public void listPropertyOnMarket() throws InternalException, InvalidInputException{
        Property p = getPropertyById();
        if (p instanceof RentalProperty)
            ((RentalProperty)p).setManager(pm);
        else
            throw new InternalException("not implemented");
        try {
            p.list();
        } catch (OperationNotAllowedException e) {
            throw new InternalException(e);
        }
        System.out.println("Successfully listed property.");
    }

    public User getUser() {
        return user;
    }


    public void console() {
        super.console();
        while (true) {
            try {
                String option = displayMenu();
                if (option.equals("view newly-added properties"))
                    viewNewlyAddedProperties();
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