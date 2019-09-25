package finance;

public class FullTimeBaseSalary extends PayrollItem{
    private static final String idPrefix = "FTBS";

    public FullTimeBaseSalary(Employee employee) {
        super(employee, idPrefix, employee.getHourlySalary() * Employee.maxMonthlyHours);
    }
}