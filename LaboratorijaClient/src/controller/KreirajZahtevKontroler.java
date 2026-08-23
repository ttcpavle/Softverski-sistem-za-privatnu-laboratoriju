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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import models.DomenskiComboBoxModel;
import models.StavkaTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

public class KreirajZahtevKontroler extends OpstiKontrolerKI {

    private StavkaTableModel stavkaTableModel;

    // id novog zahteva
    private int idNoviZahtev;
    // da li je trenutno kreirani zahtev vec sacuvan
    private boolean sacuvano;

    public KreirajZahtevKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;
        Radnik radnik = (Radnik) f.getRadnikComboBox().getSelectedItem();
        Kupac kupac = (Kupac) f.getKupacComboBox().getSelectedItem();
        boolean prioritet = false;
        prioritet = f.getPrioritetCheckBox().isSelected();
        String status = (String) f.getStatusCombo().getSelectedItem();

        ZahtevZaAnalizu zahtev = new ZahtevZaAnalizu();
        zahtev.setIdZahtev(idNoviZahtev);
        zahtev.setDatum(LocalDate.now());
        zahtev.setStatus(status);
        zahtev.setPrioritet(prioritet);
        zahtev.setRadnik(radnik);
        zahtev.setKupac(kupac);

        List<StavkaZahteva> stavke = stavkaTableModel.getStavke();
        zahtev.setStavke(stavke);

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

            }
        });

        // --- Sacuvaj (zapamti) zahtev ---
        f.getKreirajZahtevButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stavkaTableModel.getRowCount() == 0) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti zahtev za analizu: Zahtev mora imati bar jednu stavku", null);
                    return;
                }

                ZahtevZaAnalizu zahtev = (ZahtevZaAnalizu) formToOdo();

                if (zahtev.getRadnik() == null) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti zahtev za analizu: Izaberite radnika", null);
                    return;
                }
                if (zahtev.getKupac() == null) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti zahtev za analizu: Izaberite kupca", null);
                    return;
                }

                Response response = sendReceive(Operacija.PROMENI_ZAHTEV_ZA_ANALIZU, zahtev);
                if (response == null) return;

                if (response.isSuccess()) {
                    sacuvano = true;
                    forma.prikaziInfoPane("Sistem je zapamtio zahtev za analizu.");
                    zatvoriFormu();
                } else {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti zahtev za analizu: " + response.getException().getMessage(), null);
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
                zatvoriFormu();
            }
        });

        forma.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                zatvoriFormu();
            }
        });

        f.getProizvodComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azurirajUkupnuCenuStavke();
            }
        });

        // reaguje na svaki ukucan karakter u polju za kolicinu
        f.getKolicinaField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { azurirajUkupnuCenuStavke(); }
            @Override
            public void removeUpdate(DocumentEvent e) { azurirajUkupnuCenuStavke(); }
            @Override
            public void changedUpdate(DocumentEvent e) { azurirajUkupnuCenuStavke(); }

        });

        stavkaTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                azurirajUkupnuCenuZahteva();
            }
        });
    }

    private void azurirajUkupnuCenuZahteva() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;

        double ukupno = 0;
        for (StavkaZahteva s : stavkaTableModel.getStavke()) {
            ukupno += s.getUkupnaCena();
        }

        f.getUkupnaCenaZahteva().setText(String.valueOf(ukupno));
    }

    private void azurirajUkupnuCenuStavke() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;

        Proizvod izabraniProizvod = (Proizvod) f.getProizvodComboBox().getSelectedItem();
        String kolicinaTekst = f.getKolicinaField().getText();

        if (izabraniProizvod == null || kolicinaTekst == null || kolicinaTekst.isEmpty()) {
            f.getUkupnaCenaStavkeField().setText("");
            return;
        }

        try {
            int kolicina = Integer.parseInt(kolicinaTekst);
            double ukupno = kolicina * izabraniProizvod.getCena();
            f.getUkupnaCenaStavkeField().setText(String.valueOf(ukupno));
        } catch (NumberFormatException ex) {
            f.getUkupnaCenaStavkeField().setText("");
        }
    }

    // ucitavaju se liste za combo boxove a onda se odmah poziva sistem da kreira nov zahtev
    @Override
    protected void inicijalizujFormu() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;

        // Postavi table model
        stavkaTableModel = new StavkaTableModel();
        stavkaTableModel.setNaGresku(poruka -> forma.prikaziErrorPane(poruka, null));
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

        javax.swing.SwingUtilities.invokeLater(this::kreirajPrazanZahtev);
    }


    private void kreirajPrazanZahtev() {
        Response response = sendReceive(Operacija.KREIRAJ_ZAHTEV_ZA_ANALIZU, new ZahtevZaAnalizu());
        if (response != null && response.isSuccess()) {
            ZahtevZaAnalizu kreiraniZahtev = (ZahtevZaAnalizu) response.getResult();
            idNoviZahtev = kreiraniZahtev.getIdZahtev();
            sacuvano = false;
            forma.prikaziInfoPane("Sistem je kreirao zahtev za analizu.");
        } else {
            String razlog = response != null ? response.getException().getMessage() : "greska u komunikaciji";
            forma.prikaziErrorPane("Sistem ne moze da kreira zahtev za analizu: " + razlog, null);
            forma.dispose();
        }
    }

    // ako novi zahtev nikada nije sacuvan, brise se slog pre zatvaranja forme
    private void zatvoriFormu() {
        if (!sacuvano && idNoviZahtev > 0) {
            ZahtevZaAnalizu z = new ZahtevZaAnalizu();
            z.setIdZahtev(idNoviZahtev);
            sendReceive(Operacija.OBRISI_ZAHTEV_ZA_ANALIZU, z);
        }
        forma.dispose();
    }

    private void ocistiFormu() {
        KreirajZahtevForm f = (KreirajZahtevForm) forma;
        f.getKolicinaField().setText("");
        f.getUkupnaCenaStavkeField().setText("");
        f.getDatumField().setText(LocalDate.now().toString());
        stavkaTableModel.setStavke(new java.util.ArrayList<>());
        if (f.getRadnikComboBox().getItemCount() > 0) f.getRadnikComboBox().setSelectedIndex(0);
        if (f.getKupacComboBox().getItemCount() > 0) f.getKupacComboBox().setSelectedIndex(0);
        if (f.getProizvodComboBox().getItemCount() > 0) f.getProizvodComboBox().setSelectedIndex(0);
    }
}
