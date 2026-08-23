package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;
import domen.StavkaZahteva;
import domen.ZahtevZaAnalizu;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (z.getKupac() == null) {
            return new Exception("Zahtev mora imati kupca");
        }
        if (z.getRadnik() == null) {
            return new Exception("Zahtev mora imati radnika");
        }
        if (z.getDatum() == null) {
            return new Exception("Zahtev mora imati datum");
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

        boolean result = dbb.promeniSlog(z);
        if (!result) {
            return new Response(null, new Exception("Greska pri izmeni headera zahteva za analizu"), false);
        }

        StavkaZahteva probeStavka = new StavkaZahteva(z);
        boolean nadjene = dbb.vratiSvePremaUslovu(probeStavka, "stavkazahteva", "", "proizvod",
                "stavkazahteva.idProizvod=proizvod.idProizvod",
                "idZahtev=" + z.getIdZahtev(), null);
        if (!nadjene) {
            return new Response(null, new Exception("Greska pri ucitavanju postojecih stavki zahteva"), false);
        }
        List<StavkaZahteva> stareStavke = (List<StavkaZahteva>) dbb.getRezultat();

        Map<Integer, StavkaZahteva> stareMap = new HashMap<>();
        for (StavkaZahteva stara : stareStavke) {
            stareMap.put(stara.getRbStavka(), stara);
        }

        Map<Integer, StavkaZahteva> noveMap = new HashMap<>();
        for (StavkaZahteva nova : z.getStavke()) {
            noveMap.put(nova.getRbStavka(), nova);
        }

        for (StavkaZahteva nova : z.getStavke()) {
            nova.setZahtev(z);
            if (stareMap.containsKey(nova.getRbStavka())) {
                Response r = new ZapamtiStavkuZahtevaZaAnalizu().izvrsenjeSO(nova, dbb);
                if (!r.isSuccess()) {
                    return r;
                }
            } else {
                Response r = new DodajStavkuZahtevaZaAnalizu().izvrsenjeSO(nova, dbb);
                if (!r.isSuccess()) {
                    return r;
                }
            }
        }

        for (StavkaZahteva stara : stareStavke) {
            if (!noveMap.containsKey(stara.getRbStavka())) {
                Response r = new ObrisiStavkuZahtevaZaAnalizu().izvrsenjeSO(stara, dbb);
                if (!r.isSuccess()) {
                    return r;
                }
            }
        }

        return new Response(null, null, true);
    }
}