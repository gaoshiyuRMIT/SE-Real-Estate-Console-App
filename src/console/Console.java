package console;

import java.io.*;

import java.util.*;
import java.time.*;

import se.*;
import exception.*;
import user.*;
import user.employee.*;
import user.customer.*;
import consts.*;
import console.util.*;

public class Console extends BaseConsole {
    public Console() throws Exception{
        super(
            new String[] {
                "register",
                "log in",
                "exit"
            },
            new Branch("Test"),
            new SEScanner(System.in),
            new BufferedReader(new InputStreamReader(System.in)),
            new PropertyManager("Emmy.de.propertymanager@gmail.com", "123")
        );
        branch.addEmployee(pm);
        BranchManager bm = new BranchManager("Abe.de.branchmanager@gmail.com", "123");
        Employee admin = new Employee("Carter.de.branchadmin@gmail.com", "123",
                                        EmployeeType.BranchAdmin);
        branch.addEmployee(bm);
        branch.addEmployee(admin);
        User landlord = branch.login(branch.register("landlord@gmail.com", "123", "Landlord"), "123");
        User tenant = branch.login(branch.register("tenant@gmail.com", "123", "Tenant"), "123");
        System.out.println("landlord " + landlord.getId());
        System.out.println("tenant " + tenant.getId());
        System.out.println("branch manager " + bm.getId());
        System.out.println("property manager " + pm.getId());
        System.out.println("branch admin " + admin.getId());
    }

    public void register() throws InvalidInputException {
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        System.out.printf("Enter role (Landlord/Tenant/Vendor/Buyer): ");
        String role = scanner.next();
        String cid;
        try {
            cid = branch.register(email, password, role);
        } catch (CustomerExistException | InvalidParamException e) {
            throw new InvalidInputException(e);
        }
        System.out.printf("Registration successful! Your user id is %s.\n", cid);
    }

    public void login() throws InvalidInputException {
        System.out.print("Enter user id: ");
        String uid = scanner.next();
        System.out.print("Enter password: ");
        String pswd = scanner.next();
        User u = branch.login(uid, pswd);
        if (u == null)
            throw new InvalidInputException("Combination of user id and password does not exist.");
        if (u instanceof BranchManager)
            (new BranchManagerConsole(u, this)).console();
        else if (u instanceof Landlord)
            (new LandlordConsole(u, this)).console();
        else if (u instanceof Tenant)
            (new TenantConsole(u, this)).console();
        else if (u instanceof Employee && ((Employee)u).getRole() == EmployeeType.BranchAdmin)
            (new BranchAdminConsole(u, this)).console();
    }

    public static void main(String[] args) throws InternalException{
        Console console;
        try {
            console = new Console();
        } catch (Exception e) {
            throw new InternalException(e);
        }
        while (true) {
            try {
                String option = console.displayMenu();
                if (option.equals("register"))
                    console.register();
                else if (option.equals("log in"))
                    console.login();
                else
                    break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}