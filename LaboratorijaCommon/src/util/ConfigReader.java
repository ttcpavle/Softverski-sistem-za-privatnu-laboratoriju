/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author totic
 */
public class ConfigReader {

    private static final Logger LOGGER = Logger.getLogger(ConfigReader.class.getName());
    
    private final Properties config = new Properties();
    private static ConfigReader configReader;
    
    public static ConfigReader getInstance(){
        if(configReader == null){
            configReader = new ConfigReader();
        }
        return configReader;
    }
    
    public ConfigReader(){
        try (FileInputStream input = new FileInputStream("config.properties")) {
            config.load(input);
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Nije pronadjen konfiguracioni fajl", ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Neuspesno citanje konfiguracija", ex);
        }
    }

    public String getProperty(String key){
        return config.getProperty(key);
    }
}
