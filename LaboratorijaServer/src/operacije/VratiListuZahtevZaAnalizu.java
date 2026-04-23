/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import domen.Radnik;
import domen.ZahtevZaAnalizu;

/**
 *
 * @author totic
 */
public class VratiListuZahtevZaAnalizu extends OpstaSO{

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if(odo instanceof ZahtevZaAnalizu || odo instanceof Radnik || odo instanceof Kupac || odo instanceof Proizvod){
            return null;
        }
        return new Exception("Nije prosledjen validan objekat kao kriterijum");
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception e = preduslovi(odo, dbb);
        if(e != null){
            return new Response(null, e, false);
        }
        
        if(odo instanceof ZahtevZaAnalizu){
            dbb.vratiSve(odo);
            return new Response(dbb.getRezultat(), null, true);
        }
        if(odo instanceof Radnik){
            dbb.vratiSvePremaUslovu(new ZahtevZaAnalizu(), "zahtevzaanalizu", "", "radnik", "zahtevzaanalizu.idZahtev=radnik.idRadnik", "", "");
            return new Response(dbb.getRezultat(), null, true);
        }
        if(odo instanceof Kupac){
            dbb.vratiSvePremaUslovu(new ZahtevZaAnalizu(), "zahtevzaanalizu", "", "kupac", "zahtevzaanalizu.idZahtev=kupac.idKupac", "", "");
            return new Response(dbb.getRezultat(), null, true);
        }
        if(odo instanceof Proizvod){
            // OVDE TREBA LOGIKA
            System.out.println("NIJE IMPLEMENTIRANO");
            return new Response(dbb.getRezultat(), null, true);
        }
        return new Response(null, null, false);        
    }
    
}
