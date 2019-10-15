package finance;

import java.time.*;

import user.employee.*;
import util.*;

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
        date = LocalDateTimeUtil.extractMonth(LocalDateTime.now());
        id = formatId(idPrefix, employee);
        this.amount = amount;
    }

    public static String formatId(String idPrefix, Employee employee) {
        return String.format("%s-%s", idPrefix, employee.getId());
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double a) {
        amount = a;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDateTime getDate() {
        return date;
    }
}