package SnE;

import java.time.*;

import exception.*;

public class Inspection {
    private LocalDateTime dateTime;
    private String description;
    private boolean cancelled;
    private boolean done;

    public Inspection(LocalDateTime dateTime, String description)
                        throws InvalidParamException {
        if (LocalDateTime.now().compareTo(dateTime) < 0)
            throw new InvalidParamException("Cannot schedule an inspection in the past.");
        this.dateTime = dateTime;
        this.description = description;
        cancelled = false;
        done = false;
    }
}