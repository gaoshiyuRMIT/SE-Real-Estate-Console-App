package console;


public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super("Invalid input.");
    }

    public InvalidInputException(String msg) {
        super(msg);
    }

    public InvalidInputException(Throwable t) {
        super(t);
    }

    public InvalidInputException(String msg, Throwable t) {
        super(msg, t);
    }
}