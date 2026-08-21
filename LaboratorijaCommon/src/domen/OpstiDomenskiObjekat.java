package domen;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OpstiDomenskiObjekat {
    
    // format vracenog stringa: 'neki string', 123, 10, 'abc' (koristi se npr kod INSERT INTO x VALUES (<povratna vrednost ove funkcije>)
    public String vratiVrednostiAtributa();
    public String vratiImeTabele();
    // format za uslov za nadjislog: username='abc' AND password=123 (tj x1=y1 AND x2=y2 AND ... AND xn=yn)
    public String vratiUslovZaNadjiSlog();
    public String vratiNaziveKolona();
    // format za uslov update: username='abc', password=123 (tj x1=y1, x2=y2 AND ... AND xn=yn)
    public String vratiVrednostiZaUpdate();
    public String vratiNazivKolonePK();
    public String vratiVrednostPK();
    // na osnovu resultset inplace se azuriraju vrednosti objekta
    public void popuniIzResultSet(ResultSet rs) throws SQLException;
    
    
}
