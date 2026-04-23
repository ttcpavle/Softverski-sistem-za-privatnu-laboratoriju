package controller;

import communication.Operacija;
import communication.Response;
import domen.Kupac;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import domen.Radnik;
import domen.StavkaZahteva;
import domen.ZahtevZaAnalizu;
import forms.KreirajZahtevForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import models.DomenskiComboBoxModel;
import models.StavkaTableModel;

public class KreirajZahtevKontroler extends OpstiKontrolerKI {

    private StavkaTableModel stavkaTableModel;

    public KreirajZahtevKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }

    /**
     * Cita izabranog radnika i kupca iz combo boxova,
     * konstruise ZahtevZaAnalizu sa listom stavki iz tabele.
     */
    @Override
    public OpstiDomenskiObjekat formToOdo() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;

        // Ocitaj radnika iz combo boxa
        Radnik radnik = (Radnik) f.getRadnikComboBox().getSelectedItem();

        // Ocitaj kupca iz combo boxa
        Kupac kupac = (Kupac) f.getKupacComboBox().getSelectedItem();

        // Ocitaj prioritet
        boolean prioritet = false;
        String prioritetTekst = f.getPrioritetField().getText().trim();
        if (!prioritetTekst.isEmpty()) {
            prioritet = prioritetTekst.equalsIgnoreCase("true")
                    || prioritetTekst.equals("1")
                    || prioritetTekst.equalsIgnoreCase("da");
        }

        // Ocitaj status
        String status = f.getStatusField().getText().trim();

        // Kreiraj zahtev
        ZahtevZaAnalizu zahtev = new ZahtevZaAnalizu();
        zahtev.setDatum(LocalDate.now());
        zahtev.setStatus(status);
        zahtev.setPrioritet(prioritet);
        zahtev.setRadnik(radnik);
        zahtev.setKupac(kupac);

        // Preuzmi listu stavki iz table modela i postavi je u zahtev
        List<StavkaZahteva> stavke = stavkaTableModel.getStavke();
        zahtev.setStavke(stavke);

        // Izracunaj ukupnu cenu zahteva kao sumu ukupnih cena svih stavki
        double ukupna = stavke.stream().mapToDouble(StavkaZahteva::getUkupnaCena).sum();
        zahtev.setUkupnaCenaZahteva(ukupna);

        return zahtev;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        // nije potrebna implementacija
    }

    @Override
    protected void postaviListenere() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;

        // --- Dodaj stavku ---
        f.getDodajStavkuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Proizvod proizvod = (Proizvod) f.getProizvodComboBox().getSelectedItem();
                if (proizvod == null) {
                    forma.prikaziErrorPane("Izaberite proizvod", null);
                    return;
                }

                String kolicinaTekst = f.getKolicinaField().getText().trim();
                if (kolicinaTekst.isEmpty()) {
                    forma.prikaziErrorPane("Unesite kolicinu", null);
                    return;
                }

                int kolicina;
                try {
                    kolicina = Integer.parseInt(kolicinaTekst);
                    if (kolicina <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    forma.prikaziErrorPane("Kolicina mora biti pozitivan ceo broj", null);
                    return;
                }

                double jedinicnaCena = proizvod.getCena();
                double ukupnaCena = kolicina * jedinicnaCena;

                StavkaZahteva stavka = new StavkaZahteva();
                stavka.setProizvod(proizvod);
                stavka.setKolicina(kolicina);
                stavka.setJedinicnaCena(jedinicnaCena);
                stavka.setUkupnaCena(ukupnaCena);
                // rbStavka = redni broj u listi (1-based)
                stavka.setRbStavka(stavkaTableModel.getRowCount() + 1);

                stavkaTableModel.dodajStavku(stavka);

                // Azuriraj ukupnu cenu zahteva u polju
                double ukupnaZahtev = stavkaTableModel.getStavke()
                        .stream().mapToDouble(StavkaZahteva::getUkupnaCena).sum();
                f.getUkupnaCenaField().setText(String.valueOf(ukupnaZahtev));

                // Azuriraj prikaz ukupne cene stavke i ocisti polja
                f.getUkupnaCenaStavkeField().setText(String.valueOf(ukupnaCena));
                f.getKolicinaField().setText("");
            }
        });

        // --- Ukloni stavku ---
        f.getUkloniStavkuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = f.getTabelaStavke().getSelectedRow();
                if (selectedRow < 0) {
                    forma.prikaziErrorPane("Izaberite stavku za uklanjanje", null);
                    return;
                }

                stavkaTableModel.ukloniStavku(selectedRow);

                // Ponovo numerisi rbStavka nakon uklanjanja
                List<StavkaZahteva> stavke = stavkaTableModel.getStavke();
                for (int i = 0; i < stavke.size(); i++) {
                    stavke.get(i).setRbStavka(i + 1);
                }

                // Azuriraj ukupnu cenu zahteva
                double ukupnaZahtev = stavke.stream().mapToDouble(StavkaZahteva::getUkupnaCena).sum();
                f.getUkupnaCenaField().setText(String.valueOf(ukupnaZahtev));
            }
        });

        // --- Kreiraj zahtev ---
        f.getKreirajZahtevButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stavkaTableModel.getRowCount() == 0) {
                    forma.prikaziErrorPane("Zahtev mora imati bar jednu stavku", null);
                    return;
                }

                ZahtevZaAnalizu zahtev = (ZahtevZaAnalizu) formToOdo();

                if (zahtev.getRadnik() == null) {
                    forma.prikaziErrorPane("Izaberite radnika", null);
                    return;
                }
                if (zahtev.getKupac() == null) {
                    forma.prikaziErrorPane("Izaberite kupca", null);
                    return;
                }

                Response response = sendReceive(Operacija.KREIRAJ_ZAHTEV_ZA_ANALIZU, zahtev);
                if (response == null) return;

                if (response.isSuccess()) {
                    forma.prikaziInfoPane("Zahtev za analizu je uspesno kreiran!");
                    ocistiFormu();
                } else {
                    forma.prikaziErrorPane("Greska pri kreiranju zahteva", response.getException());
                }
            }
        });

        // --- Ocisti formu ---
        f.getOcistiFormuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ocistiFormu();
            }
        });

        // --- Glavna forma ---
        f.getGlavnaFormaButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forma.dispose();
            }
        });
    }

    /**
     * Ucitava podatke za combo boxove sa servera pri otvaranju forme.
     * Tabelu ne diramo jer pri inicijalizaciji nema podataka.
     */
    @Override
    protected void inicijalizujFormu() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;

        // Postavi table model
        stavkaTableModel = new StavkaTableModel();
        f.getTabelaStavke().setModel(stavkaTableModel);

        // Postavi danasnji datum
        f.getDatumField().setText(LocalDate.now().toString());

        // Ucitaj radnike
        Response radniciResponse = sendReceive(Operacija.VRATI_LISTU_SVI_RADNIK, null);
        if (radniciResponse != null && radniciResponse.isSuccess()) {
            List<Radnik> radnici = (List<Radnik>) radniciResponse.getResult();
            f.getRadnikComboBox().setModel(new DomenskiComboBoxModel<>(radnici));
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju radnika", null);
        }

        // Ucitaj kupce
        Response kupciResponse = sendReceive(Operacija.VRATI_LISTU_SVI_KUPAC, null);
        if (kupciResponse != null && kupciResponse.isSuccess()) {
            List<Kupac> kupci = (List<Kupac>) kupciResponse.getResult();
            f.getKupacComboBox().setModel(new DomenskiComboBoxModel<>(kupci));
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju kupaca", null);
        }

        // Ucitaj proizvode
        Response proizvodiResponse = sendReceive(Operacija.VRATI_LISTU_SVI_PROIZVOD, null);
        if (proizvodiResponse != null && proizvodiResponse.isSuccess()) {
            List<Proizvod> proizvodi = (List<Proizvod>) proizvodiResponse.getResult();
            f.getProizvodComboBox().setModel(new DomenskiComboBoxModel<>(proizvodi));
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju proizvoda", null);
        }
    }

    private void ocistiFormu() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;
        f.getStatusField().setText("");
        f.getPrioritetField().setText("");
        f.getUkupnaCenaField().setText("");
        f.getKolicinaField().setText("");
        f.getUkupnaCenaStavkeField().setText("");
        f.getDatumField().setText(LocalDate.now().toString());
        stavkaTableModel.setStavke(new java.util.ArrayList<>());
        if (f.getRadnikComboBox().getItemCount() > 0) f.getRadnikComboBox().setSelectedIndex(0);
        if (f.getKupacComboBox().getItemCount() > 0) f.getKupacComboBox().setSelectedIndex(0);
        if (f.getProizvodComboBox().getItemCount() > 0) f.getProizvodComboBox().setSelectedIndex(0);
    }
}