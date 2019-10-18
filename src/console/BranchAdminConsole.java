package console;

import java.util.*;
import java.time.*;

import user.*;
import user.employee.*;
import user.customer.*;
import exception.*;

public class BranchAdminConsole extends BaseConsole {
    private Employee user;

    public BranchAdminConsole(User user, BaseConsole base) {
        super(new String[] {
            "browse tenants",
            "view payout to employees",
            "view payout to landlords",
            "prepare payout to employees",
            "prepare payout to landlords",
            "run payroll",
            "log out"
        }, base);
        this.user = (Employee)user;
    }

    public User getUser() {
        return user;
    }

    public void preparePayoutToEmployees() throws InvalidInputException,
                                                    InternalException{
        LocalDateTime date = getYearMonth();
        branch.prepareEmployeePayOut(date);
    }

    public LocalDateTime getYearMonth()  throws InvalidInputException,
                                                    InternalException {
        LocalDateTime date = LocalDateTime.now();
        System.out.printf("Enter year (%d)\n", date.getYear());
        int y;
        try {
            y = getInt();
        }catch (EmptyInputException e) {
            y = date.getYear();
        }
        System.out.printf("Enter month (%d)\n", date.getMonthValue());
        int m;
        try {
            m = getInt();
        } catch (EmptyInputException e) {
            m = date.getMonthValue();
        }
        return LocalDateTime.of(y, Month.of(m), 1, 0, 0);
    }

    public void preparePayoutToLandlords() throws InternalException{
        try {
            branch.prepareLandlordPayout();
        }catch (InsufficientBalanceException e) {
            throw new InternalException(e);
        }
    }

    public void runPayroll() throws InvalidInputException, InternalException{
        LocalDateTime date = getYearMonth();
        try {
            branch.runPayroll(date);
        }catch (InsufficientBalanceException e) {
            throw new InternalException(e);
        }
    }

    public void viewPayoutToEmployees()  {
        HashMap<Employee, Double> payout = branch.getPreparedEmployeePayout();
        for (Employee e : payout.keySet()) {
            System.out.printf("Employee %-20s: %.2f\n", e.getId(), payout.get(e));
        }
    }

    public void viewPayoutToLandlords() {
        HashMap<Landlord, Double> payout = branch.getPreparedLandlordPayout();
        for (Landlord l : payout.keySet()) {
            System.out.printf("Landlord %-20s: %.2f\n", l.getId(), payout.get(l));
        }
    }

    public void browseTenants() {
        System.out.print("Enter ID type (Passport/DriverLicence): ");
        String idType = scanner.next();
        System.out.print("Enter ID number: ");
        String idContent = scanner.next();
        ID id = new ID(idType, idContent);
        List<NonOwner> res = branch.getNonOwnerWithApplicant(id);
        List<Tenant> tenants = new ArrayList<Tenant>();
        for (NonOwner no : res) {
            if (no instanceof Tenant)
                tenants.add((Tenant)no);
        }
        System.out.println(util.getPageBreak("Tenants"));
        for (Tenant t : tenants) {
            System.out.println(t.getId());
        }
    }

    public void console() {
        super.console();
        while (true) {
            try {
                String option = displayMenu();
                if (option.equals("prepare payout to employees"))
                    preparePayoutToEmployees();
                else if (option.equals("prepare payout to landlords"))
                    preparePayoutToLandlords();
                else if (option.equals("run payroll"))
                    runPayroll();
                else if (option.equals("view payout to employees"))
                    viewPayoutToEmployees();
                else if (option.equals("view payout to landlords"))
                    viewPayoutToLandlords();
                else if (option.equals("browse tenants"))
                    browseTenants();
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
