/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package domen;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author totic
 * pored ovih metoda treba implementirati: 
 * 3 konstruktora (jedan sa id, jedan bez id, i prazan - zbog popuniIzResultSet() funkcije)
 * getteri/setteri
 * 
 */
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
