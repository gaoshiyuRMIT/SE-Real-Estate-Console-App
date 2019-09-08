package se;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.*;

import consts.*;
import exception.*;
import user.customer.*;


public class TestApplication {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /*
    creating an application with no applicant details supplied should be forbidden
    */
    @Test
    public void testCreateApplicationWithoutApplicant() throws Exception {
        Tenant tenant = new Tenant("a.b@c.d", "123456");

        thrown.expect(InvalidParamException.class);
        thrown.expectMessage(stringContainsInOrder(
            Arrays.asList("detail", "not", "specified")
        ));
        Application a = new Application(
            Arrays.asList(), 300, 5, tenant
        );
    }

    /*
    it should be forbidden to create an application with applicant details not stored in the account of the initiator
    */
    @Test
    public void testCreateApplicationWithApplicantsNotBelongingToUser() throws Exception {
        Tenant tenant = new Tenant("a.b@c.d", "123456");
        ApplicantDetail ad = new ApplicantDetail(
            new ID(IDType.Passport, "E00001111"),
            "Abby de Tenant", 100000, "Software Engineer",
            Arrays.asList("Dunder Mifflin, Junior Software Engineer, 06/2016-12/2017"),
            Arrays.asList()
        );

        thrown.expect(InvalidParamException.class);
        thrown.expectMessage(stringContainsInOrder(
            Arrays.asList("applicant id", "inconsistent with", "initiator")
        ));
        Application a = new Application(
            Arrays.asList(ad.getId()), 300, 5, tenant
        );
    }

}