package util;

import java.time.*;

public class LocalDateTimeUtil {
    public static LocalDateTime extractMonth(LocalDateTime date) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), 1, 0, 0);
    }
}