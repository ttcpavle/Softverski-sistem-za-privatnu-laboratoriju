package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.ZahtevZaAnalizu;

public class KreirajZahtevZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof ZahtevZaAnalizu)) {
            return new Exception("Nije prosledjen objekat tipa ZahtevZaAnalizu");
        }
        return null;
    }


    // kreiranje praznog zahteva
    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }

        ZahtevZaAnalizu z = (ZahtevZaAnalizu) odo;

        boolean result = dbb.pamtiSlog(z);
        if (!result) {
            return new Response(null, new Exception("Greska pri kreiranju zahteva za analizu"), false);
        }

        Integer generisaniId = (Integer) dbb.getRezultat();
        if (generisaniId == null || generisaniId <= 0) {
            return new Response(null, new Exception("Nije moguce preuzeti generisani ID zahteva"), false);
        }
        z.setIdZahtev(generisaniId);

        return new Response(z, null, true);
    }
}