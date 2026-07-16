// StavkaZahteva.java
package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StavkaZahteva implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Kompozitni ključ
    private int idZahtev;
    private int rbStavka;
    
    private int kolicina;
    private double jedinicnaCena;
    private double ukupnaCena;
    
    // Veze
    private ZahtevZaAnalizu zahtev;
    private Proizvod proizvod;

    public StavkaZahteva(ZahtevZaAnalizu zahtev) {
        this.zahtev = zahtev;
        if (zahtev != null) {
            this.idZahtev = zahtev.getIdZahtev();
        }
    }

    public StavkaZahteva() {
    }
    
    
    
    
    @Override
    public String vratiImeTabele() {
        return "stavkazahteva";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idZahtev, rbStavka, kolicina, jedinicnaCena, ukupnaCena, idProizvod";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idZahtev > 0 ? idZahtev : "NULL").append(", ");
        sb.append(rbStavka > 0 ? rbStavka : "NULL").append(", ");
        sb.append(kolicina).append(", ");
        sb.append(jedinicnaCena).append(", ");
        sb.append(ukupnaCena).append(", ");
        sb.append(proizvod != null ? proizvod.getIdProizvod() : "NULL");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "kolicina=" + kolicina + ", jedinicnaCena=" + jedinicnaCena + 
               ", ukupnaCena=" + ukupnaCena + 
               ", idProizvod=" + (proizvod != null ? proizvod.getIdProizvod() : "NULL");
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idZahtev, rbStavka"; 
    }
    
    @Override
    public String vratiVrednostPK() {
        return idZahtev + ", " + rbStavka;
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        StringBuilder sb = new StringBuilder();
        
        if (idZahtev > 0) {
            sb.append("idZahtev=").append(idZahtev);
        }
        if (rbStavka > 0) {
            if (sb.length() > 0) sb.append(" AND ");
            sb.append("rbStavka=").append(rbStavka);
        }
        if (proizvod != null && proizvod.getIdProizvod() > 0) {
            if (sb.length() > 0) sb.append(" AND ");
            sb.append("idProizvod=").append(proizvod.getIdProizvod());
        }
        
        return sb.length() > 0 ? sb.toString() : "1=1";
    }
    
    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idZahtev = rs.getInt("idZahtev");
        this.rbStavka = rs.getInt("rbStavka");
        this.kolicina = rs.getInt("kolicina");
        this.jedinicnaCena = rs.getDouble("jedinicnaCena");
        this.ukupnaCena = rs.getDouble("ukupnaCena");
        
        if (idZahtev > 0) {
            this.zahtev = new ZahtevZaAnalizu();
            this.zahtev.setIdZahtev(idZahtev);
        }
        
        int idProizvod = rs.getInt("idProizvod");
        if (!rs.wasNull()) {
            this.proizvod = new Proizvod();
            this.proizvod.setIdProizvod(idProizvod);
            
            if (hasColumn(rs, "naziv")) {
                this.proizvod.setNaziv(rs.getString("naziv"));
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
    public int getIdZahtev() { return idZahtev; }
    public void setIdZahtev(int idZahtev) { this.idZahtev = idZahtev; }
    
    public int getRbStavka() { return rbStavka; }
    public void setRbStavka(int rbStavka) { this.rbStavka = rbStavka; }
    
    public int getKolicina() { return kolicina; }
    public void setKolicina(int kolicina) { this.kolicina = kolicina; }
    
    public double getJedinicnaCena() { return jedinicnaCena; }
    public void setJedinicnaCena(double jedinicnaCena) { this.jedinicnaCena = jedinicnaCena; }
    
    public double getUkupnaCena() { return ukupnaCena; }
    public void setUkupnaCena(double ukupnaCena) { this.ukupnaCena = ukupnaCena; }
    
    public ZahtevZaAnalizu getZahtev() { return zahtev; }
    public void setZahtev(ZahtevZaAnalizu zahtev) { 
        this.zahtev = zahtev;
        if (zahtev != null) {
            this.idZahtev = zahtev.getIdZahtev();
        }
    }
    
    public Proizvod getProizvod() { return proizvod; }
    public void setProizvod(Proizvod proizvod) { 
        this.proizvod = proizvod;
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
        final StavkaZahteva other = (StavkaZahteva) obj;
        if (this.idZahtev != other.idZahtev) {
            return false;
        }
        return this.rbStavka == other.rbStavka;
    }
    
    
}
