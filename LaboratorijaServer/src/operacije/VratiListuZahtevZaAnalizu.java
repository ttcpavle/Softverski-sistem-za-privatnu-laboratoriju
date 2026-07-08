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
import domen.StavkaZahteva;
import domen.ZahtevZaAnalizu;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ClientHandler;

/**
 *
 * @author totic
 */
public class VratiListuZahtevZaAnalizu extends OpstaSO{

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    
    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof ZahtevZaAnalizu)) {
            return new Exception("Nije prosledjen objekat tipa ZahtevZaAnalizu");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception e = preduslovi(odo, dbb);
        if(e != null){
            return new Response(null, e, false);
        }
        
        boolean result = dbb.vratiSve(odo);
        if(!result){
            return new Response(null, new Exception("Greska pri ocitavanju zahteva"), false);
        }
        
        List<ZahtevZaAnalizu> lista = (List<ZahtevZaAnalizu>) dbb.getRezultat();
        for(ZahtevZaAnalizu zahtev:lista){
            result = dbb.nadjiSlog(zahtev.getKupac());
            if (!result) {
                return new Response(null, new Exception("Greska pri ocitavanju kupca"), false);
            }
            Kupac kupac = (Kupac) dbb.getRezultat();
            result = dbb.nadjiSlog(zahtev.getRadnik());
            if (!result) {
                return new Response(null, new Exception("Greska pri ocitavanju radnika"), false);
            }
            Radnik radnik = (Radnik) dbb.getRezultat();

            StavkaZahteva stavkaPretraga = new StavkaZahteva(zahtev);
            result = dbb.vratiSvePremaUslovu(stavkaPretraga, "stavkazahteva", "", "proizvod", 
                    "stavkazahteva.idProizvod=proizvod.idProizvod", 
                    "idZahtev=" + stavkaPretraga.getIdZahtev(), null);
            if (!result) {
                return new Response(null, new Exception("Greska pri ocitavanju stavki zahteva"), false);
            }
            List<StavkaZahteva> stavke = (List<StavkaZahteva>) dbb.getRezultat();

            zahtev.setStavke(stavke);
            zahtev.setKupac(kupac);
            zahtev.setRadnik(radnik);            
        }
//        for (ZahtevZaAnalizu z : lista) {
//            System.out.println("SERVER pre slanja: " + z.getKupac().getIme() + " " + z.getKupac().getPrezime());
//        }
        return new Response(lista, null, true);
    
    }
    
}
