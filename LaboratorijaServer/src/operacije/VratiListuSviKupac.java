/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;
import java.util.List;

/**
 *
 * @author totic
 */
public class VratiListuSviKupac extends OpstaSO{

    @Override
    protected Response preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return new Response(null, null, true);
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        boolean result = dbb.vratiSve(new Kupac());
        if(!result){
            return new Response(null, new Exception("Doslo je do greske"), false);
        }
        List<Kupac> kupci = (List<Kupac>) dbb.getRezultat();
        return new Response(kupci, null, true);
    }
    
}
