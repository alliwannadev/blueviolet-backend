package com.blueviolet.backend.common.util;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DateTimeUtil {

    private static final String[] DATE_PATTERNS = {"yyyyMMdd", "yyyy-MM-dd"};

    public static LocalDate toLocalDate(String date) {
        return Arrays.stream(DATE_PATTERNS)
                .filter(pattern -> canParseToLocalDate(date, pattern))
                .map(pattern -> toLocalDate(date, pattern))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.DATE_TIME_PARSING_ERROR));
    }

    public static LocalDate toLocalDate(String date, String pattern) {
        return LocalDate.parse(date, getDateTimeFormatterByPattern(pattern));
    }

    private static boolean canParseToLocalDate(String date, String pattern) {
        try {
            LocalDate.parse(date, getDateTimeFormatterByPattern(pattern));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static DateTimeFormatter getDateTimeFormatterByPattern(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }
}
