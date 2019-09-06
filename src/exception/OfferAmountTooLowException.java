package exception;


public class OfferAmountTooLowException extends Exception {
    public OfferAmountTooLowException() {
        super();
    }

    public OfferAmountTooLowException(String msg) {
        super(msg);
    }
}