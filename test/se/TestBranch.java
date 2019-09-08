package se;

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
import user.*;
import user.customer.*;
import user.employee.*;
import property.*;

public class TestBranch {
    private Branch branch;
    private Landlord landlord;
    private Vendor vendor;
    private Tenant tenant;
    private RentalProperty rentalProperty;
    private ForSaleProperty forSaleProperty;
    private SalesConsultant[] consultants;
    private PropertyManager[] propMgrs;
    private BranchManager[] brchMgrs;
    private Employee[] brchAdms;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        branch = new Branch("Melbourne");
        setUpEmployees();
    }

    /*
    1.a new customer can register a new account
    2.the id of a customer used to log on should start with "c"
    */
    @Test
    public void testRegister() throws InvalidParamException, CustomerExistException {
        String email = "jane.doe123@gmail.com";
        String cid = branch.register(email, "666666", "Landlord");
        assertTrue(cid.startsWith("c"));
        User u = branch.login(cid, "666666");
        assertNotNull(u);
        assertTrue(u instanceof Landlord);
    }

    /*
    1.one email should be able to register as different roles
    2.separate accounts are created
    */
    @Test
    public void testRegisterSameEmail() throws InvalidParamException, CustomerExistException {
        String email = "jane.doe123@gmail.com";
        String cid = branch.register(email, "666666", "Landlord");
        // same email, a new role
        String cid2 = branch.register(email, "777777", "Vendor");
        assertNotEquals(cid, cid2);
    }

    /*
    one email, one role can only have one account
    */
    @Test
    (expected = CustomerExistException.class)
    public void testRegisterRepeatedUser() throws InvalidParamException, CustomerExistException{
        String email = "jane.doe123@gmail.com";
        branch.register(email, "666666", "Landlord");
        branch.register(email, "777777", "Landlord");
    }

    /*
    the id of an employee should start with "e"
    */
    @Test
    public void testEmployeeLogin() throws Exception {
        branch.addEmployee(consultants[0]);
        assertTrue(consultants[0].getId().startsWith("e"));
        User u = branch.login(consultants[0].getId(), "123456");
        assertNotNull(u);
        assertTrue(u instanceof SalesConsultant);
    }

    /*
    (tests methods used when the branch manager is selecting which employee to assign)
    */
    @Test
    public void testGetEmployees() throws Exception {
        branch.addEmployee(consultants[0]);
        branch.addEmployee(consultants[1]);
        branch.addEmployee(propMgrs[0]);

        List<SalesConsultant> res = branch.getAllSaleConsultants();
        assertTrue(res.contains(consultants[0]));
        assertTrue(res.contains(consultants[1]));

        List<PropertyManager> res1 = branch.getAllPropertyManagers();
        assertTrue(res1.contains(propMgrs[0]));
    }

    @Test
    public void testLoginWIncorrectPasswd() throws Exception {
        String email = "jane.doe123@gmail.com";
        String id = branch.register(email, "666666", "Landlord");
        User u = branch.login(id, "666667");
        assertNull(u);
    }

    /*
    1.a branch manager should be able to see which properties need to be reviewed & listed
    2.the property should be returned when searching for properties owned by this landlord
    */
    @Test
    public void testAddRentalProperty() throws Exception {
        setUpLandlord();
        setUpRentalProperty(landlord);

        branch.addProperty(rentalProperty);

        List<RentalProperty> ps = branch.getOwnedRentalProperty(landlord);
        assertTrue(ps.contains(rentalProperty));

        List<Property> ps1 = branch.getNewlyAddedProperties();
        assertTrue(ps1.contains(rentalProperty));
    }

    @Test
    public void testAddForSaleProperty() throws Exception {
        setUpVendor();
        setUpForSaleProperty();

        branch.addProperty(forSaleProperty);

        List<ForSaleProperty> ps = branch.getOwnedForSaleProperty(vendor);
        assertTrue(ps.contains(forSaleProperty));

        List<Property> ps1 = branch.getNewlyAddedProperties();
        assertTrue(ps1.contains(forSaleProperty));
    }

    /*
    the property should be returned when searching for properties managed by this property manager
    */
    @Test
    public void testAssignPropertyManager2RentalProperty() throws Exception {
        setUpLandlord();
        setUpRentalProperty(landlord);
        branch.addEmployee(propMgrs[0]);

        branch.addProperty(rentalProperty);
        rentalProperty.setManager(propMgrs[0]);

        List<RentalProperty> ps = branch.getManagedRentalProperty(propMgrs[0]);
        assertTrue(ps.contains(rentalProperty));
    }

    /*
    after a rental property is listed,
    1.property status should change
    2.property should be open for inspection
    3.property is no longer in the search results of newly-added properties
    */
    @Test
    public void testListRentalProperty() throws Exception {
        setUpLandlord();
        setUpRentalProperty(landlord);
        branch.addEmployee(propMgrs[1]);
        branch.addProperty(rentalProperty);
        rentalProperty.setManager(propMgrs[1]);

        rentalProperty.list();

        assertEquals(rentalProperty.getStatus(), PropertyStatus.ApplicationOpen);
        assertTrue(rentalProperty.isOpenForInspection());
        List<Property> ps = branch.getNewlyAddedProperties();
        assertFalse(ps.contains(rentalProperty));
    }

    /*
    it is forbidden to list a property before an employee is assigned to it
    */
    @Test
    (expected = OperationNotAllowedException.class)
    public void testListRentalPropertyWNoAssignee() throws Exception {
        setUpLandlord();
        setUpRentalProperty(landlord);
        branch.addEmployee(propMgrs[1]);
        branch.addProperty(rentalProperty);
        rentalProperty.list();
    }

    @Test
    public void addApplicantDetail2Tenant() throws Exception {
        setUpTenant();
        ApplicantDetail ad = new ApplicantDetail(
            new ID(IDType.Passport, "E00001111"),
            "Abby de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );
        tenant.addApplicant(ad);
        assertTrue(tenant.getApplicants(null).contains(ad));
        assertTrue(tenant.hasApplicant(ad.getId()));
    }

    /*
    (tests the method that searches for applications initiated by a particular tenant)
    */
    @Test
    public void testGetPropertyNApplication4Tenant() throws Exception {
        setUpLandlord();
        setUpRentalProperty(landlord);
        branch.addEmployee(propMgrs[1]);
        branch.addProperty(rentalProperty);
        rentalProperty.setManager(propMgrs[1]);
        rentalProperty.list();

        setUpTenant();
        ApplicantDetail ad = new ApplicantDetail(
            new ID(IDType.Passport, "E00001111"),
            "Abby de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );
        tenant.addApplicant(ad);

        Application a = new Application(
            Arrays.asList(ad.getId()), 300, 5, tenant
        );
        rentalProperty.addApplication(a);

        List<SimpleEntry<RentalProperty, Application>> as
                = branch.getApplications(tenant);
        assertTrue(as.contains(
            new SimpleEntry<RentalProperty, Application>(rentalProperty, a)
        ));
    }


    public void setUpVendor() throws Exception {
        String cid = branch.register("john.doe345@gmail.com", "111111", "Vendor");
        this.vendor = (Vendor)(branch.login(cid, "111111"));
    }

    public void setUpLandlord() throws Exception {
        String cid = branch.register("jane.doe123@gmail.com", "111111", "Landlord");
        this.landlord = (Landlord)(branch.login(cid, "111111"));
    }

    public void setUpRentalProperty(Landlord l) throws Exception {
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        rentalProperty = new RentalProperty(
            "124 Thomas St, VIC 3122", "Hawthorn", cap,
            PropertyType.House, 300, 6, l
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

    public void setUpTenant() throws Exception {
        String cid = branch.register("abby.the.tenant@gmail.com", "123456", "Tenant");
        tenant = (Tenant)(branch.login(cid, "123456"));
    }
}