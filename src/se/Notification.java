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

    public String getTextualDetail() {
        return String.format(
            "%-30s: %s\n"
                + "%-30s: \n%s",
            "Time Received", dateTime,
            "Message", message
        );
    }

    public NotifStatus getStatus() {
        return status;
    }

    // change status
    public void dismiss() {
        this.status = NotifStatus.Archived;
    }

    public List<String> getURIs() {
        return uris;
    }
    
    public String getMessage() {
    	return message;
    }
 }