package hourSubmission;

import user.employee.Employee;

public class HourSubmission{
	private int id;
	private  int hourSubmission;
	private int year;
	private int month;
	//private String status;
	private boolean isApproved;
	private boolean isPending;
	private boolean isRejected;
	
	
	public boolean isPending() {
		return isPending;
	}


	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}


	public boolean isRejected() {
		return isRejected;
	}


	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}	

	/*public void setStatus(String status) {
		this.status = status;
	}*/

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public void setHourSubmission(int hourSubmission) {
		this.hourSubmission = hourSubmission;
	}

	public HourSubmission(int id, int hourSubmission,int year,int month,String status) {
		this.id = id;
		this.hourSubmission = hourSubmission;
		this.year = year;
		this.month = month;
		isApproved = false;
		isPending = false;
		isRejected = false;
		//this.status = "pending";
	}
	
	public int getHourSubmission() {
		return hourSubmission;
	}


	
}
	
	


