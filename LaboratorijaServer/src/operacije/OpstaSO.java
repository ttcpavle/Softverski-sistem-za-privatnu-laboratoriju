package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;

public abstract class OpstaSO {
    protected String porukaUspesnostiSO; // status trenutno izvrsavane operacije
    
    
    // baza garantuje: ako dva threada menjaju isti red, jedan thread ce cekati
    // programer mora da garantuje: ako dva threada rade istu biznis operaciju, mora je sinhronizovati na aplikacionom nivou
    public synchronized Response opsteIzvrsenjeSO(OpstiDomenskiObjekat odo) {
        
        DBBroker dbb = new DBBroker();
        if(!dbb.isUspesnaKonekcija()){
            return new Response(null, new Exception("Neuspesna konekcija sa bazom"), false);
        }
        Response result = izvrsenjeSO(odo, dbb);

        if (result.isSuccess()) {
            dbb.commitTransakcije();
        } else {
            dbb.rollbackTransakcije();
        }
        
        dbb.zatvoriBazu();
        return result;

    }

    protected abstract Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb);
    protected abstract Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb);

}
