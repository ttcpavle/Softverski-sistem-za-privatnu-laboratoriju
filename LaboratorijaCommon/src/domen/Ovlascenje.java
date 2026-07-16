package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Ovlascenje implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Kompozitni ključ
    private int idRadnik;
    private int idTipUsluge;
    
    private LocalDate datumOd;
    private LocalDate datumDo;
    
    // Veze
    private Radnik radnik;
    private TipUsluge tipUsluge;
    
    @Override
    public String vratiImeTabele() {
        return "ovlascenje";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idRadnik, idTipUsluge, datumOd, datumDo";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idRadnik > 0 ? idRadnik : "NULL").append(", ");
        sb.append(idTipUsluge > 0 ? idTipUsluge : "NULL").append(", ");
        sb.append("'").append(datumOd.toString()).append("', ");
        sb.append("'").append(datumDo.toString()).append("'");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "datumOd='" + datumOd + "', datumDo='" + datumDo + "'";
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idRadnik, idTipUsluge";  // Kompozitni ključ
    }
    
    @Override
    public String vratiVrednostPK() {
        return idRadnik + ", " + idTipUsluge;
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        StringBuilder sb = new StringBuilder();
        
        if (idRadnik > 0) {
            sb.append("idRadnik=").append(idRadnik);
        }
        if (idTipUsluge > 0) {
            if (sb.length() > 0) sb.append(" AND ");
            sb.append("idTipUsluge=").append(idTipUsluge);
        }
        
        return sb.length() > 0 ? sb.toString() : "1=1";
    }
    
    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idRadnik = rs.getInt("idRadnik");
        this.idTipUsluge = rs.getInt("idTipUsluge");
        
        java.sql.Date sqlDateOd = rs.getDate("datumOd");
        if (sqlDateOd != null) {
            this.datumOd = sqlDateOd.toLocalDate();
        }
        
        java.sql.Date sqlDateDo = rs.getDate("datumDo");
        if (sqlDateDo != null) {
            this.datumDo = sqlDateDo.toLocalDate();
        }
        
        // Radnik (ako je uključen u JOIN)
        if (hasColumn(rs, "radnik_ime")) {
            this.radnik = new Radnik();
            this.radnik.setIdRadnik(idRadnik);
            this.radnik.setIme(rs.getString("radnik_ime"));
            this.radnik.setPrezime(rs.getString("radnik_prezime"));
        }
        
        // TipUsluge (ako je uključen u JOIN)
        if (hasColumn(rs, "nazivUsluge")) {
            this.tipUsluge = new TipUsluge();
            this.tipUsluge.setIdTipUsluge(idTipUsluge);
            this.tipUsluge.setNazivUsluge(rs.getString("nazivUsluge"));
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
    public int getIdRadnik() { return idRadnik; }
    public void setIdRadnik(int idRadnik) { this.idRadnik = idRadnik; }
    
    public int getIdTipUsluge() { return idTipUsluge; }
    public void setIdTipUsluge(int idTipUsluge) { this.idTipUsluge = idTipUsluge; }
    
    public LocalDate getDatumOd() { return datumOd; }
    public void setDatumOd(LocalDate datumOd) { this.datumOd = datumOd; }
    
    public LocalDate getDatumDo() { return datumDo; }
    public void setDatumDo(LocalDate datumDo) { this.datumDo = datumDo; }
    
    public Radnik getRadnik() { return radnik; }
    public void setRadnik(Radnik radnik) { 
        this.radnik = radnik;
        if (radnik != null) {
            this.idRadnik = radnik.getIdRadnik();
        }
    }
    
    public TipUsluge getTipUsluge() { return tipUsluge; }
    public void setTipUsluge(TipUsluge tipUsluge) { 
        this.tipUsluge = tipUsluge;
        if (tipUsluge != null) {
            this.idTipUsluge = tipUsluge.getIdTipUsluge();
        }
    }
}
