package se;

import java.util.*;
import java.time.*;

import consts.*;

public class Notification {
    private LocalDateTime dateTime;
    private String message;
    private NotifStatus status;
    private List<String> uris;

    public Notification(String message, String... uris) {
        this.uris = Arrays.asList(uris);
        this.message = message;
        this.status = NotifStatus.Active;
        this.dateTime = LocalDateTime.now();
    }

    // change status
    public void dismiss() {
        this.status = NotifStatus.Archived;
    }

    public List<String> getURIs() {
        return uris;
    }
 }