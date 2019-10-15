package console;

import java.util.*;

import user.customer.*;
import user.*;
import user.employee.*;
import property.*;
import exception.*;

public class PropertyListConsole extends BaseConsole{
    private User user;
    private List<Property> propertyList;

    public PropertyListConsole(User u, List<? extends Property> l, BaseConsole base) {
        super(base);
        user = u;
        propertyList = new ArrayList<Property>();
        for (Property p : l)
            propertyList.add(p);
        if (user instanceof Landlord || user instanceof BranchManager) {
            setMenuOptions(new String[] {
                "view property",
                "back"
            });
        }
    }

    public void viewProperty() throws InvalidInputException{
        Property p = getPropertyById();
        (new PropertyConsole(this.user, p, this)).console();
    }

    @Override
    public Property getPropertyById() throws InvalidInputException{
        Property p = super.getPropertyById();
        if (!propertyList.contains(p))
            throw new InvalidInputException(
                "The specified property is not in the list above."
            );
        return p;
    }

    public void console() {
        while (true) {
            System.out.println(util.getPageBreak("Properties"));
            for (Property rp : propertyList) {
                System.out.printf(
                    "%s %s %s\n",
                    rp.getId(), rp.getSuburb(), rp.getStatusS()
                );
            }
            System.out.println(util.getPageBreak());
            try {
                String option = displayMenu();
                if (option.equals("view property"))
                    viewProperty();
                else
                    break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}