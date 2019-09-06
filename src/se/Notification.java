package se;

import user.*;
import consts.*;

public class Notification {
    private String message;
    private NotifStatus status;

    public Notification(String message) {
        this.message = message;
        this.status = NotifStatus.Active;
    }

    // change status
    public void dismiss() {
        this.status = NotifStatus.Archived;
    }
}