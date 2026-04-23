package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;

public class PretraziProizvod extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Proizvod)) {
            return new Exception("Nije prosledjen objekat tipa Proizvod");
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
            return new Response(null, new Exception("Proizvod nije pronadjen"), false);
        }
        return new Response(dbb.getRezultat(), null, true);
    }
}
