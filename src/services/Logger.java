package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String INFO_PATH = "logs/info.txt";
    private static final String ERROR_PATH = "logs/errors.txt";
    
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z uuuu")
            .withZone(ZONE_ID);
    private static Logger instance;

    private PrintWriter infoWriter;
    private PrintWriter errorWriter;

    private Logger() {
        try {
            infoWriter = new PrintWriter(new FileWriter(INFO_PATH, true));
            errorWriter = new PrintWriter(new FileWriter(ERROR_PATH, true));
        } catch (IOException e) {
            System.out.println("An error occurred while initializing the log files.");
            e.printStackTrace();
        }
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logInfo(String message) {
        String log = getFormattedLog("INFO", message);
        infoWriter.println(log);
        infoWriter.flush();
    }

    public void logError(String message) {
        String log = getFormattedLog("ERROR", message);
        errorWriter.println(log);
        errorWriter.flush();
    }

    public void closeLogFiles() {
        infoWriter.close();
        errorWriter.close();
    }

    private String getFormattedLog(String logType, String message) {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZONE_ID);
        String formattedDate = zonedDateTime.format(LOG_DATE_FORMATTER);
        return "[" + formattedDate + "][" + logType + "] " + message;
    }

    
}
