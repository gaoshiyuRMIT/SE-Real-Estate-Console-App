package console;

import java.io.*;
import java.util.*;
import java.time.*;

import se.*;
import user.*;
import user.employee.*;
import user.customer.*;
import property.*;

public class BranchManagerConsole extends BaseConsole {
    private BranchManager user;

    public BranchManagerConsole(User user, Branch branch, Scanner scanner,
                                BufferedReader reader, PropertyManager pm) {
        super(new String[] {
            "view newly-added properties",
            "list property on market",
            "log out"
        }, branch, scanner, reader, pm);
        this.user = (BranchManager)user;
    }

    public void viewNewlyAddedProperties() {
        for (Property p : branch.getNewlyAddedProperties()) {
            System.out.printf(
                "%s %s %s\n",
                p.getId(), p.getSuburb(), p.getClass().getSimpleName()
            );
        }
    }

    public void listPropertyOnMarket() throws Exception{
        Property p = getPropertyById();
        if (p instanceof RentalProperty)
            ((RentalProperty)p).setManager(pm);
        else
            throw new Exception("not implemented");
        p.list();
        System.out.println("Successfully listed property.");
    }

    public User getUser() {
        return user;
    }


    public void console() throws Exception{
        super.console();
        while (true) {
            // try {
                String option = displayMenu();
                if (option.equals("view newly-added properties"))
                    viewNewlyAddedProperties();
                else if (option.equals("list property on market"))
                    listPropertyOnMarket();
                else
                    break;
            // } catch (Exception e) {
            //     System.out.println(e.getMessage());
            // }
        }
    }
}