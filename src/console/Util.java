package console;

import java.util.*;

public class Util {
    private Scanner scanner;

    public Util(Scanner scanner) {
        this.scanner = scanner;
    }

    public String displayMenu(List<String> menuOptions) throws InvalidInputException{
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.printf("%-30d: %s\n", i+1, menuOptions.get(i));
        }
        System.out.print("Enter your choice: ");
        int op = scanner.nextInt();
        if (op < 1 || op > menuOptions.size())
            throw new InvalidInputException(
                "Your choice must be a number displayed on the menu."
            );
        return menuOptions.get(op - 1);
    }
}