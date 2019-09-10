package console;

import java.util.*;
import java.time.*;
import java.io.*;

import se.*;
import property.*;
import user.employee.*;

public class BaseConsole {
    private String[] menuOptions;
    protected Scanner scanner;
    protected Branch branch;
    protected BufferedReader reader;
    protected PropertyManager pm;

    public BaseConsole(String[] menuOptions, Branch branch, Scanner scanner,
                        BufferedReader reader, PropertyManager pm) {
        this.menuOptions = menuOptions;
        this.scanner = scanner;
        this.branch = branch;
        this.reader = reader;
        this.pm = pm;
    }

    public String displayMenu() throws Exception{
        System.out.println("=================================");
        for (int i = 0; i < menuOptions.length; i++) {
            System.out.printf("%-30d: %s\n", i+1, menuOptions[i]);
        }
        System.out.print("Enter your choice: ");
        int op = scanner.nextInt();
        if (op < 1 || op > menuOptions.length)
            throw new Exception("Your choice must be a number displayed on the menu.");
        return menuOptions[op - 1];
    }

    public Property getPropertyById() throws Exception {
        System.out.print("Enter property id: ");
        String pid = scanner.next();
        Property p = branch.getPropertyById(pid);
        return p;
    }

    // public void viewPropertyDetail() throws Exception {
        // Property p = getPropertyById();
    //     System.out.printf(
    //         "%s %s %s %s %d %d %d",

    //     );
    // }

    public String getLine() throws IOException{
        String fs;
        fs = reader.readLine().trim();
        return fs;
    }
}