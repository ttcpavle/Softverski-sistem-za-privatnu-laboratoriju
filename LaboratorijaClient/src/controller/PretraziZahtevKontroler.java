/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import communication.Operacija;
import communication.Request;
import communication.Response;
import domen.Kupac;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.Radnik;
import domen.ZahtevZaAnalizu;
import forms.KreirajZahtevForm;
import forms.PretraziZahtevForm;
import forms.ZahtevDetaljiForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import models.DomenskiComboBoxModel;
import models.StavkaTableModel;
import models.ZahtevTableModel;

/**
 *
 * @author totic
 */
public class PretraziZahtevKontroler extends OpstiKontrolerKI {

    private ZahtevTableModel zahtevTableModel;

    public PretraziZahtevKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        PretraziZahtevForm f = (PretraziZahtevForm) forma;
        ZahtevZaAnalizu z = new ZahtevZaAnalizu();
        Kupac kupac = (Kupac) f.getKupacComboBox().getSelectedItem();
        Radnik radnik = (Radnik) f.getRadnikComboBox().getSelectedItem();
        String status = (String) f.getStatusCombo().getSelectedItem();
        z.setKupac(kupac);
        z.setRadnik(radnik);
        z.setStatus(status);
        return z;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {

    }

    @Override
    protected void postaviListenere() {
        PretraziZahtevForm f = (PretraziZahtevForm) forma;

        f.getPretraziButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ZahtevZaAnalizu zahtev = (ZahtevZaAnalizu) formToOdo();

                Response response = sendReceive(Operacija.VRATI_LISTU_ZAHTEV_ZA_ANALIZU, zahtev);
                if (response == null) {
                    return;
                }
                if (response.getException() != null) {
                    f.prikaziErrorPane("Greska: ", response.getException());
                    return;
                }
                List<ZahtevZaAnalizu> pronadjeniZahtevi = (List<ZahtevZaAnalizu>) response.getResult();

                zahtevTableModel = new ZahtevTableModel(pronadjeniZahtevi);
                f.getTabelaZahtevi().setModel(zahtevTableModel);
                if (pronadjeniZahtevi.isEmpty()) {
                    f.prikaziErrorPane("Sistem ne moze da nadje zahteve za analizu po zadatim kriterijumima", null);
                } else {
                    f.prikaziInfoPane("Sistem je nasao zahteve za analizu po zadatim kriterijumima");

                }                
            }

        });

        f.getGlavnaFormaButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forma.dispose();
            }
        });
        
        f.getDetaljiButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.JTable tabela = f.getTabelaZahtevi();
                int izabraniRed = tabela.getSelectedRow();

                if (izabraniRed == -1) {
                    f.prikaziErrorPane("Nije izabran red u tabeli", null);
                    return;
                }

                int konvertovaniRed = tabela.convertRowIndexToModel(izabraniRed);
                ZahtevZaAnalizu selektovaniZahtev = zahtevTableModel.getZahtevAt(konvertovaniRed); 
                ZahtevDetaljiForm detalji = new ZahtevDetaljiForm();
                ZahtevDetaljiKontroler kontroler = new ZahtevDetaljiKontroler(detalji, selektovaniZahtev);
                detalji.setLocationRelativeTo(null);
                detalji.setVisible(true);
            }
        });
    }

    @Override
    protected void inicijalizujFormu() {
        PretraziZahtevForm f = (PretraziZahtevForm) forma;

        // Postavi table model
        zahtevTableModel = new ZahtevTableModel();
        f.getTabelaZahtevi().setModel(zahtevTableModel);

        // Ucitaj radnike
        Response radniciResponse = sendReceive(Operacija.VRATI_LISTU_SVI_RADNIK, null);
        if (radniciResponse != null && radniciResponse.isSuccess()) {
            List<Radnik> radnici = (List<Radnik>) radniciResponse.getResult();
            Radnik prazanRadnik = new Radnik();
            prazanRadnik.setIme("---SVI RADNICI---");
            prazanRadnik.setPrezime("");
            radnici.add(0, prazanRadnik);
            f.getRadnikComboBox().setModel(new DomenskiComboBoxModel<>(radnici));
            
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju radnika", null);
        }
        

        // Ucitaj kupce
        Response kupciResponse = sendReceive(Operacija.VRATI_LISTU_SVI_KUPAC, null);
        if (kupciResponse != null && kupciResponse.isSuccess()) {
            List<Kupac> kupci = (List<Kupac>) kupciResponse.getResult();
            Kupac prazanKupac = new Kupac();
            prazanKupac.setIme("---SVI KUPCI---");
            prazanKupac.setPrezime("");
            prazanKupac.setMail("");
            kupci.add(0, prazanKupac);
            f.getKupacComboBox().setModel(new DomenskiComboBoxModel<>(kupci));
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju kupaca", null);
        }
    }

}
