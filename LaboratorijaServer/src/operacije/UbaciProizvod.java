package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;

public class UbaciProizvod extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Proizvod)) {
            return new Exception("Nije prosledjen objekat tipa Proizvod");
        }
        Proizvod p = (Proizvod) odo;
        if (p.getNaziv() == null || p.getNaziv().isEmpty()) {
            return new Exception("Proizvod mora imati naziv");
        }
        if (p.getVremeIzdavanjaRez() == null) {
            return new Exception("Proizvod mora imati vreme izdavanja rezultata");
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
            return new Response(null, new Exception("Greska pri unosu proizvoda"), false);
        }
        return new Response(null, null, true);
    }
}