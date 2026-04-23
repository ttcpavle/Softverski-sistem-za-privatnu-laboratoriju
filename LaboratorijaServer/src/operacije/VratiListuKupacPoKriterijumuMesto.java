package operacije;

import communication.Response;
import database.DBBroker;
import domen.Kupac;
import domen.Mesto;
import domen.OpstiDomenskiObjekat;

public class VratiListuKupacPoKriterijumuMesto extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof Mesto)) {
            return new Exception("Nije prosledjen objekat tipa Mesto");
        }
        Mesto m = (Mesto) odo;
        if (m.getIdMesto() <= 0) {
            return new Exception("Mesto nema validan ID");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }
        Mesto m = (Mesto) odo;
        dbb.vratiSvePremaUslovu(
                new Kupac(),
                "kupac",
                "INNER",
                "mesto",
                "kupac.idMesto = mesto.idMesto",
                "mesto.idMesto = " + m.getIdMesto(),
                ""
        );
        return new Response(dbb.getRezultat(), null, true);
    }
}
