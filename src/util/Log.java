package util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static Log instance;
    private StringBuffer logBuffer;
    
    private Log() {
        logBuffer = new StringBuffer();
    }
    
    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }
    
    public void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logBuffer.append(timestamp).append(" - ").append(message).append("\n");
    }
    
    public void saveLogs() {
        try (FileWriter writer = new FileWriter("parcel_system_log.txt")) {
            writer.write(logBuffer.toString());
        } catch (IOException e) {
            System.err.println("Error saving logs: " + e.getMessage());
        }
    }
    public String getLogContents() {
        return logBuffer.toString();
    }

    public void clearLog() {
        logBuffer = new StringBuffer();
    }
}
