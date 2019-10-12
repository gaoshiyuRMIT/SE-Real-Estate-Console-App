package exception;


public class InvalidParamException extends Exception {
    public InvalidParamException() {
        super("Invalid parameter.");
    }

    public InvalidParamException(String msg) {
        super(msg);
    }
}