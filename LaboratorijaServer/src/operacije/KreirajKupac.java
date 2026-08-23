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
        return null;
    }

    // pravi se prazan slog kupca
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
        Integer generisaniId = (Integer) dbb.getRezultat();
        if (generisaniId == null || generisaniId <= 0) {
            return new Response(null, new Exception("Nije moguce preuzeti generisani ID kupca"), false);
        }
        Kupac k = (Kupac) odo;
        k.setIdKupac(generisaniId);
        return new Response(k, null, true);
    }
}