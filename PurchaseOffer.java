import java.time.*;

import user.*;

public class PurchaseOffer {
    private String id;
    private Buyer buyer;
    private double amount;
    private LocalDateTime dateReceived;
    private LocalDateTime dateAccepted;
    private boolean accepted;
    private boolean rejected;
    private boolean withdrawn;
    private boolean depositPaid;
    private boolean settlementPaid;

    public PurchaseOffer(double amount, Buyer buyer) {
        this.amount = amount;
        accepted = false;
        rejected = false;
        withdrawn = false;
        depositPaid = false;
        settlementPaid = false;
        dateReceived = LocalDateTime.now();
        this.buyer = buyer;
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
                        && !depositPaid)
            withdrawn = true;
        return withdrawn;
    }
}