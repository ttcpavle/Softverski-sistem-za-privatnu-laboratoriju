/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author totic
 */
public class Common {
    
    public static String formatirajDatumZaSQL(LocalDate datum) {
        if (datum == null) {
            return "";
        }

        // MySQL/MariaDB DATETIME format: YYYY-MM-DD HH:MM:SS
        DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return datum.format(sqlFormatter);
    }

    public static String escapeSQL(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("'", "''");
    }
    
    
    public static boolean validPassword(String password){
        if (password.length() < 8) {
            return false;
        }
        return true;
    }
    
    public static boolean validJMBG(String jmbg) {
        if (!jmbg.matches("\\d{13}")) {
            return false;
        }
        return true;
    }
    
    public static boolean validEmail(String email) {
        if (!email.contains("@") || !email.contains(".")
                || email.indexOf('@') > email.lastIndexOf('.')) {
            return false;
        }
        return true;
    } 
}
