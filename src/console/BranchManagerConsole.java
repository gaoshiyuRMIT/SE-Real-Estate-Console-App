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
            "log out"
        }, base);
        this.user = (BranchManager)user;
    }

    public void viewNewlyAddedProperties() {
        (new PropertyListConsole(user, branch.getNewlyAddedProperties(), this)).console();
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
                else
                    break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}