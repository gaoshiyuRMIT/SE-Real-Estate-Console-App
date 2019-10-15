package finance;

import exception.*;

public class Account {
    private double initialBalance;
    private double balance;

    public Account(double iniBal) {
        initialBalance = iniBal;
        balance = iniBal;
    }

    public Account() {
        this(0.0);
    }

    public double getNetIncome() {
        return balance - initialBalance;
    }

    public void withdraw(double amount) throws InsufficientBalanceException{
        if (amount > balance)
            throw new InsufficientBalanceException();
        balance -= amount;
    }

    public void printCheck(double amount, String desc) throws InsufficientBalanceException{
        withdraw(amount);
    }

    public void transferTo(Account other, double amount) throws InsufficientBalanceException{
        withdraw(amount);
        other.deposit(amount);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}