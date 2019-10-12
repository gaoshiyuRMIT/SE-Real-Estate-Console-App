package exception;


public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("Insufficient balance.");
    }

    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}