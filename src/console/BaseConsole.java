package console;

import java.util.*;
import java.time.*;
import java.io.*;

import exception.*;
import se.*;
import user.*;
import property.*;
import user.employee.*;

import console.util.*;

public abstract class BaseConsole {
    private List<String> menuOptions;
    protected SEScanner scanner;
    protected Branch branch;
    protected BufferedReader reader;
    protected PropertyManager pm;
    protected Util util;

    public BaseConsole(List<String> menuOptions, Branch branch, SEScanner scanner,
                        BufferedReader reader, PropertyManager pm) {
        this.menuOptions = menuOptions;
        this.scanner = scanner;
        this.branch = branch;
        this.reader = reader;
        this.pm = pm;
        this.util = new Util(scanner);
    }

    public BaseConsole(String[] menuOptions, Branch branch, SEScanner scanner,
                        BufferedReader reader, PropertyManager pm) {
        this(menuOptions != null ? Arrays.asList(menuOptions) : null, branch, scanner, reader, pm);
    }

    public BaseConsole(String[] menuOptions, BaseConsole base) {
        this(menuOptions, base.branch, base.scanner, base.reader, base.pm);
    }

    public BaseConsole(BaseConsole base) {
        this(null, base);
    }

    public int getInt() throws InternalException, InvalidInputException,
                                EmptyInputException{
        String s = getLine();
        if (s.isEmpty())
            throw new EmptyInputException();
        int res;
        try {
            res = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("A whole number/integer is required.");
        }
        return res;
    }

    public double getDouble()  throws InternalException, InvalidInputException,
                                        EmptyInputException{
        String s = getLine();
        if (s.isEmpty())
            throw new EmptyInputException();
        double res;
        try {
            res = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("A whole number/integer is required.");
        }
        return res;
    }

    public void setMenuOptions(String[] menuOptions) {
        setMenuOptions(Arrays.asList(menuOptions));
    }

    public void setMenuOptions(List<String> menuOptions) {
        this.menuOptions = menuOptions;
    }

    public String displayMenu() throws InvalidInputException{
        System.out.println(util.getPageBreak("Menu"));
        return util.displayMenu(menuOptions);
    }

    public Property getPropertyById() throws InvalidInputException {
        System.out.print("Enter property id: ");
        String pid = scanner.next();
        Property p;
        try {
            p = branch.getPropertyById(pid);
        } catch (InvalidParamException e) {
            throw new InvalidInputException(e);
        }
        return p;
    }

    public User getUser() {return null;};

    public String getLine() throws InternalException{
        String fs;
        try {
            fs = reader.readLine();
        } catch (IOException e) {
            throw new InternalException(e);
        }
        if (fs != null)
            return fs.trim();
        return fs;
    }

    public void console() {
        User user = getUser();
        System.out.printf("\nWelcome back, %s\n",
                            user == null ? "User" : user.getClass().getSimpleName());

    }
}