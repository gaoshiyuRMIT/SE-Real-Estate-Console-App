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
        User u = branch.login(cid, "666666");
        assertTrue(cid.startsWith("c"));
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
        List<PropertyManager> res1 = branch.getAllPropertyManagers();

        assertTrue(res.contains(consultants[0]));
        assertTrue(res.contains(consultants[1]));
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
        List<Property> ps1 = branch.getNewlyAddedProperties();
        assertTrue(ps.contains(rentalProperty));
        assertTrue(ps1.contains(rentalProperty));
    }

    @Test
    public void testAddForSaleProperty() throws Exception {
        setUpVendor();
        setUpForSaleProperty();

        branch.addProperty(forSaleProperty);

        List<ForSaleProperty> ps = branch.getOwnedForSaleProperty(vendor);
        List<Property> ps1 = branch.getNewlyAddedProperties();
        assertTrue(ps.contains(forSaleProperty));
        assertTrue(ps1.contains(forSaleProperty));
    }

    /*
    the property should be returned when searching for properties managed by this property manager
    */
    @Test
    public void testAssignPropertyManagerToRentalProperty() throws Exception {
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

        List<Property> ps = branch.getNewlyAddedProperties();
        assertFalse(ps.contains(rentalProperty));
        assertEquals(rentalProperty.getStatus(), PropertyStatus.ApplicationOpen);
        assertTrue(rentalProperty.isOpenForInspection());
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


    /*
    (tests the method that searches for applications initiated by a particular tenant)
    */
    @Test
    public void testGetPropertyApplicationForTenant() throws Exception {
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

    /*
    the range of management fee rate should change
    */
    @Test
    public void testAddMultiplePropertiesForOneLandlord() throws Exception{
        setUpLandlord();
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlord
            ),
            new RentalProperty(
                "29 Manningtree Rd, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlord
            )
        };
        branch.addProperty(properties[0]);
        properties[0].setManagementFeeRate(0.75);
        double oldRate = properties[0].getManagementFeeRate();
        branch.addProperty(properties[1]);
        double rate = properties[0].getManagementFeeRate();
        assertEquals(oldRate, 0.75, 5e-8);
        assertTrue(rate <= 0.7 && rate >= 0.6);
    }
    /*
    1.browse rental property by address (road/street name)
    2.only listed ones should be returned
    */
    @Test
    public void testBrowseRentalPropertyByAddress() throws Exception {
        Landlord[] landlords = {
            (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123"),
            (Landlord)branch.login(branch.register("b.de.landlord@gmail.com", "123", "Landlord"), "123")
        };
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            ),
            new RentalProperty(
                "29 Manningtree Rd, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[1]
            ),
            new RentalProperty(
                "32 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            )
        };
        for (RentalProperty p : properties)
            branch.addProperty(p);
        // list only properties[0]&[1]
        for (int i = 0; i < 2; i++) {
            properties[i].setManager(propMgrs[0]);
            properties[i].list();
        }
        List<RentalProperty> res;
        res = branch.browseRentalProperties("Thomas", null, null, null, true);
        assertTrue(res.contains(properties[0]));
        assertFalse(res.contains(properties[1]));
        assertFalse(res.contains(properties[2]));
    }

    /*
    1.browse rental property by suburb
    2.only listed ones should be returned
    */
    @Test
    public void testBrowseRentalPropertyBySuburb() throws Exception {
        Landlord[] landlords = {
            (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123"),
            (Landlord)branch.login(branch.register("b.de.landlord@gmail.com", "123", "Landlord"), "123")
        };
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            ),
            new RentalProperty(
                "159 Through Rd, VIC 3124", "Camberwell", cap,
                PropertyType.Flat, 400, 6, landlords[1]
            ),
            new RentalProperty(
                "29 Manningtree Rd, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[1]
            )
        };
        for (RentalProperty p : properties)
            branch.addProperty(p);
        // list only properties[0]&[1]
        for (int i = 0; i < 2; i++) {
            properties[i].setManager(propMgrs[0]);
            properties[i].list();
        }
        List<RentalProperty> res;
        res = branch.browseRentalProperties(null, "Hawthorn", null, null, true);
        assertTrue(res.contains(properties[0]));
        assertFalse(res.contains(properties[1]));
        assertFalse(res.contains(properties[2]));
    }

    /*
    1.browse rental property by type
    2.only listed ones should be returned
    */
    @Test
    public void testBrowseRentalPropertyByType() throws Exception {
        Landlord[] landlords = {
            (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123"),
            (Landlord)branch.login(branch.register("b.de.landlord@gmail.com", "123", "Landlord"), "123")
        };
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            ),
            new RentalProperty(
                "159 Through Rd, VIC 3124", "Camberwell", cap,
                PropertyType.Flat, 400, 6, landlords[1]
            ),
            new RentalProperty(
                "29 Manningtree Rd, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[1]
            )
        };
        for (RentalProperty p : properties)
            branch.addProperty(p);
        // list only properties[0]&[1]
        for (int i = 0; i < 2; i++) {
            properties[i].setManager(propMgrs[0]);
            properties[i].list();
        }
        List<RentalProperty> res;
        res = branch.browseRentalProperties(null, null, null, PropertyType.House, true);
        assertTrue(res.contains(properties[0]));
        assertFalse(res.contains(properties[1]));
        assertFalse(res.contains(properties[2]));
    }

    /*
    1.browse rental property by capacity, number of bedrooms/bathrooms/car spaces all specified
    2.only listed ones should be returned
    */
    @Test
    public void testBrowseRentalPropertyByCompleteCapacity() throws Exception {
        Landlord[] landlords = {
            (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123"),
            (Landlord)branch.login(branch.register("b.de.landlord@gmail.com", "123", "Landlord"), "123")
        };
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        HashMap<String, Integer> cap2 = new HashMap<String, Integer>();
        cap2.put("bedroom", 2);
        cap2.put("bathroom", 1);
        cap2.put("car space", 1);
        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            ),
            new RentalProperty(
                "159 Through Rd, VIC 3124", "Camberwell", cap2,
                PropertyType.Flat, 400, 6, landlords[1]
            ),
            new RentalProperty(
                "32 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            )
        };
        for (RentalProperty p : properties)
            branch.addProperty(p);
        // list only properties[0]&[1]
        for (int i = 0; i < 2; i++) {
            properties[i].setManager(propMgrs[0]);
            properties[i].list();
        }
        List<RentalProperty> res;
        res = branch.browseRentalProperties(null, null, cap, null, true);
        assertTrue(res.contains(properties[0]));
        assertFalse(res.contains(properties[1]));
        assertFalse(res.contains(properties[2]));
    }

    /*
    1.browse rental property by capacity, number of bedrooms/bathrooms/car spaces partly specified
    */
    @Test
    public void testBrowseRentalPropertyByPartialCapacity() throws Exception {
        Landlord[] landlords = {
            (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123"),
            (Landlord)branch.login(branch.register("b.de.landlord@gmail.com", "123", "Landlord"), "123")
        };
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        HashMap<String, Integer> cap2 = new HashMap<String, Integer>();
        cap2.put("bedroom", 2);
        cap2.put("bathroom", 1);
        cap2.put("car space", 1);
        HashMap<String, Integer> cap3 = new HashMap<String, Integer>();
        cap3.put("bedroom", 1);
        cap3.put("bathroom", 1);
        cap3.put("car space", 0);

        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            ),
            new RentalProperty(
                "159 Through Rd, VIC 3124", "Camberwell", cap2,
                PropertyType.Flat, 400, 6, landlords[1]
            ),
            new RentalProperty(
                "1/32 Thomas St, VIC 3122", "Hawthorn", cap3,
                PropertyType.Studio, 280, 6, landlords[0]
            )
        };
        for (RentalProperty p : properties) {
            branch.addProperty(p);
            p.setManager(propMgrs[0]);
            p.list();
        }
        HashMap<String, Integer> cap_search = new HashMap<String, Integer>();
        cap_search.put("bedroom", 2);
        cap_search.put("bathroom", 1);
        List<RentalProperty> res;
        res = branch.browseRentalProperties(null, null, cap_search, null, true);
        assertTrue(res.contains(properties[0]));
        assertTrue(res.contains(properties[1]));
        assertFalse(res.contains(properties[2]));
    }
    /*
    property with accepted application should disappear from searching result
    */
    @Test
    public void testBrowseRentalPropertyAfterApplicationSecurement() throws Exception {
        Landlord[] landlords = {
            (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123"),
            (Landlord)branch.login(branch.register("b.de.landlord@gmail.com", "123", "Landlord"), "123")
        };
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        RentalProperty[] properties = {
            new RentalProperty(
                "31 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            ),
            new RentalProperty(
                "32 Thomas St, VIC 3122", "Hawthorn", cap,
                PropertyType.House, 500, 6, landlords[0]
            )
        };
        for (RentalProperty p : properties) {
            branch.addProperty(p);
            p.setManager(propMgrs[0]);
            p.list();
        }

        setUpTenant();
        ApplicantDetail ad = new ApplicantDetail(
            new ID(IDType.Passport, "E00001111"),
            "Abby de Tenant", 80000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );
        tenant.addApplicant(ad);
        Application a = new Application(
            Arrays.asList(ad.getId()), 500, 6, tenant
        );
        properties[0].addApplication(a);
        properties[0].acceptApplication(a);
        properties[0].payRentBondForApplication(a);

        List<RentalProperty> res;
        res = branch.browseRentalProperties("Thomas", null, null, null, true);
        assertTrue(res.contains(properties[1]));
        assertFalse(res.contains(properties[0]));
    }

    @Test
    public void testSendNotificationForListedProperty() throws Exception {
        Tenant tenant = (Tenant)branch.login(branch.register("a.de.tenant@gmail.com", "123", "Tenant"), "123");
        tenant.addSuburbOfInterest("Hawthorn");
        tenant.addSuburbOfInterest("Sunshine");

        Landlord landlord = (Landlord)branch.login(branch.register("a.de.landlord@gmail.com", "123", "Landlord"), "123");
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        RentalProperty p = new RentalProperty(
            "31 Thomas St, VIC 3122", "Hawthorn", cap,
            PropertyType.House, 500, 6, landlord
        );
        branch.addProperty(p);
        p.setManager(propMgrs[0]);

        List<Notification> activeNotifListBeforeListing = tenant.getNotifications(NotifStatus.Active);

        p.list();
        branch.sendNotifForListedProperty(p);

        List<Notification> activeNotifList = tenant.getNotifications(NotifStatus.Active);
        Notification notif = null;
        String msg = "";
        if (activeNotifList.size() > 0) {
            notif = activeNotifList.get(0);
            msg = notif.getMessage();
        }
        String propertyId = p.getId();

        assertTrue(activeNotifListBeforeListing.isEmpty());
        assertEquals(activeNotifList.size(), 1);
        assertTrue(msg.contains("listed") && msg.contains(propertyId));
    }

    @Test
    public void testSendNotificationForNewInspection() throws Exception {
        fail("not implemented");
    }

    @Test
    public void testSendNotificationForCancelledInspection() throws Exception {
        fail("not implemented");
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