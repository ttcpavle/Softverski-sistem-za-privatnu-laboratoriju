package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.StavkaZahteva;
import domen.ZahtevZaAnalizu;

public class KreirajZahtevZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof ZahtevZaAnalizu)) {
            return new Exception("Nije prosledjen objekat tipa ZahtevZaAnalizu");
        }
        ZahtevZaAnalizu z = (ZahtevZaAnalizu) odo;
        if (z.getKupac() == null) {
            return new Exception("Zahtev mora imati kupca");
        }
        if (z.getRadnik() == null) {
            return new Exception("Zahtev mora imati radnika");
        }
        if (z.getStavke() == null || z.getStavke().isEmpty()) {
            return new Exception("Zahtev mora imati bar jednu stavku");
        }
        if (z.getDatum() == null) {
            return new Exception("Zahtev mora imati datum");
        }
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        Exception preduslov = preduslovi(odo, dbb);
        if (preduslov != null) {
            return new Response(null, preduslov, false);
        }

        ZahtevZaAnalizu z = (ZahtevZaAnalizu) odo;

        // 1. kreiranje
        boolean result = dbb.pamtiSlog(z);
        if (!result) {
            return new Response(null, new Exception("Greska pri kreiranju zahteva za analizu"), false);
        }

        // 2.uzima generisan zahtev
        Integer generisaniId = (Integer) dbb.getRezultat();
        if (generisaniId == null || generisaniId <= 0) {
            return new Response(null, new Exception("Nije moguce preuzeti generisani ID zahteva"), false);
        }
        z.setIdZahtev(generisaniId);

        // 3. posebno cuvanje stavki zahteva
        for (StavkaZahteva stavka : z.getStavke()) {
            stavka.setZahtev(z); // generisan id
            boolean stavkaResult = dbb.pamtiSlog(stavka);
            if (!stavkaResult) {
                return new Response(null, new Exception("Greska pri cuvanju stavke zahteva (rbStavka=" + stavka.getRbStavka() + ")"), false);
            }
        }

        return new Response(null, null, true);
    }
}