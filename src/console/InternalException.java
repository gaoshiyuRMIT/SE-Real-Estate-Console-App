package console;


public class InternalException extends Exception {
    public InternalException() {
        super("Internal exception. Please contact system administrator.");
    }

    public InternalException(String msg) {
        super(msg);
    }

    public InternalException(Throwable t) {
        super(t);
    }
}