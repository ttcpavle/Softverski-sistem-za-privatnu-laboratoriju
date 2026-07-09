/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;
import util.ConfigReader;

public class ConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPool.class.getName());

    private static ConnectionPool instance;
    private BlockingQueue<Connection> slobodneKonekcije;
    private final int MAX_KONEKCIJA;
    private final String URL;
    private final String USER;
    private final String PASS;

    private ConnectionPool() {
        ConfigReader cr = ConfigReader.getInstance();
        MAX_KONEKCIJA = Integer.parseInt(cr.getProperty("max_konekcija"));
        URL = cr.getProperty("db_url");
        USER = cr.getProperty("db_user");
        PASS = cr.getProperty("db_pass");

        slobodneKonekcije = new LinkedBlockingQueue<>(MAX_KONEKCIJA);
        inicijalizujPool();
    }

    // singleton connection pool
    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private void inicijalizujPool() {
        for (int i = 0; i < MAX_KONEKCIJA; i++) {
            try {
                Connection con = DriverManager.getConnection(URL, USER, PASS);
                con.setAutoCommit(false);
                slobodneKonekcije.offer(con);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Greska pri povezivanju sa bazom: " + e.getMessage());
                break;
            }
        }
    }

    // proverava da li je baza dostupna. Ako nije, resetuje ceo pool
    public synchronized boolean proveriKonekciju() {
        Connection con = null;
        try {
            con = slobodneKonekcije.poll(2, TimeUnit.SECONDS);
            if (con != null && con.isValid(2)) {
                slobodneKonekcije.offer(con);
                return true;
            }
            resetujPool();
            return !slobodneKonekcije.isEmpty();
        } catch (SQLException | InterruptedException e) {
            return false;
        }
    }

    private void resetujPool() {
        zatvoriSve();
        slobodneKonekcije.clear();
        inicijalizujPool();
    }

    // Daj konekciju klijentu
    public Connection getConnection() throws InterruptedException {
        return slobodneKonekcije.take();
    }

    // Vrati konekciju u pool
    public void returnConnection(Connection con) {
        if (con != null) {
            slobodneKonekcije.offer(con);
        }
    }

    // Zatvori sve konekcije
    public void zatvoriSve() {
        for (Connection con : slobodneKonekcije) {
            try {
                con.close();
            } catch (SQLException e) {
                //LOGGER.log(Level.WARNING, "Greska pri zatvaranju konekcija: " + e.getMessage());
            }
        }
    }
}
