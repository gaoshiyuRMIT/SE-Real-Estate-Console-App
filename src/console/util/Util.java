package console.util;

import java.util.*;

import console.*;

public class Util {
    private static final int pageBreakLen = 60;
    private SEScanner scanner;

    public Util(SEScanner scanner) {
        this.scanner = scanner;
    }

    public String getPageBreak() {
        String ret = "";
        for (int i = 0; i < pageBreakLen; i++) {
            ret += "=";
        }
        return ret;
    }

    public String getPageBreak(String title) {
        String ret = "";
        int len = (pageBreakLen - title.length() - 2) / 2;
        for (int i = 0; i < len; i++)
            ret += "=";
        if (title.length() % 2 == 1)
            ret += "=";
        ret += " " + title + " ";
        for (int i = 0; i < len; i++)
            ret += "=";
        return ret;
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