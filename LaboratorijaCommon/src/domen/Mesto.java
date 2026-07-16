package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mesto implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idMesto;
    private int zipKod;
    private String naziv;
    
    private List<Kupac> kupci;
    
    public Mesto() {
        this.kupci = new ArrayList<>();
    }
    
    @Override
    public String vratiImeTabele() {
        return "mesto";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idMesto, zipKod, naziv";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idMesto > 0 ? idMesto : "NULL").append(", ");
        sb.append(zipKod).append(", ");
        sb.append("'").append(naziv).append("'");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "zipKod=" + zipKod + ", naziv='" + naziv + "'";
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idMesto";
    }
    
    @Override
    public String vratiVrednostPK() {
        return String.valueOf(idMesto);
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        List<String> conditions = new ArrayList<>();
        
        if (idMesto > 0) {
            conditions.add("idMesto=" + idMesto);
        }
        if (zipKod > 0) {
            conditions.add("zipKod=" + zipKod);
        }
        if (naziv != null && !naziv.isEmpty()) {
            conditions.add("naziv LIKE '%" + naziv + "%'");
        }
        
        return conditions.isEmpty() ? "1=1" : String.join(" AND ", conditions);
    }
    
    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idMesto = rs.getInt("idMesto");
        this.zipKod = rs.getInt("zipKod");
        this.naziv = rs.getString("naziv");
    }
    
    public int getIdMesto() { return idMesto; }
    public void setIdMesto(int idMesto) { this.idMesto = idMesto; }
    
    public int getZipKod() { return zipKod; }
    public void setZipKod(int zipKod) { this.zipKod = zipKod; }
    
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    
    public List<Kupac> getKupci() { return kupci; }
    public void setKupci(List<Kupac> kupci) { this.kupci = kupci; }

    @Override
    public String toString() {
        String tekstMesta = (naziv != null) ? naziv : "--- SVA MESTA ---";
        return tekstMesta + " " + zipKod;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
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
        final Mesto other = (Mesto) obj;
        if (this.idMesto != other.idMesto) {
            return false;
        }
        if (this.zipKod != other.zipKod) {
            return false;
        }
        return Objects.equals(this.naziv, other.naziv);
    }
    
    
}
