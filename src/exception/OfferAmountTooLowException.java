package exception;


public class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException() {
        super();
    }

    public OperationNotAllowedException(String msg) {
        super(msg);
    }
}