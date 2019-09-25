package user.employee;

import consts.*;
import hourSubmission.HourSubmission;

public class BranchManager extends Employee {
	private int submitid;
	private boolean approve;
	
    public BranchManager(String email, String password) {
        super(email, password, EmployeeType.BranchManager);
    }

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}
}