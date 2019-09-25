package finance;


import user.employee.*;

import se.*;

import exception.*;



public class SalesBonus extends PayrollItem{

    private static final String idPrefix = "SB";

    PurchaseOffer offer;



    public SalesBonus(Employee employee, PurchaseOffer offer)

                        throws OperationNotAllowedException{

        super(

            employee,

            String.format("%s-%s", idPrefix, offer.getId()),

            offer.getCommission() * 0.4

        );

        if (!offer.isFulfilled())

            throw new OperationNotAllowedException(

                "Sales bonus cannot be created until full price is paid for the offer."

            );

        this.offer = offer;

    }

}