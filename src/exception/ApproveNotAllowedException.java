package exception;

public class ApproveNotAllowedException extends Exception {
    public ApproveNotAllowedException() {
        super();
    }

    public ApproveNotAllowedException(String msg) {
        super(msg);
    }
}