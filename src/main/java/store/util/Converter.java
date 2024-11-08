package store.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converter {
    private static final String ERROR_NUMBER_FORMAT = "[ERROR] '%s'는 숫자 형식이 아닙니다.";

    public Converter() {

    }

    public int toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(ERROR_NUMBER_FORMAT, value));
        }
    }

    public LocalDateTime toLocalDateTime(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(value, formatter).atStartOfDay();
    }
}
