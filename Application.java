import java.time.*;

public class Application {
    private Tenant[] applicants;
    private double weeklyRental;

    // number of months
    private int duration;
    private LocalDateTime dateReceived;
    private LocalDateTime dateAccepted;
    private boolean accepted;
    private boolean rejected;
    private boolean rentBondPaid;
    private boolean withdrawn;

    public Application(Tenant[] applicants, double weeklyRental, int duration) {
        accepted = false;
        rejected = false;
        rentBondPaid = false;
        withdrawn = false;
        dateReceived = LocalDateTime.now();
        this.applicants = applicants;
        this.weeklyRental = weeklyRental;
        this.duration = duration;
    }

    public boolean isRejected() {
        if (!accepted && !rejected
                        && Instant.now().isAfter(dateReceived.plusDays(3).toInstant()))
            rejected = true;
        return rejected;
    }

    public void setAccepted() {
        accepted = true;
        dateAccepted = LocalDateTime.now();
    }

    public boolean isWithDrawn() {
        if (accepted && Instant.now().isAfter(dateAccepted.plusHours(24).toInstant())
                        && !rentBondPaid)
            withdrawn = true;
        return withdrawn;
    }
}