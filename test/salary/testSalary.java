package salary;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;

import user.employee.*;

public class testSalary {
	private Employee e1;
	/*
	 * check if entered work hours is 1.valid(not minus ) and 2.not null
	 */
	@Test
	public void testWorkHours() throws Exception{
		assertNotNull(e1.getHours());
		assertTrue( e1.getHours() > 0);

	}	
	
	/*
	 * check if Part-timers are paid a salary that is proportional to the hours they worked
	 * SalesConsultant have extra 40% of commission
	 */
	@Test
	public void testPaidSalary() throws Exception{
		PropertyManager pm1 = new PropertyManager("pm1@gmail.com","pswd",1);
		BranchManager bm1 = new BranchManager("bm1@gmail.com","pswd",1);
		SalesConsultant sc1 = new SalesConsultant("sc1@gmail.com","pswd",1);
		//assume hoursalary 20 and commissionreceived=1000
		assertEquals(bm1.getSalary(), bm1.getHours()*20+0.4*1000);
		
	/*
	 * check if the status change 
	 * when they get the branch manager approval 
	 */
	 @Test
	 public void testPartTimeSalary() throws Exception{
		PartTimer pt1 = new PartTimer("pt1@gmail.com","pswd",35);
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
