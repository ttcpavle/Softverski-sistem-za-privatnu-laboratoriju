package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proizvod implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idProizvod;
    private String naziv;
    private int vremeCekanjaSati; // ocekivano vreme cekanja na rezultate, u satima (npr. 72 = 3 dana)
    private double cena;
    private String opis;
    
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
        return "idProizvod, naziv, vremeCekanjaSati, cena, opis";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idProizvod > 0 ? idProizvod : "NULL").append(", ");
        sb.append("'").append(naziv).append("', ");
        sb.append(vremeCekanjaSati).append(", ");
        sb.append(cena).append(", ");
        sb.append(opis != null ? "'" + opis + "'" : "NULL");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "naziv='" + naziv + "', vremeCekanjaSati=" + vremeCekanjaSati + 
               ", cena=" + cena + ", opis=" + (opis != null ? "'" + opis + "'" : "NULL");
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
        this.vremeCekanjaSati = rs.getInt("vremeCekanjaSati");
        this.cena = rs.getDouble("cena");
        this.opis = rs.getString("opis");
    }
    
    // Getters and Setters
    public int getIdProizvod() { return idProizvod; }
    public void setIdProizvod(int idProizvod) { this.idProizvod = idProizvod; }
    
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    
    public int getVremeCekanjaSati() { return vremeCekanjaSati; }
    public void setVremeCekanjaSati(int vremeCekanjaSati) { this.vremeCekanjaSati = vremeCekanjaSati; }
    
    public double getCena() { return cena; }
    public void setCena(double cena) { this.cena = cena; }
    
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    
    public List<StavkaZahteva> getStavke() { return stavke; }
    public void setStavke(List<StavkaZahteva> stavke) { this.stavke = stavke; }
    
    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Proizvod other = (Proizvod) obj;
        if (this.idProizvod != other.idProizvod) {
            return false;
        }
        return Objects.equals(this.naziv, other.naziv);
    }
}