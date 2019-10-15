package console;

import java.util.*;

import user.*;
import user.customer.*;
import se.*;
import exception.*;

public class ApplicationListConsole extends BaseConsole {
    private User user;
    private List<ApplicationBase> appList;

    public ApplicationListConsole(User u, List<? extends ApplicationBase> l,
                                    BaseConsole base) {
        super(base);
        user = u;
        appList = new ArrayList<ApplicationBase>();
        for (ApplicationBase a : l)
            appList.add(a);
        setMenuOptions(new String[] {
            "view application",
            "back"
        });
    }

    public ApplicationBase getApplicationById() throws InvalidInputException{
        System.out.print("Enter application id: ");
        String aid = scanner.next();
        for (ApplicationBase a : appList) {
            if (a.getId().equals(aid))
                return a;
        }
        throw new InvalidInputException(
            "The specified application is not in the list above."
        );
    }

    public void viewApplication() throws InvalidInputException, InternalException {
        ApplicationBase a = getApplicationById();
        (new ApplicationConsole(user, a, this)).console();
    }

    public void console() {
        while (true) {
            System.out.println(util.getPageBreak("Applications"));
            System.out.println(util.getPageBreak(
                String.format("Displaying %d result(s).", appList.size())
            ));
            for (ApplicationBase a : appList) {
                System.out.printf("%s by %s, %s\n", a.getId(), a.getInitiator().getId(),
                                    a.getStatusS());
            }
            System.out.println(util.getPageBreak());
            try {
                String option = displayMenu();
                if (option.equals("view application"))
                    viewApplication();
                else
                    break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (InternalException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}