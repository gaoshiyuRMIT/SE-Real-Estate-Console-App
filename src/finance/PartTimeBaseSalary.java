package finance;

import consts.*;
import user.employee.*;

public class PartTimeBaseSalary extends PayrollItem{
    private static final String idPrefix = "PTBS";

    private int nHour;
    private HoursStatus status;
    private BranchManager branchManager;

    public PartTimeBaseSalary(int nHour, Employee employee) {
        super(employee, idPrefix);
        this.nHour = nHour;
        status = HoursStatus.Pending;
        branchManager = null;
    }

    public static String formatId(Employee e) {
        return PayrollItem.formatId(idPrefix, e);
    }

    public HoursStatus getStatus() {
        return status;
    }

    public void approve(BranchManager bm) {
        status = HoursStatus.Approved;
        branchManager = bm;
        this.setAmount(nHour * this.getEmployee().getHourlySalary());
    }

    public void reject(BranchManager bm) {
        status = HoursStatus.Rejected;
        branchManager = bm;
    }

    public boolean isPending() {
        return status == HoursStatus.Pending;
    }
}