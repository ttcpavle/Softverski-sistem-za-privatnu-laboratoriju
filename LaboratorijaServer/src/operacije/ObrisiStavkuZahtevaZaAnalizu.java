package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.StavkaZahteva;

/** Interna operacija - poziva se samo iz PromeniZahtevZaAnalizu, nije izlozena kroz Operacija/ClientHandler. */
public class ObrisiStavkuZahtevaZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof StavkaZahteva)) {
            return new Exception("Nije prosledjen objekat tipa StavkaZahteva");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }
        StavkaZahteva s = (StavkaZahteva) odo;
        boolean result = dbb.obrisiSlog(s);
        if (!result) {
            return new Response(null, new Exception("Greska pri brisanju stavke zahteva (rbStavka=" + s.getRbStavka() + ")"), false);
        }
        return new Response(null, null, true);
    }
}
