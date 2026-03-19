/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Radnik;
import java.util.List;

/**
 *
 * @author totic
 */
public class VratiListuSviRadnik extends OpstaSO{

    @Override
    protected Response preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return new Response(null, null, true); // nema preduslova za ovu operaciju
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        boolean result = dbb.vratiSve(new Radnik());
        if(!result){
            return new Response(null, new Exception("Doslo je do greske"), false);
        }
        List<Radnik> radnici = (List<Radnik>) dbb.getRezultat();
        return new Response(radnici, null, true);
    }
    
}
