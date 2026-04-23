package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.StavkaZahteva;
import domen.ZahtevZaAnalizu;

public class PromeniZahtevZaAnalizu extends OpstaSO {

    @Override
    protected Exception preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        if (!(odo instanceof ZahtevZaAnalizu)) {
            return new Exception("Nije prosledjen objekat tipa ZahtevZaAnalizu");
        }
        ZahtevZaAnalizu z = (ZahtevZaAnalizu) odo;
        if (z.getIdZahtev() <= 0) {
            return new Exception("Zahtev nema validan ID za izmenu");
        }
        if (z.getStavke() == null || z.getStavke().isEmpty()) {
            return new Exception("Zahtev mora imati bar jednu stavku");
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

        // 1. Promeni header zahteva (datum, status, prioritet, ukupnaCena, radnik, kupac)
        boolean result = dbb.promeniSlog(z);
        if (!result) {
            return new Response(null, new Exception("Greska pri izmeni headera zahteva za analizu"), false);
        }

        // 2. Obrisi sve postojece stavke za ovaj zahtev (delete-then-insert strategija)
        //    Kreiramo "probe" stavku samo sa idZahtev kako bi vratiUslovZaNadjiSlog()
        //    generisalo: WHERE idZahtev=X (bez rbStavka i proizvod uslova)
        StavkaZahteva probeStavka = new StavkaZahteva();
        probeStavka.setZahtev(z); // postavi se idZahtev unutar stavke i on se koristi kao filter
        boolean obrisan = dbb.obrisiSvePremaUslovu(probeStavka);
        if (!obrisan) {
            return new Response(null, new Exception("Greska pri brisanju stavki zahteva"), false);
        }

        // 3. Ubaci novi set stavki
        for (StavkaZahteva stavka : z.getStavke()) {
            stavka.setZahtev(z); // osigurava idZahtev na svakoj stavki
            boolean stavkaResult = dbb.pamtiSlog(stavka);
            if (!stavkaResult) {
                return new Response(null, new Exception("Greska pri unosu stavke zahteva (rbStavka=" + stavka.getRbStavka() + ")"), false);
            }
        }

        return new Response(null, null, true);
    }
}