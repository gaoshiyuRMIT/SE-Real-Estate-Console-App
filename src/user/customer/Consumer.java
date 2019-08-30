package user.customer;

public class Consumer extends Customer {
    String suburbOfInterest;

    public Consumer(String email, String password) {
        super(email, password);
    }

    public boolean interestedIn(String suburb) {
        return suburb.equals(this.suburbOfInterest);
    }
}