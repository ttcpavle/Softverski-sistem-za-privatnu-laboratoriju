package operacije;

import communication.Response;
import database.DBBroker;
import domen.Mesto;
import domen.OpstiDomenskiObjekat;

public class VratiListuSviMesto extends OpstaSO{
    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        boolean result = dbb.vratiSve(new Mesto());
        return new Response(dbb.getRezultat(), null, true);
    }    
}
