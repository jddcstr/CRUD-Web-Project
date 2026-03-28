package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SystemLogger {
    
    public static Logger setupLogger(String className, String appRootPath) {
        Logger logger = Logger.getLogger(className);
        
        try {
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            
            String logDirPath = appRootPath + File.separator + "log";
            File logDir = new File(logDirPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            String logFilePath = logDirPath + File.separator + "Log_" + dateStr + ".log";
            
            if (logger.getHandlers().length == 0) {
                FileHandler fileHandler = new FileHandler(logFilePath, true); 
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
                logger.setLevel(Level.INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return logger;
    }
}