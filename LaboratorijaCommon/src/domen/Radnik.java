/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Radnik.java
package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Radnik implements OpstiDomenskiObjekat, Serializable {

    private static final long serialVersionUID = 1L;

    private int idRadnik;
    private String JMBG;
    private String ime;
    private String prezime;
    private String korisnickoIme;
    private String lozinka;
    private Boolean admin;

    // Veze - nećemo ih direktno čuvati u bazi, već preko stranih ključeva
    private List<ZahtevZaAnalizu> zahtevi;
    private List<Ovlascenje> ovlascenja;

    public Radnik() {
        this.zahtevi = new ArrayList<>();
        this.ovlascenja = new ArrayList<>();
    }

    public Radnik(int idRadnik) {
        this.idRadnik = idRadnik;
        this.zahtevi = new ArrayList<>();
        this.ovlascenja = new ArrayList<>();
    }

    
    
    @Override
    public String vratiImeTabele() {
        return "radnik";
    }

    @Override
    public String vratiNaziveKolona() {
        return "idRadnik, JMBG, ime, prezime, korisnickoIme, lozinka, admin";
    }

    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();

        // idRadnik
        sb.append(idRadnik > 0 ? idRadnik : "NULL").append(", ");

        // JMBG
        sb.append("'").append(JMBG).append("', ");

        // ime
        sb.append("'").append(ime).append("', ");

        // prezime
        sb.append("'").append(prezime).append("', ");

        // korisnickoIme
        sb.append("'").append(korisnickoIme).append("', ");

        // lozinka
        sb.append("'").append(lozinka).append("', ");

        // admin
        sb.append(admin != null && admin ? 1 : 0);

        return sb.toString();
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        return "JMBG='" + JMBG + "', ime='" + ime + "', prezime='" + prezime
                + "', korisnickoIme='" + korisnickoIme + "', lozinka='" + lozinka
                + "', admin=" + (admin != null && admin ? 1 : 0);
    }

    @Override
    public String vratiNazivKolonePK() {
        return "idRadnik";
    }

    @Override
    public String vratiVrednostPK() {
        return String.valueOf(idRadnik);
    }

    @Override
    public String vratiUslovZaNadjiSlog() {
        List<String> conditions = new ArrayList<>();

        if (idRadnik > 0) {
            conditions.add("idRadnik=" + idRadnik);
        }
        if (JMBG != null && !JMBG.isEmpty()) {
            conditions.add("JMBG='" + JMBG + "'");
        }
        if (ime != null && !ime.isEmpty()) {
            conditions.add("ime LIKE '%" + ime + "%'");
        }
        if (prezime != null && !prezime.isEmpty()) {
            conditions.add("prezime LIKE '%" + prezime + "%'");
        }
        if (korisnickoIme != null && !korisnickoIme.isEmpty()) {
            conditions.add("korisnickoIme='" + korisnickoIme + "'");
        }
        // DODATO: Provera za lozinku
        if (lozinka != null && !lozinka.isEmpty()) {
            conditions.add("lozinka='" + lozinka + "'");
        }
        // admin je Boolean wrapper - null znaci da kriterijum nije zadat
        if (admin != null) {
            conditions.add("admin=" + (admin ? 1 : 0));
        }

        if (conditions.isEmpty()) {
            return "1=1";
        }
        return String.join(" AND ", conditions);
    }

    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idRadnik = rs.getInt("idRadnik");
        this.JMBG = rs.getString("JMBG");
        this.ime = rs.getString("ime");
        this.prezime = rs.getString("prezime");
        this.korisnickoIme = rs.getString("korisnickoIme");
        this.lozinka = rs.getString("lozinka");
        this.admin = rs.getBoolean("admin");
    }

    // Getters and Setters
    public int getIdRadnik() {
        return idRadnik;
    }

    public void setIdRadnik(int idRadnik) {
        this.idRadnik = idRadnik;
    }

    public String getJMBG() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG = JMBG;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<ZahtevZaAnalizu> getZahtevi() {
        return zahtevi;
    }

    public void setZahtevi(List<ZahtevZaAnalizu> zahtevi) {
        this.zahtevi = zahtevi;
    }

    public List<Ovlascenje> getOvlascenja() {
        return ovlascenja;
    }

    public void setOvlascenja(List<Ovlascenje> ovlascenja) {
        this.ovlascenja = ovlascenja;
    }
    
    @Override
    public String toString() {
        return ime + " " + prezime;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Radnik other = (Radnik) obj;
        if (this.idRadnik != other.idRadnik) {
            return false;
        }
        return Objects.equals(this.JMBG, other.JMBG);
    }

    
    
}