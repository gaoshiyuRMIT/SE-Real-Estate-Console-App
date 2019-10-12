package util;

import java.util.*;

public class StringUtil {
    public static String join(String del, List<? extends Object> l) {
        List<String> ls = new ArrayList<String>();
        for (Object o : l) {
            ls.add(o.toString());
        }
        return String.join(del, ls);
    }
}