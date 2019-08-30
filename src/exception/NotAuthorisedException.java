package exception;


public class NotAuthorisedException extends Exception {
    public NotAuthorisedException() {
        super();
    }

    public NotAuthorisedException(String msg) {
        super(msg);
    }
}