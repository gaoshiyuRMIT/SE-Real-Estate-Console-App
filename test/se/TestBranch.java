package se;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import consts.*;
import exception.*;
import user.*;
import user.customer.*;
import user.employee.*;
import property.*;

public class TestBranch {
    Branch branch;
    Landlord landlord;
    Vendor vendor;
    RentalProperty rentalProperty;
    ForSaleProperty forSaleProperty;
    SalesConsultant[] consultants;
    PropertyManager[] propMgrs;
    BranchManager[] brchMgrs;
    Employee[] brchAdms;

    @Before
    public void setUp() {
        branch = new Branch("Melbourne");
        setUpEmployees();
    }

    @Test
    public void testRegister() throws InvalidParamException, CustomerExistException {
        String email = "jane.doe123@gmail.com";
        String cid = branch.register(email, "666666", "Landlord");
        assertTrue(cid.startsWith("c"));
        User u = branch.login(cid, "666666");
        assertNotNull(u);
        assertTrue(u instanceof Landlord);
    }

    @Test
    public void testRegisterSameEmail() throws InvalidParamException, CustomerExistException {
        String email = "jane.doe123@gmail.com";
        String cid = branch.register(email, "666666", "Landlord");
        // same email, a new role
        String cid2 = branch.register(email, "777777", "Vendor");
        assertNotEquals(cid, cid2);
    }

    @Test
    (expected = CustomerExistException.class)
    public void testRegisterRepeatedUser() throws InvalidParamException, CustomerExistException{
        String email = "jane.doe123@gmail.com";
        branch.register(email, "666666", "Landlord");
        branch.register(email, "777777", "Landlord");
    }

    @Test
    public void testEmployeeLogin() throws Exception {
        branch.addEmployee(consultants[0]);
        User u = branch.login(consultants[0].getId(), "123456");
        assertNotNull(u);
        assertTrue(u instanceof SalesConsultant);
    }

    @Test
    public void testGetEmployees() throws Exception {
        branch.addEmployee(consultants[0]);
        branch.addEmployee(consultants[1]);
        branch.addEmployee(propMgrs[0]);

        ArrayList<SalesConsultant> res = branch.getAllSaleConsultants();
        assertTrue(res.contains(consultants[0]));
        assertTrue(res.contains(consultants[1]));

        ArrayList<PropertyManager> res1 = branch.getAllPropertyManagers();
        assertTrue(res1.contains(propMgrs[0]));
    }

    @Test
    public void testRegisterIncorrectPasswd() throws Exception {
        String email = "jane.doe123@gmail.com";
        String id = branch.register(email, "666666", "Landlord");
        User u = branch.login(id, "666667");
        assertNull(u);
    }

    @Test
    public void testAddRentalProperty() throws Exception {
        setUpLandlord();
        setUpRentalProperty();

        branch.addProperty(rentalProperty);

        ArrayList<RentalProperty> ps = branch.getOwnedRentalProperty(landlord);
        assertTrue(ps.contains(rentalProperty));

        ArrayList<Property> ps1 = branch.getNewlyAddedProperties();
        assertTrue(ps1.contains(rentalProperty));
    }

    @Test
    public void testAssignPropertyManager2RentalProperty() throws Exception {
        setUpLandlord();
        setUpRentalProperty();
        branch.addEmployee(propMgrs[0]);

        branch.addProperty(rentalProperty);
        rentalProperty.setManager(propMgrs[0]);

        ArrayList<RentalProperty> ps = branch.getManagedRentalProperty(propMgrs[0]);
        assertTrue(ps.contains(rentalProperty));
    }

    @Test
    public void testListRentalProperty() throws Exception {
        setUpLandlord();
        setUpRentalProperty();
        branch.addEmployee(propMgrs[1]);
        branch.addProperty(rentalProperty);
        rentalProperty.setManager(propMgrs[1]);

        rentalProperty.list();

        ArrayList<Property> ps = branch.getNewlyAddedProperties();
        assertFalse(ps.contains(rentalProperty));
    }

    @Test
    (expected = OperationNotAllowedException.class)
    public void testListRentalPropertyWNoAssignee() throws Exception {
        setUpLandlord();
        setUpRentalProperty();
        branch.addEmployee(propMgrs[1]);
        branch.addProperty(rentalProperty);
        rentalProperty.list();
    }

    @Test
    public void testAddForSaleProperty() throws Exception {
        setUpVendor();
        setUpForSaleProperty();

        branch.addProperty(forSaleProperty);

        ArrayList<ForSaleProperty> ps = branch.getOwnedForSaleProperty(vendor);
        assertTrue(ps.contains(forSaleProperty));

        ArrayList<Property> ps1 = branch.getNewlyAddedProperties();
        assertTrue(ps1.contains(forSaleProperty));
    }

    public void setUpVendor() throws Exception {
        String cid = branch.register("john.doe345@gmail.com", "111111", "Vendor");
        this.vendor = (Vendor)(branch.login(cid, "111111"));
    }

    public void setUpLandlord() throws Exception {
        String cid = branch.register("jane.doe123@gmail.com", "111111", "Landlord");
        this.landlord = (Landlord)(branch.login(cid, "111111"));
    }

    public void setUpRentalProperty() throws Exception {
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        rentalProperty = new RentalProperty(
            "124 Thomas St, VIC 3122", "Hawthorn", cap,
            PropertyType.House, 300, 6, this.landlord
        );
    }

    public void setUpForSaleProperty() throws Exception {
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 2);
        cap.put("car space", 2);

        forSaleProperty = new ForSaleProperty(
            "2 Manningtree Rd, VIC 3122", "Hawthorn", cap,
            PropertyType.Studio, 400000, this.vendor
        );
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