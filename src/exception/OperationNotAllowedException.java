package exception;


public class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException() {
        super("Operation not allowed.");
    }

    public OperationNotAllowedException(String msg) {
        super(msg);
    }

    public OperationNotAllowedException(String msg, Throwable t) {
        super(msg, t);
    }

}