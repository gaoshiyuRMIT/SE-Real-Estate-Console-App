package property;

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


public class TestRentalProperty {
    private Landlord landlord;
    private RentalProperty rentalProperty;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        landlord = new Landlord("jane.de.landlord@gmail.com", "123");
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("bedroom", 2);
        cap.put("bathroom", 1);
        cap.put("car space", 2);
        rentalProperty = new RentalProperty(
            "124 Thomas St, VIC 3122", "Hawthorn", cap,
            PropertyType.House, 300, 6, landlord
        );
    }

    /*
    check the input format of capacity when creating a new rental property
    */
    @Test
    public void testCreatePropertyInvalidCapacity() throws Exception {
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("new type of room", 2);
        Landlord l = new Landlord("a.b@c.d", "123");

        thrown.expect(InvalidParamException.class);
        RentalProperty p = new RentalProperty(
            "123 Abc St, Melbourne, VIC 3000", "Melbourne", cap,
            PropertyType.Studio, 300, 6, l
        );
    }

    @Test
    public void testAddInspectionToRentalProperty() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        Inspection inspection = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspection);
        assertTrue(rentalProperty.getUpcomingInspections().contains(inspection));
        // cancel an inspection
        inspection.setCancelled();
        assertFalse(rentalProperty.getUpcomingInspections().contains(inspection));
    }

    /*
    after an application is taken,
    1.it should be returned by multiple searching methods of the property
    2.it should be pending
    */
    @Test
    public void testApplyForRentalProperty() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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

        List<Application> pas = rentalProperty.getApplications();
        assertTrue(pas.contains(a));

        List<Application> pias = rentalProperty.getApplicationsInitiatedBy(tenant);
        assertTrue(pias.contains(a));

        List<Application> ppas = rentalProperty.getPendingApplications();
        assertTrue(pas.contains(a));
    }

    /*
    applying for a property before it is listed should be forbidden
    */
    @Test
    public void testApplyForRentalPropertyBeforeListing() throws Exception {
        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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

        thrown.expect(OperationNotAllowedException.class);
        rentalProperty.addApplication(a);
    }

    /*
    after an application is accepted,
    1.property status should change
    2.other applications should be automatically rejected
    3.no further applications can be taken
    */
    @Test
    public void testAcceptApplicationAmongMultiple() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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

        Tenant tenant_ellen = new Tenant("ellen.de.tenant@gmail.com", "123");
        ApplicantDetail ad_ellen = new ApplicantDetail(
            new ID(IDType.Passport, "E00001113"),
            "Ellen de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );
        tenant_ellen.addApplicant(ad_ellen);

        Application a_ellen = new Application(
            Arrays.asList(ad_ellen.getId()), 300, 5, tenant_ellen
        );
        rentalProperty.addApplication(a_ellen);

        assertArrayEquals(rentalProperty.getPendingApplications().toArray(),
                            new Object[] {a, a_ellen});

        rentalProperty.acceptApplication(a);
        assertNotEquals(rentalProperty.getStatus(), PropertyStatus.ApplicationOpen);
        assertEquals(rentalProperty.getPendingApplications().size(), 0);
        assertTrue(a.isAwaitingPayment());

        assertTrue(rentalProperty.isOpenForInspection());
        // add an inspection
        Inspection inspection = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspection);
        assertTrue(rentalProperty.getUpcomingInspections().contains(inspection));

        Tenant tenant2 = new Tenant("georgia.de.tenant@gmail.com", "123");
        ApplicantDetail ad2 = new ApplicantDetail(
            new ID(IDType.Passport, "E00001112"),
            "georgia de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );
        tenant2.addApplicant(ad2);

        Application a2 = new Application(
            Arrays.asList(ad2.getId()), 300, 5, tenant2
        );

        thrown.expect(OperationNotAllowedException.class);
        rentalProperty.addApplication(a2);
    }

    @Test
    public void testWithdrawApplicationWhenPending() throws Exception{
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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
        rentalProperty.withdrawApplication(a);
        assertFalse(rentalProperty.getPendingApplications().contains(a));
        // accept after withdrawal
        thrown.expect(OperationNotAllowedException.class);
        rentalProperty.acceptApplication(a);
    }

    @Test
    public void testWithdrawApplicationAfterAcceptance() throws Exception{
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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
        rentalProperty.acceptApplication(a);
        assertEquals(rentalProperty.getStatus(), PropertyStatus.InspectionOpen);
        rentalProperty.withdrawApplication(a);
        assertEquals(rentalProperty.getStatus(), PropertyStatus.ApplicationOpen);
    }

    @Test
    public void testRejectApplication() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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
        rentalProperty.rejectApplication(a);
        assertFalse(rentalProperty.getPendingApplications().contains(a));
        // withdraw after rejection
        thrown.expect(OperationNotAllowedException.class);
        rentalProperty.withdrawApplication(a);
    }

    @Test
    public void testPayRentNBondForAcceptedApplication() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();
        // add an inspection
        Inspection inspection = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspection);

        Tenant tenant = new Tenant("abby.de.tenant@gmail.com", "123");
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
        // there are still upcoming inspections
        assertTrue(rentalProperty.getUpcomingInspections().contains(inspection));

        rentalProperty.acceptApplication(a);

        rentalProperty.payRentBondForApplication(a);
        assertFalse(rentalProperty.isOpenForInspection());
        assertTrue(a.isSecured());
        // upcoming inspections are all cancelled
        assertFalse(rentalProperty.getUpcomingInspections().contains(inspection));
        // a lease is created
        Lease lease = rentalProperty.getCurrentLease();
        assertNotNull(lease);
        assertTrue(lease.getTenants().equals(a.getApplicants()));
        assertEquals((long)(lease.getDuration()), 5l);
        assertEquals(lease.getWeeklyRental(), 300, 5e-8);
    }
}