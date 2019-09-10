package console;

import java.io.*;

import java.util.*;
import java.time.*;

import se.*;
import user.*;
import user.employee.*;
import user.customer.*;

public class Console extends BaseConsole {
    public Console() {
        super(
            new String[] {
                "register",
                "log in",
                "exit"
            },
            new Branch("Test"),
            new Scanner(System.in),
            new BufferedReader(new InputStreamReader(System.in)),
            new PropertyManager("Emmy.de.propertymanager@gmail.com", "123")
        );
        branch.addEmployee(pm);
        BranchManager bm = new BranchManager("Abe.de.branchmanager@gmail.com", "123");
        branch.addEmployee(
            bm
        );
        System.out.println("branch manager " + bm.getId());
        System.out.println("property manager " + pm.getId());
    }

    public void register() throws Exception {
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        System.out.printf("Enter role (Landlord/Tenant/Vendor/Buyer): ");
        String role = scanner.next();
        String cid = branch.register(email, password, role);
        System.out.printf("Registration successful! Your user id is %s.\n", cid);
    }

    public void login() throws Exception {
        System.out.print("Enter user id: ");
        String uid = scanner.next();
        System.out.print("Enter password: ");
        String pswd = scanner.next();
        User u = branch.login(uid, pswd);
        if (u == null)
            throw new Exception("User id does not exist.");
        if (u instanceof BranchManager)
            (new BranchManagerConsole(u, branch, scanner, reader, pm)).console();
        else if (u instanceof Landlord)
            (new LandlordConsole(u, branch, scanner, reader, pm)).console();
        else if (u instanceof Tenant)
            (new TenantConsole(u, branch, scanner, reader, pm)).console();
    }

    public static void main(String[] args) throws Exception{
        Console console = new Console();
        while (true) {
            // try {
                String option = console.displayMenu();
                if (option == "register")
                    console.register();
                else if (option == "log in")
                    console.login();
                else
                    break;
            // } catch (Exception e) {
            //     System.out.println(e.getMessage());
            // }
        }
    }
}