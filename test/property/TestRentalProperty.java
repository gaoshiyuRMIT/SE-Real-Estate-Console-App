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
        Branch branch = new Branch("Melbourne");
        branch.addProperty(rentalProperty);
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
        List<Inspection> upcomingBefore = rentalProperty.getUpcomingInspections();
        // cancel an inspection
        inspection.setCancelled();
        List<Inspection> upcomingAfter = rentalProperty.getUpcomingInspections();
        assertTrue(upcomingBefore.contains(inspection));
        assertFalse(upcomingAfter.contains(inspection));
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
        List<Application> pias = rentalProperty.getApplicationsInitiatedBy(tenant);
        List<Application> ppas = rentalProperty.getPendingApplications();

        assertTrue(pas.contains(a));
        assertTrue(pias.contains(a));
        assertTrue(ppas.contains(a));
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
    2.application status should change
    3.pending-application list of the property should be cleared
    4.inspections can still be scheduled
    */
    @Test
    public void testAcceptApplication() throws Exception {
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
        List<Application> pendingApplications = rentalProperty.getPendingApplications();
        // add an inspection
        Inspection inspection = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspection);

        assertEquals(rentalProperty.getStatus(), PropertyStatus.InspectionOpen);
        assertTrue(a.isAwaitingPayment());
        assertEquals(pendingApplications.size(), 0);
        assertTrue(rentalProperty.isOpenForInspection());
        assertTrue(rentalProperty.getUpcomingInspections().contains(inspection));
    }

    /*
    after an application is accepted,
    1.inspections added before are still upcoming
    2.new inspections can still be scheduled
    */
    @Test
    public void testAddInspectionsAndAcceptApplication() throws Exception {
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
        // submit application
        rentalProperty.addApplication(a);
        // add an inspection
        Inspection inspectionBefore = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspectionBefore);
        // accept the application
        rentalProperty.acceptApplication(a);
        // schedule a new inspection
        Inspection inspection = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspection);

        List<Inspection> upcomingList = rentalProperty.getUpcomingInspections();

        assertTrue(upcomingList.contains(inspectionBefore));
        assertTrue(upcomingList.contains(inspection));
    }

    /*
    after an application is accepted, no further application can be taken
    */
    @Test
    public void testAddApplicationAfterApplicationAcceptance() throws Exception{
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
        // attempt to add a new application
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

    /*
    when multiple applications are submitted to one property,
    after an application is accepted,
    1.other applications should be automatically rejected
    2.hence the pending list should be empty
    */
    @Test
    public void testAcceptApplicationAmongMultiple() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        // first application
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

        // second application
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

        // accept the first application
        rentalProperty.acceptApplication(a);
        List<Application> pendingList = rentalProperty.getPendingApplications();

        assertTrue(a_ellen.isRejected());
        assertEquals(pendingList.size(), 0);
    }

    /*
    after an application is withdrawn, landlord should no longer see it in pending list
    */
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

        assertTrue(a.isWithdrawn());
        assertFalse(rentalProperty.getPendingApplications().contains(a));
    }

    /*
    after an application is accepted and then withdrawn, property should be back on market
    */
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
        rentalProperty.withdrawApplication(a);
        PropertyStatus stAfterWithdrawal = rentalProperty.getStatus();

        assertEquals(stAfterWithdrawal, PropertyStatus.ApplicationOpen);
    }

    /*
    after an application is rejected, it should not appear in the pending-application list
    */
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
        assertTrue(a.isRejected());
        assertFalse(a.isPending());
    }

    /*
    after rent & bond are paid for an application,
    1.a lease with the same applicants, rental, and duration should be created
    2.application status should change
    3.property status should change
    */
    @Test
    public void testPayRentNBondForAcceptedApplication() throws Exception {
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
        rentalProperty.payRentBondForApplication(a);
        Lease lease = rentalProperty.getCurrentLease();

        assertFalse(rentalProperty.isOpenForInspection());
        assertTrue(a.isSecured());
        assertNotNull(lease);
        assertTrue(lease.getTenants().equals(a.getApplicants()));
        assertEquals((long)(lease.getDuration()), 5l);
        assertEquals(lease.getWeeklyRental(), 300, 5e-8);
    }

    /*
    after rent & bond are paid for an application, all future inspections are cancelled
    */
    @Test
    public void testPayRentNBondAndInspection() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();

        // add an inspection
        Inspection inspectionBeforeApplication = new Inspection(LocalDateTime.now().plusDays(9));
        rentalProperty.addInspection(inspectionBeforeApplication);
        // add application
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
        // accept application
        rentalProperty.acceptApplication(a);
        // add another inspection
        Inspection inspectionAfterApplicationAccepted = new Inspection(LocalDateTime.now().plusDays(10));
        rentalProperty.addInspection(inspectionAfterApplicationAccepted);
        // pay rent & bond
        rentalProperty.payRentBondForApplication(a);

        List<Inspection> upcomingInspectionsAfterRentBondPaid = rentalProperty.getUpcomingInspections();

        assertEquals(upcomingInspectionsAfterRentBondPaid.size(), 0);
        assertTrue(inspectionBeforeApplication.isCancelled());
        assertTrue(inspectionAfterApplicationAccepted.isCancelled());
    }


    /*
    after rent & bond are paid for an application, scheduling new inspections is forbidden
    */
    @Test
    public void testAddInspectionAfterRentBondPaid() throws Exception {
        PropertyManager pm = new PropertyManager("lucas.de.manager@gmail.com", "456");
        rentalProperty.setManager(pm);
        rentalProperty.list();
        // add application
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
        // accept application
        rentalProperty.acceptApplication(a);
        // pay rent & bond
        rentalProperty.payRentBondForApplication(a);

        Inspection inspection = new Inspection(LocalDateTime.now().plusDays(10));

        thrown.expect(OperationNotAllowedException.class);
        rentalProperty.addInspection(inspection);
    }

    /*
    attempts to set the management fee outside of the range should fail
    */
    @Test
    public void testSetMangementFeeRateOutsideRateRange() {
        fail("not implemented");
    }
}