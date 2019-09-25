package salary;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.*;
import java.time.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import se.*;
import exception.*;
import user.customer.*;
import user.employee.*;
import consts.*;
import org.junit.Test;


public class testSalary {
	private Employee e1;

	/*
	 * check if Part-timers are paid a salary that is proportional to the hours they worked
	 * SalesConsultant have extra 40% of commission
	 */
	@Test
	public void testPaidSalary() throws Exception{
		PropertyManager pm1 = new PropertyManager("pm1@gmail.com","pswd");
		BranchManager bm1 = new BranchManager("bm1@gmail.com","pswd");
		SalesConsultant sc1 = new SalesConsultant("sc1@gmail.com","pswd");
		assertEquals(bm1.getBaseSalary(), bm1.getHours()*Employee.getHourlySalary()+0.4*commissionReceived);
		
	/*
	 * check if the status change 
	 * when they get the branch manager approval 
	 */
	 @Test
	 public void testPartTimeSalary() throws Exception{
		Employee pt1 = new Employee("pt1@gmail.com","pswd",35);
		assertTrue(pt1.getSalary().isApproved());
		}

	 
	/*
	 * check if the branch manager is the part-time worker and approve himself
	 */
		@Test  
		public void managerIsPTWorkderThrowsException(){  
		    try{  
		    	PartTimer pt1 = new PartTimer("pt1@gmail.com","pswd",35);
		        BranchManager bm1 = new BranchManager("m1@gmail.com","pswd");
		        pt1.getId().equals(bm1.getId());
		        fail("No exception thrown.");  
		    }catch(Exception ex){  
		        assertTrue(invalidRoleException);  
		        assertTrue(ex.getMessage().contains("Branch manager cannot approve himself"));  
		    }  
		}  
}