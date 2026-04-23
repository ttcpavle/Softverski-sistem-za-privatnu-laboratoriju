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
            String upit = "INSERT INTO " + odo.vratiImeTabele() + "(" + odo.vratiNaziveKolona() + ") "
                    + " VALUES(" + odo.vratiVrednostiAtributa() + ");";
            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
            Statement st = con.createStatement();
            st.executeUpdate(upit, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                rezultat = generatedKeys.getInt(1);
            } else {
                rezultat = null;
            }
            st.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju pamtiSlog" + ex.getMessage(), ex);
            return false;
        }
        return true;
    }


    public boolean nadjiSlog(OpstiDomenskiObjekat odo) {
        try {
            String upit = "SELECT * FROM " + odo.vratiImeTabele() + " WHERE " + odo.vratiUslovZaNadjiSlog();
            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
            PreparedStatement ps = con.prepareStatement(upit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                OpstiDomenskiObjekat obj = odo.getClass().getDeclaredConstructor().newInstance();
                obj.popuniIzResultSet(rs);
                rezultat = obj;
                return true;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju nadjiSlog: " + ex.getMessage(),ex);
            rezultat = null;
        }
        return false;
    }


    public boolean vratiSve(OpstiDomenskiObjekat odo) {
        try {
            String upit = "SELECT * FROM " + odo.vratiImeTabele() + " WHERE " + odo.vratiUslovZaNadjiSlog() + ";";
            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
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
            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
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
        try {

            String upit = "UPDATE " + odo.vratiImeTabele()
                    + " SET " + odo.vratiVrednostiZaUpdate()
                    + " WHERE " + odo.vratiNazivKolonePK() + " = " + odo.vratiVrednostPK();

            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
            Statement st = con.createStatement();
            int izmenjenihRedova = st.executeUpdate(upit);
            st.close();

            if (izmenjenihRedova == 0) {
                LOGGER.log(Level.WARNING, "Nijedan slog nije izmenjen. Proverite da li slog postoji u bazi.");
                return false;
            }

            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju promeniSlog: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    
    /**
     * Brise jedan slog na osnovu kompozitnog ili prostog PK.
     * Primer generisanog upita za StavkaZahteva:
     *   DELETE FROM stavkazahteva WHERE idZahtev=5 AND rbStavka=2
     * Primer za Kupac:
     *   DELETE FROM kupac WHERE idKupac=3
     *
     * Metoda razlaže vratiNazivKolonePK() i vratiVrednostPK() po zarezima
     * i gradi WHERE klauzulu sa AND između parova kolona=vrednost.
     */    
    @Override
    public boolean obrisiSlog(OpstiDomenskiObjekat odo) {
        try {
            String[] kolone = odo.vratiNazivKolonePK().split(",");
            String[] vrednosti = odo.vratiVrednostPK().split(",");

            StringBuilder where = new StringBuilder();
            for (int i = 0; i < kolone.length; i++) {
                if (i > 0) {
                    where.append(" AND ");
                }
                where.append(kolone[i].trim()).append("=").append(vrednosti[i].trim());
            }

            String upit = "DELETE FROM " + odo.vratiImeTabele() + " WHERE " + where;
            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
            Statement st = con.createStatement();
            st.executeUpdate(upit);
            st.close();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju obrisiSlog: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    /**
     * Brise sve slogove koji zadovoljavaju uslov vratiUslovZaNadjiSlog().
     * Primer generisanog upita za brisanje svih stavki zahteva sa idZahtev=5:
     *   DELETE FROM stavkazahteva WHERE idZahtev=5
     */
    @Override
    public boolean obrisiSvePremaUslovu(OpstiDomenskiObjekat odo) {
        try {
            String uslov = odo.vratiUslovZaNadjiSlog();
            if (uslov == null || uslov.equals("1=1")) {
                LOGGER.log(Level.WARNING, "obrisiSvePremaUslovu odbijen - uslov je 1=1, odbijamo brisanje cele tabele");
                return false;
            }
            String upit = "DELETE FROM " + odo.vratiImeTabele() + " WHERE " + uslov;
            LOGGER.log(Level.INFO, "izvrsavanje upita: " + upit);
            Statement st = con.createStatement();
            st.executeUpdate(upit);
            st.close();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Greska pri izvrsenju obrisiSvePremaUslovu: " + ex.getMessage(), ex);
            return false;
        }
    }

}
