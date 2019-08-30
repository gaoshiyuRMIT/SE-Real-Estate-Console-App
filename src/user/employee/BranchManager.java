package user.employee;

import java.util.*;

import property.*;
import SnE.*;
import consts.*;

public class BranchManager extends Employee {
    public ArrayList<Property> getNewlyAddedProperties(Branch branch) {
        ArrayList<Property> ret = branch.getProperties(
            null, null, null, PropertyStatus.NotListed, null, false
        );
        ret.addAll(branch.getProperties(
            null, null, null, PropertyStatus.NotListed, null, true
        ));
        return ret;
    }

    public void listProperty(Property property, EmployeeAssigned employee) {
        property.setEmployee(employee);
        property.setStatus(PropertyStatus.ApplicationOpen);
    }
}