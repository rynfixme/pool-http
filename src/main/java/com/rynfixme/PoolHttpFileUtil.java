package com.rynfixme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PoolHttpFileUtil {
    public PoolHttpFileUtil() {}

    public String createFileName() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        LocalDateTime ldate = LocalDateTime.now();
        String d = dateTimeFormatter.format(ldate);

        String fileName = "poolhttp-" + d + ".txt";
        return fileName;
    }
}
