package com.blueviolet.backend.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderNoUtil {
    public static String createOrderNo() {
        StringBuilder sb = new StringBuilder();
        sb.append(RandomCodeUtil.createRandomAlphabetsByLength(3));
        String nanoSecond = LocalDateTime.now().format(DateTimeFormatter.ofPattern("n"));
        sb.append(nanoSecond);
        sb.append(RandomCodeUtil.createRandomCodesByLength(3));

        return sb.toString();
    }
}
