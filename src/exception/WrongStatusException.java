package exception;

public class WrongStatusException extends Exception{
	public WrongStatusException() {
        super();
    }

    public WrongStatusException(String msg) {
        super(msg);
    }
}
