package exception;


public class InvalidParamException extends Exception {
    public InvalidParamException() {
        super();
    }

    public InvalidParamException(String msg) {
        super(msg);
    }
}