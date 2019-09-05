package user.customer;

import user.*;

public abstract class Customer extends User {

    private static int idCounter = 0;

    public static String genId() {
        return String.format("%c08d", Customer.idCounter++);
    }

    public abstract String getRole();

    public Customer(String email, String passwd) {
        super(email, passwd, Customer.genId());
    }
}