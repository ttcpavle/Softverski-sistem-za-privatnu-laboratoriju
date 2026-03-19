package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Proizvod implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idProizvod;
    private String naziv;
    private LocalTime vremeIzdavanjaRez;
    private double cena;
    
    private List<StavkaZahteva> stavke;
    
    public Proizvod() {
        this.stavke = new ArrayList<>();
    }
    
    @Override
    public String vratiImeTabele() {
        return "proizvod";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idProizvod, naziv, vremeIzdavanjaRez, cena";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idProizvod > 0 ? idProizvod : "NULL").append(", ");
        sb.append("'").append(naziv).append("', ");
        sb.append("'").append(vremeIzdavanjaRez.toString()).append("', ");
        sb.append(cena);
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "naziv='" + naziv + "', vremeIzdavanjaRez='" + vremeIzdavanjaRez + 
               "', cena=" + cena;
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idProizvod";
    }
    
    @Override
    public String vratiVrednostPK() {
        return String.valueOf(idProizvod);
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        List<String> conditions = new ArrayList<>();
        
        if (idProizvod > 0) {
            conditions.add("idProizvod=" + idProizvod);
        }
        if (naziv != null && !naziv.isEmpty()) {
            conditions.add("naziv LIKE '%" + naziv + "%'");
        }
        
        return conditions.isEmpty() ? "1=1" : String.join(" AND ", conditions);
    }
    
    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idProizvod = rs.getInt("idProizvod");
        this.naziv = rs.getString("naziv");
        
        Time time = rs.getTime("vremeIzdavanjaRez");
        if (time != null) {
            this.vremeIzdavanjaRez = time.toLocalTime();
        }
        
        this.cena = rs.getDouble("cena");
    }
    
    // Getters and Setters
    public int getIdProizvod() { return idProizvod; }
    public void setIdProizvod(int idProizvod) { this.idProizvod = idProizvod; }
    
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    
    public LocalTime getVremeIzdavanjaRez() { return vremeIzdavanjaRez; }
    public void setVremeIzdavanjaRez(LocalTime vremeIzdavanjaRez) { this.vremeIzdavanjaRez = vremeIzdavanjaRez; }
    
    public double getCena() { return cena; }
    public void setCena(double cena) { this.cena = cena; }
    
    public List<StavkaZahteva> getStavke() { return stavke; }
    public void setStavke(List<StavkaZahteva> stavke) { this.stavke = stavke; }
}
