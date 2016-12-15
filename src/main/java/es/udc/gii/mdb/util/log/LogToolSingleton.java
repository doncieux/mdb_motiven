package es.udc.gii.mdb.util.log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mastropiero
 */


public class LogToolSingleton {
    
    
    private static LogToolSingleton instance = null;
    private List<LogTool> logs;
   
    
    public static LogToolSingleton getInstance() {
        if (instance == null) {
            instance = new LogToolSingleton();
        }
        return instance;
    }

    private LogToolSingleton() {
        logs = new ArrayList<>();
    }
    
    
    public void addLog(LogTool log) {
        logs.add(log);
    }
    
    
    public void closeLogs() {
        for (LogTool log : logs) {
            log.close();
        }
    }
    
    
    
}
