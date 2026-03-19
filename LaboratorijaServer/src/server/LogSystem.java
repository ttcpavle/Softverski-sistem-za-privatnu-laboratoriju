/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author totic
 */
import java.util.logging.*;
import java.io.IOException;

public class LogSystem {
    public static void setup() {
        Logger rootLogger = Logger.getLogger("");
        try {
            // file output for logs
            FileHandler fh = new FileHandler("server.log", true);
            fh.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fh);
            
            
            rootLogger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}