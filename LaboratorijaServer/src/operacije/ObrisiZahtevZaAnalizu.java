package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.ZahtevZaAnalizu;

public class ObrisiZahtevZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof ZahtevZaAnalizu)) {
            return new Exception("Prosledjeni objekat nije zahtev za analizu");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception e = preduslovi(odo, dbb);
        if (e != null) {
            return new Response(null, e, false);
        }
        boolean result = dbb.obrisiSlog(odo);
        if (!result) {
            return new Response(null, new Exception("Greska pri brisanju zahteva za analizu"), false);
        }
        return new Response(null, null, true);
    }
}
