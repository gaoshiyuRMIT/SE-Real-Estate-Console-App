package user;

import java.util.*;

import se.*;
import consts.*;

public abstract class User {
    private String id;
    private String email;
    private String password;
    private List<Notification> notifications;

    /*
    TODO: check email format
    */
    public User(String email, String password, String id) {

        this.notifications = new ArrayList<Notification>();
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public void addNotif(Notification notif) {
        this.notifications.add(0, notif);
    }

    public List<Notification> getNotifications(NotifStatus st) {
        List<Notification> ret = new ArrayList<Notification>();
        for (Notification n : notifications) {
            if (n.getStatus() == st)
                ret.add(n);
        }
        return ret;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }


    public String getEmail() {
        return email;
    }

    // calculate md5 of passwd, compare it to `passwordHash`
    public boolean authenticate(String passwd) {
        return this.password.equals(passwd);
    }

    public String getId() {
        return this.id;
    }

    public boolean equals(User u) {
        return id.equals(u.id);
    }
}