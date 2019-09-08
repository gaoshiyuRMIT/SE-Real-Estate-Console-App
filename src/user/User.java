package user;

import java.util.*;

import se.*;

public abstract class User {
    private String id;
    private String email;
    private String password;
    private ArrayList<Notification> notifications;

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
        this.notifications.add(notif);
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