package property;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.*;
import user.customer.*;
import consts.*;

public class TestRentalProperty {
    @Test
    (expected = InvalidParamException.class)
    public void testCreatePropertyInvalidCapacity() throws Exception {
        HashMap<String, Integer> cap = new HashMap<String, Integer>();
        cap.put("new type of room", 2);
        Landlord l = new Landlord("a.b@c.d", "123");
        RentalProperty p = new RentalProperty(
            "123 Abc St, Melbourne, VIC 3000", "Melbourne", cap,
            PropertyType.Studio, 300, 6, l
        );
    }
}