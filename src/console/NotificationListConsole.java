package console;

import java.util.*;

import se.*;
import user.customer.*;
import user.*;
import exception.*;

public class NotificationListConsole extends BaseConsole {
    private List<Notification> notifList;
    private NonOwner user;

    public NotificationListConsole(User u, List<Notification> l, BaseConsole base) {
        super(base);
        notifList = l;
        user = (NonOwner)u;
        setMenuOptions(new String[] {
            "dismiss",
            "back"
        });
    }

    public void dismiss() throws InvalidInputException{
        Notification n = getNotifById();
        n.dismiss();
        System.out.println("Notification dismissed successfully.");
    }

    public Notification getNotifById() throws InvalidInputException {
        System.out.printf("Enter the No. of notification to dismiss: ");
        int i = scanner.nextInt();
        if (i > notifList.size())
            throw new InvalidInputException(
                "Specified number is not included in the list above."
            );
        return notifList.get(i-1);
    }

    public void console() {
        System.out.println("======= Notifications ==========");
        for (int i = 0; i < notifList.size(); i++) {
            Notification n = notifList.get(i);
            System.out.printf("No. %d\n", i+1);
            System.out.println(n.getTextualDetail());
            System.out.println("=========================");
        }

        while (true) {
            try {
                String option = displayMenu();
                if (option.equals("dismiss"))
                    dismiss();
                else
                    break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}