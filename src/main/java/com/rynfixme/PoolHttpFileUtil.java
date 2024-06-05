package com.rynfixme;

import burp.api.montoya.persistence.PersistedObject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PoolHttpFileUtil {
    private final PersistedObject persist;

    public PoolHttpFileUtil(PersistedObject persist) {
        this.persist = persist;

        String dir = System.getProperty("user.home") + "/poolhttp";
        String fileName = this.createFileName();

        this.persist.setString(Main.STORE_DIR_NAME, dir);
        this.persist.setString(Main.STORE_FILE_NAME, fileName);
    }

    public String getCurrentDirectoryName() {
        String current = this.persist.getString(Main.STORE_DIR_NAME);
        return current;
    }

    public void setDirectoryName(String dir) {
        this.persist.setString(Main.STORE_DIR_NAME, dir);
    }

    public String getCurrentFileName() {
        String current = this.persist.getString(Main.STORE_FILE_NAME);
        return current;
    }


    public String getFullFilePath() {
        String dirRaw = this.persist.getString(Main.STORE_DIR_NAME);
        String fileNameRaw = this.persist.getString(Main.STORE_FILE_NAME);

        Path dirPath = Paths.get(dirRaw);
        Path fileNamePath = Paths.get(fileNameRaw);
        Path fullPath = dirPath.resolve(fileNamePath);

        return fullPath.toAbsolutePath().toString();
    }

    public String createFileName() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        LocalDateTime ldate = LocalDateTime.now();
        String d = dateTimeFormatter.format(ldate);

        String fileName = "poolhttp-" + d + ".txt";

        this.persist.setString(Main.STORE_FILE_NAME, fileName);
        return fileName;
    }
}
