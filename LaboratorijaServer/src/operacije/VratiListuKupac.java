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
public class VratiListuKupac extends OpstaSO{

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if(odo == null || !(odo instanceof Kupac))
            return new Exception("Prosledjeni objekat nije kupac");
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception e = preduslovi(odo, dbb);
        if(e != null){
            return new Response(null, e, false);
        }
        
        Kupac k = (Kupac) odo;
        boolean result = dbb.vratiSvePremaUslovu(new Kupac(), "kupac", "", "mesto", 
                "kupac.idMesto=mesto.idMesto", odo.vratiUslovZaNadjiSlog(), 
                "kupac.*, mesto.naziv AS mesto_naziv, zipKod");
        
        if(!result){
            return new Response(null, new Exception("Greska pri pretrazi kupca"), false);
        }
        return new Response(dbb.getRezultat(), null, true);
    }
    
}
