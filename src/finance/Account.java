package finance;

import exception.*;

public class Account {
    private double balance;

    public void withdraw(double amount) throws InsufficientBalanceException{
        if (amount > balance)
            throw new InsufficientBalanceException();
        balance -= amount;
    }

    public void printCheck(double amount, String desc) throws InsufficientBalanceException{
        withdraw(amount);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}