package exception;


public class NotAuthorisedException extends Exception {
    public NotAuthorisedException() {
        super("Not authorised.");
    }

    public NotAuthorisedException(String msg) {
        super(msg);
    }
}