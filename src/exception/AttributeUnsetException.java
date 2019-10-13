package exception;


public class AttributeUnsetException extends Exception {
    public AttributeUnsetException() {
        super("The specified attribute is not yet set.");
    }

    public AttributeUnsetException(String msg) {
        super(msg);
    }
}