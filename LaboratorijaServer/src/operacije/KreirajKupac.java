package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;

public class KreirajKupac extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Kupac)) {
            return new Exception("Nije prosledjen objekat tipa Kupac");
        }
        Kupac k = (Kupac) odo;
        if (k.getIme() == null || k.getIme().isEmpty()) {
            return new Exception("Kupac mora imati ime");
        }
        if (k.getPrezime() == null || k.getPrezime().isEmpty()) {
            return new Exception("Kupac mora imati prezime");
        }
        if (k.getDatumRodjenja() == null) {
            return new Exception("Kupac mora imati datum rodjenja");
        }
        if (k.getMesto() == null || k.getMesto().getIdMesto() <= 0) {
            return new Exception("Kupac mora imati validan grad/mesto");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }
        boolean result = dbb.pamtiSlog(odo);
        if (!result) {
            return new Response(null, new Exception("Greska pri kreiranju kupca"), false);
        }
        return new Response(null, null, true);
    }
}