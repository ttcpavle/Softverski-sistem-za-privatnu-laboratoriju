package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;

public class PromeniProizvod extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Proizvod)) {
            return new Exception("Nije prosledjen objekat tipa Proizvod");
        }
        Proizvod p = (Proizvod) odo;
        if (p.getIdProizvod() <= 0) {
            return new Exception("Proizvod nema validan ID za izmenu");
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
            return new Response(null, new Exception("Greska pri izmeni proizvoda"), false);
        }
        return new Response(null, null, true);
    }
}
