package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
