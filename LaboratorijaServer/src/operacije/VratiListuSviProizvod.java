package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;

public class VratiListuSviProizvod extends OpstaSO{
    
    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        boolean result = dbb.vratiSve(new Proizvod());
        return new Response(dbb.getRezultat(), null, true);
    }    
}
