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
            rootLogger.addHandler(fh);
            
            
            rootLogger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}