package SnE;

import user.*;
import consts.*;

public class Notification {
    private Artifact artifact;
    private String message;

    // active, dismissed
    private NotifStatus status;

    public Notification(String message, Artifact artifact) {
        this.artifact = artifact;
        this.message = message;
        this.status = NotifStatus.Active;
    }

    // change status
    public void dismiss() {

    }
}