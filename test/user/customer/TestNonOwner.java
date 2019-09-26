package user.customer;

import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import consts.*;
import exception.*;


public class TestNonOwner {
    private Tenant tenant;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        tenant = new Tenant("abby.de.tenant@gmail.com", "123");
    }

    @Test
    public void testAddApplicant() throws Exception{
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

    @Test
    public void testAddExistingApplicant() throws Exception {
        ApplicantDetail ad = new ApplicantDetail(
            new ID(IDType.Passport, "E00001111"),
            "Abby de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );
        tenant.addApplicant(ad);

        // create an applicant with the same ID
        ApplicantDetail ad_rep = new ApplicantDetail(
            new ID(IDType.Passport, "E00001111"),
            "Doby de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Sales, 06/2016-12/2017"),
            Arrays.asList()
        );

        thrown.expect(ApplicantExistException.class);
        tenant.addApplicant(ad_rep);
    }
}