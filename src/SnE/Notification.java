package SnE;

import user.*;
import consts.*;

public class Notification {
    private String message;

    // active, dismissed
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