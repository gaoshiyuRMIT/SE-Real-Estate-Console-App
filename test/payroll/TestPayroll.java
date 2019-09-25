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
import property.*;
import se.Application;
import se.Branch;

public class TestPayroll {
	private Employee employee;
	private BranchManager branchManager;
	private PropertyManager propertyManager;
	private SalesConsultant salesConsultant;
	private BranchAdmin branchAdmin;
	private SalesConsultant[] consultants;
    private PropertyManager[] propMgrs;
    private BranchManager[] brchMgrs;
    private Employee[] brchAdms;
    private Branch branch;

   
	@Rule
    public ExpectedException thrown = ExpectedException.none();
    
     @Before
    public void setUp() {
        branch = new Branch("Melbourne");
        setUpEmployees();
    }
    
    //test part timer submit the Hour and status should be pending before approve
    @Test
    public void testHourSubmission() throws Exception{
    	branch.addEmployee(consultants[0]);
    	branch.addEmployee(brchMgrs[0]);
    	PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, consultants[0]);
        assertEquals(ptbs.getStatus(), HoursStatus.Pending);      
        ptbs.approve(brchMgrs[0]);
        assertEquals(ptbs.getStatus(),HoursStatus.Approved);
    }
    //once is approved, can't change status anymore
    @Test
    public void testRejected() throws Exception{
    	branch.addEmployee(consultants[0]);
    	branch.addEmployee(brchMgrs[0]);
    	PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, consultants[0]);  
        ptbs.approve(brchMgrs[0]);           
        thrown.expect(OperationNotAllowedException.class);
        ptbs.reject(brchMgrs[0]);
    }
    
    //check if the branch manager is the part-time worker 
    //and approve himself
    @Test
    public void testValidHours() throws Exception{
    	
    	branch.addEmployee(brchMgrs[0]);
    	PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, brchMgrs[0]);  
    	thrown.expect(ApproveNotAllowedException.class);
    	ptbs.approve(brchMgrs[0]);
    	
    }        
    
	//check if the Hour entered is valid
    @Test
    public void testTime() throws Exception {
        branch.addEmployee(consultants[0]);
        PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, consultants[0]);        
        assertNotNull(ptbs.getnHour());
        assertTrue(ptbs.getnHour() >= 0);
        assertTrue(ptbs.getnHour() <= consultants[0].getMaxMonthlyHours());
    }   

	//check if branch account is enough to expense
    @Test
    public void testBranchAccount() throws Exception {        
    	
    	branch.addEmployee(brchAdms[0]);
    	branch.addEmployee(consultants[0]);
    	branch.submitHours(consultants[0], 5);
    	PartTimeBaseSalary ptbs = new PartTimeBaseSalary(5, consultants[0]);
    	ptbs.setAmount(500);
    	branch.setBranchMoney(1000);
    	assertTrue(branch.getBranchMoney() >= ptbs.getAmount());       	   	
    } 
   
    
    public void setUpEmployees(){
        consultants = new SalesConsultant[] {
            new SalesConsultant("oliver.h@serealestate.com", "123456"),
            new SalesConsultant("william.s@serealestate.com", "234123"),
            new SalesConsultant("steph.w@serealestate.com", "111111")
        };
        propMgrs = new PropertyManager[] {
            new PropertyManager("zoe.a@serealestate.com", "222222"),
            new PropertyManager("mia.b@serealestate.com", "333333")
        };
        brchMgrs = new BranchManager[] {
            new BranchManager("ruby.g@serealestate.com", "444444")
        };
        brchAdms = new Employee[] {
            new Employee("thomas.d@serealestate.com", "555555", EmployeeType.BranchAdmin),
            new Employee("ana.h@serealestate.com", "555555", EmployeeType.BranchAdmin, 40)
        };
    }
    
}
