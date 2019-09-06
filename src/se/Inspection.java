package se;

import java.time.*;

import exception.*;

public class Inspection {
    private LocalDateTime dateTime;
    private boolean cancelled;

    public Inspection(LocalDateTime dateTime)
                        throws InvalidParamException {
        if (LocalDateTime.now().compareTo(dateTime) > 0)
            throw new InvalidParamException("Cannot schedule an inspection in the past.");
        this.dateTime = dateTime;
        cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isDone() {
        return !isCancelled() && LocalDateTime.now().compareTo(this.dateTime) > 0;
    }

    public boolean isUpcoming() {
        return !isCancelled() && LocalDateTime.now().compareTo(this.dateTime) <= 0;
    }

    public void setCancelled() {
        if (isUpcoming())
            cancelled = true;
    }
}