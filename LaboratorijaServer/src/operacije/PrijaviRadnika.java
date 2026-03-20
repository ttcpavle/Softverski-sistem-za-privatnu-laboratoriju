/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Radnik;

/**
 *
 * @author totic
 */
public class PrijaviRadnika extends OpstaSO{

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if(odo instanceof Radnik){
            Radnik r = (Radnik) odo;
            if(r.getKorisnickoIme() == null || r.getLozinka() == null){
                return new Exception("Nisu uneti svi podaci");
            }
            return null;
            
        }else{
            return new Exception("Nije prosledjen objekat radnik");
        }
        
        
        
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo,dbb);
        if(preduslov != null){
            return new Response(null, preduslov,false);
        }
        boolean res = dbb.nadjiSlog(odo);
        if(!res){
            return new Response(null, new Exception("Ne postoji radnik"), false);
        }
        Radnik r = (Radnik) dbb.getRezultat();
        return new Response(r,null,true);
        
        
    }
    
}
