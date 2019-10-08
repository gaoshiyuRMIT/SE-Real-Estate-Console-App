package finance;

import exception.*;

public class Account {
    private double balance;

    public void debit(double amount) throws InsufficientBalanceException{
        if (amount > balance)
            throw new InsufficientBalanceException();
        balance -= amount;
    }

    public void printCheck(double amount, String desc) throws InsufficientBalanceException{
        debit(amount);
    }

    public void credit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}