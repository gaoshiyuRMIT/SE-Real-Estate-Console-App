package console.util;

import java.util.*;
import java.io.*;

import console.*;

public class SEScanner {
    Scanner scanner;

    public SEScanner(InputStream is) {
        scanner = new Scanner(is);
    }

    public String next() {
        return scanner.next();
    }

    public int nextInt() throws InvalidInputException{
        int ret;
        try {
            ret = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new InvalidInputException(
                "Invalid input: expected a whole number/integer.", e
            );
        }
        return ret;
    }

    public double nextDouble() throws InvalidInputException{
        double ret;
        try {
            ret = scanner.nextDouble();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new InvalidInputException("Invalid input: expected a number.", e);
        }
        return ret;
    }
}