package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.StavkaZahteva;

/** Interna operacija - poziva se samo iz PromeniZahtevZaAnalizu, nije izlozena kroz Operacija/ClientHandler. */
public class ZapamtiStavkuZahtevaZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof StavkaZahteva)) {
            return new Exception("Nije prosledjen objekat tipa StavkaZahteva");
        }
        StavkaZahteva s = (StavkaZahteva) odo;
        if (s.getProizvod() == null) {
            return new Exception("Stavka mora imati proizvod");
        }
        if (s.getKolicina() <= 0) {
            return new Exception("Kolicina mora biti pozitivan ceo broj");
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
        boolean result = dbb.promeniSlog(s);
        if (!result) {
            return new Response(null, new Exception("Greska pri azuriranju stavke zahteva (rbStavka=" + s.getRbStavka() + ")"), false);
        }
        return new Response(null, null, true);
    }
}
