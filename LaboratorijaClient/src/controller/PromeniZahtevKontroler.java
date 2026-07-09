/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import forms.PromeniZahtevForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import models.DomenskiComboBoxModel;
import models.StavkaTableModel;

/**
 *
 * @author totic
 */
public class PromeniZahtevKontroler extends OpstiKontrolerKI{

    private ZahtevZaAnalizu zahtev;
    private StavkaTableModel stavkaTableModel;
    
    public PromeniZahtevKontroler(OpstaEkranskaForma forma, ZahtevZaAnalizu zahtev) {
        super(forma);
        this.zahtev = zahtev;
        inicijalizujFormu();
        postaviListenere();
    }
    
    

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        PromeniZahtevForm f = (PromeniZahtevForm) forma;

        try {
            LocalDate datum = LocalDate.parse(f.getDatumField().getText(), DateTimeFormatter.ISO_DATE);
            zahtev.setDatum(datum);
        } catch (DateTimeParseException e) {
            f.prikaziErrorPane("Greska pri unosu datuma. Unesite u formatu yyyy-MM-dd", e);
            return null;
        }
        Radnik radnik = (Radnik) f.getRadnikComboBox().getSelectedItem();
        Kupac kupac = (Kupac) f.getKupacComboBox().getSelectedItem();
        boolean prioritet = f.getPrioritetCheck().isSelected();
        String status = (String) f.getStatusCombo().getSelectedItem();


        
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
        PromeniZahtevForm f = (PromeniZahtevForm) forma;
        stavkaTableModel = new StavkaTableModel(zahtev.getStavke());
        f.getIdField().setText(Integer.toString(zahtev.getIdZahtev()));
        f.getDatumField().setText(zahtev.getDatum().toString());
        f.getPrioritetCheck().setSelected(zahtev.isPrioritet());
        f.getUkupnaCenaZahteva().setText(Double.toString(zahtev.getUkupnaCenaZahteva()));
        f.getStatusCombo().setSelectedItem(zahtev.getStatus());
        f.getRadnikComboBox().setSelectedItem(zahtev.getRadnik());
        f.getKupacComboBox().setSelectedItem(zahtev.getKupac());
        f.getTabelaStavke().setModel(stavkaTableModel);
    }

    @Override
    protected void postaviListenere() {
        PromeniZahtevForm f = (PromeniZahtevForm) forma;
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
                stavka.setRbStavka(stavkaTableModel.getRowCount() + 1);

                stavkaTableModel.dodajStavku(stavka);

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


        f.getPotvrdiIzmeneButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stavkaTableModel.getRowCount() == 0) {
                    forma.prikaziErrorPane("Zahtev mora imati bar jednu stavku", null);
                    return;
                }

                ZahtevZaAnalizu zahtev = (ZahtevZaAnalizu) formToOdo();

                if(zahtev == null){
                    forma.prikaziErrorPane("Zahtev nije azuriran", null);
                    return;
                }
                if (zahtev.getRadnik() == null) {
                    forma.prikaziErrorPane("Izaberite radnika", null);
                    return;
                }
                if (zahtev.getKupac() == null) {
                    forma.prikaziErrorPane("Izaberite kupca", null);
                    return;
                }

                Response response = sendReceive(Operacija.PROMENI_ZAHTEV_ZA_ANALIZU, zahtev);
                if (response == null) return;

                if (response.isSuccess()) {
                    forma.prikaziInfoPane("Zahtev za analizu je uspesno azuriran!");
                } else {
                    forma.prikaziErrorPane("Greska pri azuriranju zahteva", response.getException());
                }
            }
        });

        // --- Ocisti formu ---
        f.getOcistiFormaButton().addActionListener(new ActionListener() {
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
        
        
        f.getProizvodComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azurirajUkupnuCenuStavke();
            }
        });

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
        PromeniZahtevForm f = (PromeniZahtevForm) forma;

        double ukupno = 0;
        for (StavkaZahteva s : stavkaTableModel.getStavke()) {
            ukupno += s.getUkupnaCena();
        }

        f.getUkupnaCenaZahteva().setText(String.valueOf(ukupno));
    }
    
    private void azurirajUkupnuCenuStavke() {
        PromeniZahtevForm f = (PromeniZahtevForm) forma;

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
    @Override
    protected void inicijalizujFormu() {
        PromeniZahtevForm f = (PromeniZahtevForm) forma;

        // Ucitaj radnike
        Response radniciResponse = sendReceive(Operacija.VRATI_LISTU_SVI_RADNIK, null);
        if (radniciResponse != null && radniciResponse.isSuccess()) {
            List<Radnik> radnici = (List<Radnik>) radniciResponse.getResult();
            f.getRadnikComboBox().setModel(new DomenskiComboBoxModel<>(radnici));
        } else {
            f.prikaziErrorPane("Greska pri ucitavanju radnika", null);
        }
        
        
        // Ucitaj kupce
        Response kupciResponse = sendReceive(Operacija.VRATI_LISTU_SVI_KUPAC, null);
        if (kupciResponse != null && kupciResponse.isSuccess()) {
            List<Kupac> kupci = (List<Kupac>) kupciResponse.getResult();
            f.getKupacComboBox().setModel(new DomenskiComboBoxModel<>(kupci));
        } else {
            f.prikaziErrorPane("Greska pri ucitavanju kupaca", null);
        }
        

        // Ucitaj proizvode
        Response proizvodiResponse = sendReceive(Operacija.VRATI_LISTU_SVI_PROIZVOD, null);
        if (proizvodiResponse != null && proizvodiResponse.isSuccess()) {
            List<Proizvod> proizvodi = (List<Proizvod>) proizvodiResponse.getResult();
            f.getProizvodComboBox().setModel(new DomenskiComboBoxModel<>(proizvodi));
        } else {
            f.prikaziErrorPane("Greska pri ucitavanju proizvoda", null);
        }
        
        odoToForm(zahtev);
        f.getIdField().setEditable(false);
        f.getDatumField().setEditable(true);
    }
    private void ocistiFormu() {
        PromeniZahtevForm f = (PromeniZahtevForm) forma;
        f.getKolicinaField().setText("");
        f.getUkupnaCenaStavkeField().setText("");
        f.getDatumField().setText(zahtev.getDatum().toString());
        stavkaTableModel.setStavke(new java.util.ArrayList<>());
        if (f.getRadnikComboBox().getItemCount() > 0) f.getRadnikComboBox().setSelectedIndex(0);
        if (f.getKupacComboBox().getItemCount() > 0) f.getKupacComboBox().setSelectedIndex(0);
        if (f.getProizvodComboBox().getItemCount() > 0) f.getProizvodComboBox().setSelectedIndex(0);
    }
}
