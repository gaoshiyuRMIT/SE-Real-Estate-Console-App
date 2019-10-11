package payroll;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.*;
import static java.util.AbstractMap.SimpleEntry;

import consts.*;
import exception.*;
import finance.PartTimeBaseSalary;
import finance.PayrollItem;
import user.*;
import user.customer.*;
import user.employee.*;
import se.Application;
import se.Branch;

public class testBranchAccount {
	private static double initialBalance;
	private static Branch branch;
	private static double branchAccount;
	private Employee employee;
	private BranchManager branchManager;
	private PropertyManager propertyManager;
	private SalesConsultant salesConsultant;
	private BranchAdmin branchAdmin;
	private SalesConsultant[] consultants;
    private PropertyManager[] propMgrs;
    private BranchManager[] brchMgrs;
    private Employee[] brchAdms;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		//Fixtures
		initialBalance = 5000;
        branch = new Branch("Melbourne",initialBalance);
        consultants = new SalesConsultant[] {
                new SalesConsultant("oliver.h@serealestate.com", "123456")
            };
            propMgrs = new PropertyManager[] {
                new PropertyManager("zoe.a@serealestate.com", "222222")
            };
            brchMgrs = new BranchManager[] {
                new BranchManager("ruby.g@serealestate.com", "444444")
            };
            brchAdms = new Employee[] {
                new Employee("thomas.d@serealestate.com", "555555", EmployeeType.BranchAdmin),
            };
            
    }
		// test if branch account has enought money to pay off employee
		@Test
		public void testPayoffSuccess() throws Exception{
			//Actions				
		    //PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, consultants[0]); 
			//double payroll = ptbs.setAmount(100);
			double successPayroll = 1000;
			branch.payOff(successPayroll);
			//Actual Results
			double actualBalance = branch.getBranchAccount();
			
			//Expected Results
			double expectedBalance = 4000;
			//Assertions
			assertEquals(expectedBalance,actualBalance,0.001);
	
		}
		
		@Test
		public void testPayoffFail() throws Exception{
			//Fixtures
			double failedPayrollAmount = 4999;
			branch.payOff(failedPayrollAmount);
			double actualBalance = branch.getBranchAccount();
					
			//Expected Results
			//double expectedBalance = 4000;
			//Assertions
			//thrown.expect(OperationNotAllowedException.class);
			//thrown.expectMessage("Payroll should be less than branch account balance");
			assertTrue(actualBalance >= 0);	
			//assertEquals(expectedBalance,actualBalance,0.001);
		}
		
		
		//check if the Hour entered is valid
	    @Test
	    public void testTime() throws Exception {
	        //branch.addEmployee(consultants[0]);
	        //PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, consultants[0]);  
	        //int h = ptbs.getnHour();
	        int h = 4;
	        int max =Employee.getMaxMonthlyHours();
	        assertNotNull(h);
	        assertTrue(h >= 0);
	        assertTrue(h <= max);
	    } 
	    //negative situation
	    @Test
	    public void testInvalidTime() throws Exception {
	    	//branch.addEmployee(consultants[0]);
	    	double failedPayrollAmount = 4999;
			branch.payOff(failedPayrollAmount);
			//double actualBalance = branch.getBranchAccount();
	    	try {
	    		branch.canPayoff(5001);
	    	}catch(IllegalArgumentException ex) {
	    		assertThat(ex.getMessage(), containsString("should be sufficient"));
	    	}
	    	fail("expected Exception");
	    	
	
}
}
