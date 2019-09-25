package people;

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
import hourSubmission.HourSubmission;
import user.*;
import user.customer.*;
import user.employee.*;
import property.*;
import se.Application;
import se.Branch;

public class TestEmployee {
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
    private HourSubmission[] hs;
    
   
	@Rule
    public ExpectedException thrown = ExpectedException.none();
    
     @Before
    public void setUp() {
        branch = new Branch("Melbourne");
        setUpEmployees();
    }
    
    //test hour submission is positive number
    @Test
    public void testHourSubmission() throws Exception{
    	branch.addEmployee(consultants[0]);
        assertTrue(consultants[0].addHourSubmission(20) >= 0);
        assertNotNull(consultants[0].addHourSubmission(20));
    }
    
    
	//check if the part time and base salary set success
    @Test
    public void testPartTimerSalary() throws Exception {
        
    	//branch.addEmployee(brchMgrs[0]);
   	
    	branch.addEmployee(consultants[0]);
    	consultants[0].setHourlySalary(50);
    	//consultants[0].calculateBaseSalary(5);
    	double s = consultants[0].getBaseSalary();
        assertNotNull(s);
        List<HourSubmission> hsub = consultants[0].getHoursSubmitted();
        assertNotNull(hsub);
        assertEquals(((HourSubmission) hsub).getHourSubmission(),50);
       // assertEquals(consultants[0].addHourSubmission(5)*consultants[0].getHourlySalary(), 250);
    }   

	/*
	 * check if the status change 
	 * when they get the branch manager approval 
	 */
    @Test
    public void testStatus() throws Exception {        
    	hs = new HourSubmission[] {
    			new HourSubmission(1,1,2019,9,"pending"),    			
    	};	
    	branch.addEmployee(brchMgrs[0]);
    	branch.addEmployee(consultants[0]);
    	
    	//consultants[0].setIsParttimer(true);
    	brchMgrs[0].setApprove(true);    
    	
    	thrown.expect(WrongStatusException.class);     	
    	thrown.expectMessage("Approve failed");   	
    }  
   //check if branch account is reduce correctly
  //  @Test
    
    //check an hour submission must have a status and not same
    @Test
    public void testHourSubmissionStatus() throws Exception {
    	branch.addEmployee(brchMgrs[0]);

        thrown.expect(InvalidParamException.class);
        thrown.expectMessage("status error")
        ;
        HourSubmission hs = new HourSubmission(
            1, 1, 300, 5, "status error"
        );
        assertEquals(hs.isApproved(),hs.isPending(),hs.is)
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
            new Employee("thomas.d@serealestate.com", "555555", EmployeeType.BranchAdmin)
        };
    }
    
}
