import java.util.*;

public class User {
    private String id;
    private String email;
    private String passwordHash;
    private ArrayList<Notification> notifications;

    /*
    TODO: check email format
    */
    public User(String email, String passwd, String id) {

        this.notifications = new ArrayList<Notification>();
        this.id = id;
    }

    public void addNotif(Notification) {
    }

    // calculate md5 of passwd, compare it to `passwordHash`
    public boolean authenticate(String passwd) {
    }


}