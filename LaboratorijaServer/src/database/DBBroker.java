/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import domen.OpstiDomenskiObjekat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author totic
 */
public class DBBroker implements Repository{

    private static final Logger LOGGER =  Logger.getLogger(DBBroker.class.getName());
    private Connection con;
    private Object rezultat; // ovde se cuva rezultat operacije nad bazom (uglavnom OpstiDomenskiObjekat ili lista)
    private boolean uspesnaKonekcija;

    public DBBroker() {
        try {
            this.con = ConnectionPool.getInstance().getConnection();
            this.uspesnaKonekcija = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.uspesnaKonekcija = false;
        }
    }

    public boolean zatvoriBazu() {
        try {
            // Vrati konekciju u pool, NE zatvaraj!
            con.rollback(); // Resetuj transakciju pre vraćanja
            ConnectionPool pool = ConnectionPool.getInstance();
            pool.returnConnection(con);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Konekcija nije vracena u pool: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean commitTransakcije() {
        try {
            con.commit();
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Neuspesan commit transakcije",ex );
            return false;
        }
        return true;
    }

    public boolean rollbackTransakcije() {
        try {
            con.rollback();
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Neuspesan rollback transakcije", ex);
            return false;
        }
        return true;
    }

    public Object getRezultat() {
        return rezultat;
    }

    public boolean isUspesnaKonekcija() {
        return uspesnaKonekcija;
    }

    public void setUspesnaKonekcija(boolean uspesnaKonekcija) {
        this.uspesnaKonekcija = uspesnaKonekcija;
    }
    
    // Implementirano tako da database broker ne vraca ResultSet i time logiku jdbc drzi izolovanom od ostatka koda
    
    
    @Override
    public boolean pamtiSlog(OpstiDomenskiObjekat odo) {
        try {
            String upit = "INSERT INTO " + odo.vratiImeTabele() + "(" + odo.vratiNaziveKolona() + ") " + " VALUES(" + odo.vratiVrednostiAtributa() + ");";
            LOGGER.log(Level.FINE, "izvrsavanje upita: " + upit);
            Statement st = con.createStatement();
            st.executeUpdate(upit);
            st.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju pamtiSlog" + ex.getMessage(),ex);
            return false;
        }
        return true;

    }


    public boolean nadjiSlog(OpstiDomenskiObjekat odo) {
        try {
            String upit = "SELECT * FROM " + odo.vratiImeTabele() + " WHERE " + odo.vratiUslovZaNadjiSlog();
            LOGGER.log(Level.FINE, "izvrsavanje upita: " + upit);
            PreparedStatement ps = con.prepareStatement(upit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                OpstiDomenskiObjekat obj = odo.getClass().getDeclaredConstructor().newInstance();
                obj.popuniIzResultSet(rs);
                rezultat = obj;
                return true;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju nadjiSlog" + ex.getMessage(),ex);
            rezultat = null;
        }
        return false;
    }


    public boolean vratiSve(OpstiDomenskiObjekat odo) {
        try {
            String upit = "SELECT * FROM " + odo.vratiImeTabele() + " WHERE " + odo.vratiUslovZaNadjiSlog() + ";";
            LOGGER.log(Level.FINE, "izvrsavanje upita: " + upit);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(upit);
            List<OpstiDomenskiObjekat> lista = new ArrayList<>();
            while(rs.next()) {
                OpstiDomenskiObjekat obj = odo.getClass().getDeclaredConstructor().newInstance();
                obj.popuniIzResultSet(rs);
                lista.add(obj);
            }
            rezultat = lista;
            s.close();
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju vratiSve" + ex.getMessage(),ex);
        }
        return false;
    }


    /*za odredjene klase zbog JOIN-a moraju se koristiti aliasi:
    mesto - mesto_naziv
    
    */
    public boolean vratiSvePremaUslovu(OpstiDomenskiObjekat odo,
            String tabela1,
            String joinType, 
            String tabela2,
            String joinCondition,
            String whereClause,
            String kolone) {
        try {
            StringBuilder upit = new StringBuilder("SELECT ");

            // Ako je prosledjeno "kolone", inace koristi "*"
            if (kolone != null && !kolone.trim().isEmpty()) {
                upit.append(kolone);
            } else {
                upit.append("*");
            }

            upit.append(" FROM ");
            if (tabela1 != null && !tabela1.trim().isEmpty()
                    && tabela2 != null && !tabela2.trim().isEmpty()
                    && joinCondition != null && !joinCondition.trim().isEmpty()) {

                upit.append(tabela1).append(" ").append(joinType.toUpperCase()).append(" JOIN ")
                        .append(tabela2).append(" ON (")
                        .append(joinCondition).append(") ");
            }else{
                return false;
            }

            if (whereClause != null && !whereClause.trim().isEmpty()) {
                upit.append(" WHERE ").append(whereClause);
            }
            LOGGER.log(Level.FINE, "izvrsavanje upita: " + upit);
            PreparedStatement ps = con.prepareStatement(upit.toString());
            ResultSet rs = ps.executeQuery();
            List<OpstiDomenskiObjekat> lista = new ArrayList<>();
            while (rs.next()) {
                OpstiDomenskiObjekat obj = odo.getClass().getDeclaredConstructor().newInstance();
                // moguci problem: kolone sa istim nazivom koje se ne odnose na iste podatke
                obj.popuniIzResultSet(rs);
                lista.add(obj);
            }
            rezultat = lista;
            ps.close();
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju vratiSvePremaUslovu" + ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean promeniSlog(OpstiDomenskiObjekat odo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
