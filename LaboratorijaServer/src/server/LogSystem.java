package server;

import java.util.logging.*;
import java.io.IOException;

public class LogSystem {
    public static void setup() {
        Logger rootLogger = Logger.getLogger("");
        try {
            // file output
            FileHandler fh = new FileHandler("server.log", true);
            fh.setFormatter(new SimpleFormatter());
            System.setProperty("java.util.logging.SimpleFormatter.format", 
    "[%1$tF %1$tT] [%4$s] %5$s%6$s%n");
            rootLogger.addHandler(fh);
            
            
            rootLogger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}