package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;

public class PromeniKupac extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Kupac)) {
            return new Exception("Nije prosledjen objekat tipa Kupac");
        }
        Kupac k = (Kupac) odo;
        if (k.getIdKupac() <= 0) {
            return new Exception("Kupac nema validan ID za izmenu");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }
        boolean result = dbb.promeniSlog(odo);
        if (!result) {
            return new Response(null, new Exception("Greska pri izmeni kupca"), false);
        }
        return new Response(null, null, true);
    }
}
