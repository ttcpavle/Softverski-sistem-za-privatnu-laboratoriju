package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Radnik;

public class VratiListuSviRadnik extends OpstaSO{

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        boolean result = dbb.vratiSve(new Radnik());
        return new Response(dbb.getRezultat(), null, true);
    }
    
}
