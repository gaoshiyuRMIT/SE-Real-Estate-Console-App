package exception;


public class OfferAmountTooLowException extends Exception {
    public OfferAmountTooLowException() {
        super("Offer amount too low.");
    }

    public OfferAmountTooLowException(String msg) {
        super(msg);
    }
}