/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;

/**
 *
 * @author totic
 */
public class ObrisiKupac extends OpstaSO{

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if(!(odo instanceof Kupac))
            return new Exception("Prosledjeni objekat nije kupac");
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception e = preduslovi(odo, dbb);
        if(e != null){
            return new Response(null, e, false);
        }
        boolean result = dbb.obrisiSlog(odo);
        if(!result){
            return new Response(null, new Exception("Greska pri brisanju kupca"), false);
        }
        return new Response(null, null, true);
    }
    
}
