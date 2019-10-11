package finance;

import java.time.*;

import user.employee.*;

public class PayrollItem {
    private static int idCounter = 0;

    private String id;

    // only year & month are useful information
    private LocalDateTime date;
    private double amount;


	private boolean paid;
    private Employee employee;

    public PayrollItem(Employee employee, String idPrefix) {
        this(employee, idPrefix, 0);
    }

    public PayrollItem(Employee employee, String idPrefix, double amount) {
        this.employee = employee;
        paid = false;
        amount = 0;
        LocalDateTime now = LocalDateTime.now();
        date = LocalDateTime.of(now.getYear(), now.getMonth(), 0, 0, 0);
        id = String.format("%s-%s", idPrefix, employee.getId());
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public double setAmount(double a) {
        return amount = a;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDateTime getDate() {
        return date;
    }
    
    public double getAmount() {
		return amount;
	}
}