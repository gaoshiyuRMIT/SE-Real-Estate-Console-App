public class Notification {
    private String message;

    // active, dismissed
    private String status;
    private User user;

    public Notification(String message) {
        this.message = message;
        this.status = "active";
    }

    // change status
    public void dismiss() {

    }
}