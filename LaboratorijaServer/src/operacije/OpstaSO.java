/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import java.util.logging.Logger;

/**
 *
 * @author totic
 */
public abstract class OpstaSO {
    protected String porukaUspesnostiSO; // status trenutno izvrsavane operacije
    
    
    // Baza garantuje: ako dva threada menjaju ISTI red, jedan će čekati
    // Programer mora garantovati: ako dva threada rade ISTU biznis operaciju, mora sinhronizovati na aplikacionom nivou
    // TL;DR: Baza sinhronizuje pristup podacima. Programer mora sinhronizovati poslovnu logiku
    public synchronized Response opsteIzvrsenjeSO(OpstiDomenskiObjekat odo) {
        
        DBBroker dbb = new DBBroker();
        if(!dbb.isUspesnaKonekcija()){
            return new Response(null, new Exception("Neuspesna konekcija sa bazom"), false);
        }
        Response result = izvrsenjeSO(odo, dbb);

        if (result.isSuccess()) {
            dbb.commitTransakcije();
        } else {
            dbb.rollbackTransakcije();
        }
        
        dbb.zatvoriBazu();
        return result;

    }

    protected abstract Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb);
    protected abstract Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb);

}
