import java.time.*;

import const.*;

public class Lease {
    private LocalDateTime startDate;
    // # months
    private int duration;
    private double weeklyRental;
    private Tenant[] tenants;
    // active, expired
    private LeaseStatus status;

    public Lease(Application a) {
        this.duration = a.duration;
        this.weeklyRental = a.weeklyRental;
        this.tenants = a.tenants;
        this.status = LeaseStatus.Active;
        this.startDate = LocalDateTime.now();
    }

    public LeaseStatus getStatus() {
        if (Instant.now().isAfter(startDate.plusMonths(duration).toInstant())) {
            status = LeaseStatus.Expired;
        }
        return status;
    }

}