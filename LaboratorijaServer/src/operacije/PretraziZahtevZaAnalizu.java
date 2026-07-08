package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;
import domen.Radnik;
import domen.StavkaZahteva;
import domen.ZahtevZaAnalizu;
import java.util.List;

public class PretraziZahtevZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof ZahtevZaAnalizu)) {
            return new Exception("Nije prosledjen objekat tipa ZahtevZaAnalizu");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }

        boolean result = dbb.nadjiSlog(odo);
        if (!result) {
            return new Response(null, new Exception("Zahtev za analizu nije pronadjen"), false);
        }
        ZahtevZaAnalizu zahtev = (ZahtevZaAnalizu) dbb.getRezultat();
        // ovaj zahtev nema kupca, radnika ni stavke pa cemo da ih odmah pretrazimo
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
        //result = dbb.vratiSve(stavkaPretraga);
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
        
        

        return new Response(zahtev, null, true);
    }
}
