package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.OpstiDomenskiObjekat;

public class PretraziKupac extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Kupac)) {
            return new Exception("Nije prosledjen objekat tipa Kupac");
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
            return new Response(null, new Exception("Kupac nije pronadjen"), true); // bilo je false
        }
        return new Response(dbb.getRezultat(), null, true);
    }
}
