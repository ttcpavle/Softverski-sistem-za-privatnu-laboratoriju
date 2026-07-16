// ZahtevZaAnalizu.java
package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ZahtevZaAnalizu implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idZahtev;
    private LocalDate datum;
    private String status;
    private boolean prioritet;
    private double ukupnaCenaZahteva;
    
    // Veze
    private Radnik radnik;
    private Kupac kupac;
    private List<StavkaZahteva> stavke;
    
    public ZahtevZaAnalizu() {
        this.stavke = new ArrayList<>();
    }
    
    @Override
    public String vratiImeTabele() {
        return "zahtevzaanalizu";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idZahtev, datum, status, prioritet, ukupnaCenaZahteva, idRadnik, idKupac";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idZahtev > 0 ? idZahtev : "NULL").append(", ");
        sb.append("'").append(datum.toString()).append("', ");
        sb.append("'").append(status).append("', ");
        sb.append(prioritet ? 1 : 0).append(", ");
        sb.append(ukupnaCenaZahteva).append(", ");
        sb.append(radnik != null ? radnik.getIdRadnik() : "NULL").append(", ");
        sb.append(kupac != null ? kupac.getIdKupac() : "NULL");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "datum='" + datum + "', status='" + status + "', prioritet=" + (prioritet ? 1 : 0) +
               ", ukupnaCenaZahteva=" + ukupnaCenaZahteva + 
               ", idRadnik=" + (radnik != null ? radnik.getIdRadnik() : "NULL") +
               ", idKupac=" + (kupac != null ? kupac.getIdKupac() : "NULL");
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idZahtev";
    }
    
    @Override
    public String vratiVrednostPK() {
        return String.valueOf(idZahtev);
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        List<String> conditions = new ArrayList<>();
        
        if (idZahtev > 0) {
            conditions.add("idZahtev=" + idZahtev);
        }
        if (datum != null) {
            conditions.add("datum='" + datum.toString() + "'");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("status='" + status + "'");
        }
        if (radnik != null && radnik.getIdRadnik() > 0) {
            conditions.add("idRadnik=" + radnik.getIdRadnik());
        }
        if (kupac != null && kupac.getIdKupac() > 0) {
            conditions.add("idKupac=" + kupac.getIdKupac());
        }
        
        return conditions.isEmpty() ? "1=1" : String.join(" AND ", conditions);
    }
    
@Override
public void popuniIzResultSet(ResultSet rs) throws SQLException {
    this.idZahtev = rs.getInt("idZahtev");

    java.sql.Date sqlDate = rs.getDate("datum");
    if (sqlDate != null) {
        this.datum = sqlDate.toLocalDate();
    }

    this.status = rs.getString("status");
    this.prioritet = rs.getBoolean("prioritet");
    this.ukupnaCenaZahteva = rs.getDouble("ukupnaCenaZahteva");

    int idRadnik = rs.getInt("idRadnik");
    if (!rs.wasNull()) {
        this.radnik = new Radnik();
        this.radnik.setIdRadnik(idRadnik);

        if (hasColumn(rs, "radnik_ime")) {
            this.radnik.setIme(rs.getString("radnik_ime"));
        }
        if (hasColumn(rs, "radnik_prezime")) {
            this.radnik.setPrezime(rs.getString("radnik_prezime"));
        }
        if (hasColumn(rs, "radnik_JMBG")) {
            this.radnik.setJMBG(rs.getString("radnik_JMBG"));
        }
        if (hasColumn(rs, "radnik_korisnickoIme")) {
            this.radnik.setKorisnickoIme(rs.getString("radnik_korisnickoIme"));
        }
        // lozinka nam ne treba
    }

    int idKupac = rs.getInt("idKupac");
    if (!rs.wasNull()) {
        this.kupac = new Kupac();
        this.kupac.setIdKupac(idKupac);

        if (hasColumn(rs, "kupac_ime")) {
            this.kupac.setIme(rs.getString("kupac_ime"));
        }
        if (hasColumn(rs, "kupac_prezime")) {
            this.kupac.setPrezime(rs.getString("kupac_prezime"));
        }
        if (hasColumn(rs, "kupac_mail")) {
            this.kupac.setMail(rs.getString("kupac_mail"));
        }
        if (hasColumn(rs, "kupac_telefon")) {
            this.kupac.setTelefon(rs.getString("kupac_telefon"));
        }
        if (hasColumn(rs, "kupac_datumRodjenja")) {
            java.sql.Date sqlDatumRodjenja = rs.getDate("kupac_datumRodjenja");
            if (sqlDatumRodjenja != null) {
                this.kupac.setDatumRodjenja(sqlDatumRodjenja.toLocalDate());
            }
        }
        if (hasColumn(rs, "kupac_idMesto")) {
            int idMesto = rs.getInt("kupac_idMesto");
            if (!rs.wasNull()) {
                domen.Mesto mesto = new domen.Mesto();
                mesto.setIdMesto(idMesto);

                if (hasColumn(rs, "kupac_mesto_naziv")) {
                    mesto.setNaziv(rs.getString("kupac_mesto_naziv"));
                }
                if (hasColumn(rs, "kupac_mesto_zipKod")) {
                    mesto.setZipKod(rs.getInt("kupac_mesto_zipKod"));
                }
                this.kupac.setMesto(mesto);
            }
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
    
    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public boolean isPrioritet() { return prioritet; }
    public void setPrioritet(boolean prioritet) { this.prioritet = prioritet; }
    
    public double getUkupnaCenaZahteva() { return ukupnaCenaZahteva; }
    public void setUkupnaCenaZahteva(double ukupnaCenaZahteva) { this.ukupnaCenaZahteva = ukupnaCenaZahteva; }
    
    public Radnik getRadnik() { return radnik; }
    public void setRadnik(Radnik radnik) { this.radnik = radnik; }
    
    public Kupac getKupac() { return kupac; }
    public void setKupac(Kupac kupac) { this.kupac = kupac; }
    
    public List<StavkaZahteva> getStavke() { return stavke; }
    public void setStavke(List<StavkaZahteva> stavke) { this.stavke = stavke; }

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
        final ZahtevZaAnalizu other = (ZahtevZaAnalizu) obj;
        return this.idZahtev == other.idZahtev;
    }
    
    
}
