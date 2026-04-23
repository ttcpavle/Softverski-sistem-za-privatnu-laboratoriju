/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Kupac.java
package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Kupac implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idKupac;
    private String ime;
    private String prezime;
    private String mail;
    private String telefon;
    private LocalDate datumRodjenja;
    
    // Veze
    private Mesto mesto;
    private List<ZahtevZaAnalizu> zahtevi;
    
    public Kupac() {
        this.zahtevi = new ArrayList<>();
    }
    
    @Override
    public String vratiImeTabele() {
        return "kupac";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idKupac, ime, prezime, mail, telefon, datumRodjenja, idMesto";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idKupac > 0 ? idKupac : "NULL").append(", ");
        sb.append("'").append(ime).append("', ");
        sb.append("'").append(prezime).append("', ");
        sb.append("'").append(mail).append("', ");
        sb.append("'").append(telefon).append("', ");
        sb.append("'").append(datumRodjenja.toString()).append("', ");
        sb.append(mesto != null ? mesto.getIdMesto() : "NULL");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "ime='" + ime + "', prezime='" + prezime + "', mail='" + mail + 
               "', telefon='" + telefon + "', datumRodjenja='" + datumRodjenja + 
               "', idMesto=" + (mesto != null ? mesto.getIdMesto() : "NULL");
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idKupac";
    }
    
    @Override
    public String vratiVrednostPK() {
        return String.valueOf(idKupac);
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        List<String> conditions = new ArrayList<>();
        
        if (idKupac > 0) {
            conditions.add("idKupac=" + idKupac);
        }
        if (ime != null && !ime.isEmpty()) {
            conditions.add("ime LIKE '%" + ime + "%'");
        }
        if (prezime != null && !prezime.isEmpty()) {
            conditions.add("prezime LIKE '%" + prezime + "%'");
        }
        if (mail != null && !mail.isEmpty()) {
            conditions.add("mail='" + mail + "'");
        }
        if (telefon != null && !telefon.isEmpty()) {
            conditions.add("telefon='" + telefon + "'");
        }
        if (mesto != null && mesto.getIdMesto() > 0) {
            conditions.add("idMesto=" + mesto.getIdMesto());
        }
        
        return conditions.isEmpty() ? "1=1" : String.join(" AND ", conditions);
    }
    
    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idKupac = rs.getInt("idKupac");
        this.ime = rs.getString("ime");
        this.prezime = rs.getString("prezime");
        this.mail = rs.getString("mail");
        this.telefon = rs.getString("telefon");
        
        java.sql.Date sqlDate = rs.getDate("datumRodjenja");
        if (sqlDate != null) {
            this.datumRodjenja = sqlDate.toLocalDate();
        }
        
        // Mesto će biti naknadno učitano ili kroz JOIN
        int idMesto = rs.getInt("idMesto");
        if (!rs.wasNull()) {
            this.mesto = new Mesto();
            this.mesto.setIdMesto(idMesto);
            
            // Ako su podaci o mestu uključeni u JOIN
            if (hasColumn(rs, "mesto_naziv")) {
                this.mesto.setNaziv(rs.getString("mesto_naziv"));
            }
            if (hasColumn(rs, "zipKod")) {
                this.mesto.setZipKod(rs.getInt("zipKod"));
            }
        }
    }
    
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Getters and Setters
    public int getIdKupac() { return idKupac; }
    public void setIdKupac(int idKupac) { this.idKupac = idKupac; }
    
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    
    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    
    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }
    
    public Mesto getMesto() { return mesto; }
    public void setMesto(Mesto mesto) { this.mesto = mesto; }
    
    public List<ZahtevZaAnalizu> getZahtevi() { return zahtevi; }
    public void setZahtevi(List<ZahtevZaAnalizu> zahtevi) { this.zahtevi = zahtevi; }
    
    @Override
    public String toString() {
        return ime + " " + prezime;
    }
}
