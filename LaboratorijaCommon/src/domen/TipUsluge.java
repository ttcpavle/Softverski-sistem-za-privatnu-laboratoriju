// TipUsluge.java
package domen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipUsluge implements OpstiDomenskiObjekat, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idTipUsluge;
    private String nazivUsluge;
    private String opisUsluge;
    
    private List<Ovlascenje> ovlascenja;
    
    public TipUsluge() {
        this.ovlascenja = new ArrayList<>();
    }
    
    @Override
    public String vratiImeTabele() {
        return "tipusluge";
    }
    
    @Override
    public String vratiNaziveKolona() {
        return "idTipUsluge, nazivUsluge, opisUsluge";
    }
    
    @Override
    public String vratiVrednostiAtributa() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(idTipUsluge > 0 ? idTipUsluge : "NULL").append(", ");
        sb.append("'").append(nazivUsluge).append("', ");
        sb.append("'").append(opisUsluge).append("'");
        
        return sb.toString();
    }
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "nazivUsluge='" + nazivUsluge + "', opisUsluge='" + opisUsluge + "'";
    }
    
    @Override
    public String vratiNazivKolonePK() {
        return "idTipUsluge";
    }
    
    @Override
    public String vratiVrednostPK() {
        return String.valueOf(idTipUsluge);
    }
    
    @Override
    public String vratiUslovZaNadjiSlog() {
        List<String> conditions = new ArrayList<>();
        
        if (idTipUsluge > 0) {
            conditions.add("idTipUsluge=" + idTipUsluge);
        }
        if (nazivUsluge != null && !nazivUsluge.isEmpty()) {
            conditions.add("nazivUsluge LIKE '%" + nazivUsluge + "%'");
        }
        
        return conditions.isEmpty() ? "1=1" : String.join(" AND ", conditions);
    }
    
    @Override
    public void popuniIzResultSet(ResultSet rs) throws SQLException {
        this.idTipUsluge = rs.getInt("idTipUsluge");
        this.nazivUsluge = rs.getString("nazivUsluge");
        this.opisUsluge = rs.getString("opisUsluge");
    }
    
    // Getters and Setters
    public int getIdTipUsluge() { return idTipUsluge; }
    public void setIdTipUsluge(int idTipUsluge) { this.idTipUsluge = idTipUsluge; }
    
    public String getNazivUsluge() { return nazivUsluge; }
    public void setNazivUsluge(String nazivUsluge) { this.nazivUsluge = nazivUsluge; }
    
    public String getOpisUsluge() { return opisUsluge; }
    public void setOpisUsluge(String opisUsluge) { this.opisUsluge = opisUsluge; }
    
    public List<Ovlascenje> getOvlascenja() { return ovlascenja; }
    public void setOvlascenja(List<Ovlascenje> ovlascenja) { this.ovlascenja = ovlascenja; }
}
