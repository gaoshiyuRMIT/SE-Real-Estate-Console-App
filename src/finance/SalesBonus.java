package finance;

import user.employee.*;
import se.*;

public class SalesBonus extends PayrollItem{
    private static final String idPrefix = "SB";
    PurchaseOffer offer;

    public SalesBonus(Employee employee, PurchaseOffer offer) {
        super(
            employee,
            String.format("%s-%s", idPrefix, offer.getId()),
            offer.getCommission() * 0.4
        );
        this.offer = offer;
    }
}