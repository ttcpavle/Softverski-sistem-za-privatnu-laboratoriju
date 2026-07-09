/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// PretraziKupacKontroler.java
package controller;

import communication.Operacija;
import communication.Response;
import domen.Kupac;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import forms.KupacDetaljiForm;
import forms.PretraziKupacForm;
import forms.PromeniKupacForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import models.KupacTableModel;

/**
 *
 * @author totic
 */
public class PretraziKupacKontroler extends OpstiKontrolerKI {

    private KupacTableModel kupacTableModel;
    private final ModForme mod;

    public PretraziKupacKontroler(OpstaEkranskaForma forma, ModForme mod) {
        super(forma);
        this.mod = mod;
        inicijalizujFormu();
        postaviListenere();
    }

    /**
     * Konstruise Kupac objekat sa kriterijumima pretrage iz forme.
     */
    @Override
    public OpstiDomenskiObjekat formToOdo() {
        PretraziKupacForm f = (PretraziKupacForm) forma;
        Kupac kupac = new Kupac();
        kupac.setIme(f.getImeField().getText().trim());
        kupac.setPrezime(f.getPrezimeField().getText().trim());
        kupac.setMail(f.getEmailField().getText().trim());
        kupac.setTelefon(f.getTelefonField().getText().trim());
        return kupac;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        // nije potrebna implementacija
    }

    @Override
    protected void postaviListenere() {
        PretraziKupacForm f = (PretraziKupacForm) forma;

        // --- Pretrazi ---
        f.getPretraziButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kupac kriterijum = (Kupac) formToOdo();

                Response response = sendReceive(Operacija.VRATI_LISTU_KUPAC, kriterijum);
                if (response == null) return;

                if (response.getException() != null) {
                    forma.prikaziErrorPane("Greska: ", response.getException());
                    return;
                }

                List<Kupac> pronadjeniKupci = (List<Kupac>) response.getResult();
                kupacTableModel = new KupacTableModel(pronadjeniKupci);
                f.getKupacTable().setModel(kupacTableModel);

                if (pronadjeniKupci.isEmpty()) {
                    forma.prikaziErrorPane("Sistem ne moze da nadje kupce po zadatim kriterijumima", null);
                } else {
                    forma.prikaziInfoPane("Sistem je nasao kupce po zadatim kriterijumima");
                }
            }
        });

        // --- Detalji ---
        f.getDetaljiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kupac izabraniKupac = uzmiIzabranogKupca(f);
                if (izabraniKupac == null) return;

                KupacDetaljiForm detalji = new KupacDetaljiForm();
                KupacDetaljiKontroler kontroler = new KupacDetaljiKontroler(detalji, izabraniKupac);
                detalji.setLocationRelativeTo(null);
                detalji.setVisible(true);
            }
        });

        // --- Promeni (samo u modu PROMENA) ---
        f.getPromeniButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kupac izabraniKupac = uzmiIzabranogKupca(f);
                if (izabraniKupac == null) return;

                // moramo popuniti ceo kupac (ako lista iz pretrage ne sadrzi sva polja)
                Response r = sendReceive(Operacija.PRETRAZI_KUPCA, izabraniKupac);
                if (r == null || !r.isSuccess()) {
                    forma.prikaziErrorPane("Greska pri ucitavanju kupca", r != null ? r.getException() : null);
                    return;
                }
                Kupac kompletanKupac = (Kupac) r.getResult();

                PromeniKupacForm promeniKupacForm = new PromeniKupacForm();
                PromeniKupacKontroler kontroler = new PromeniKupacKontroler(promeniKupacForm, kompletanKupac);
                promeniKupacForm.setLocationRelativeTo(null);
                promeniKupacForm.setVisible(true);
            }
        });

        // --- Obrisi (samo u modu BRISANJE) ---
        f.getObrisiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable tabela = f.getKupacTable();
                int izabraniRed = tabela.getSelectedRow();

                if (izabraniRed == -1) {
                    forma.prikaziErrorPane("Nije izabran red u tabeli", null);
                    return;
                }

                int konvertovaniRed = tabela.convertRowIndexToModel(izabraniRed);
                Kupac izabraniKupac = kupacTableModel.getKupacAt(konvertovaniRed);

                int potvrda = JOptionPane.showConfirmDialog(f,
                        "Da li ste sigurni da zelite da obrisete kupca "
                                + izabraniKupac.getIme() + " " + izabraniKupac.getPrezime() + "?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (potvrda != JOptionPane.YES_OPTION) return;

                Response response = sendReceive(Operacija.OBRISI_KUPCA, izabraniKupac);
                if (response == null) return;

                if (response.isSuccess()) {
                    forma.prikaziInfoPane("Kupac je uspesno obrisan!");
                    kupacTableModel.ukloniKupca(konvertovaniRed);
                } else {
                    forma.prikaziErrorPane("Greska pri brisanju kupca", response.getException());
                }
            }
        });

        // --- Glavna forma ---
        f.getGlavnFormaButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forma.dispose();
            }
        });
    }

    private Kupac uzmiIzabranogKupca(PretraziKupacForm f) {
        JTable tabela = f.getKupacTable();
        int izabraniRed = tabela.getSelectedRow();

        if (izabraniRed == -1) {
            forma.prikaziErrorPane("Nije izabran red u tabeli", null);
            return null;
        }

        int konvertovaniRed = tabela.convertRowIndexToModel(izabraniRed);
        return kupacTableModel.getKupacAt(konvertovaniRed);
    }

    @Override
    protected void inicijalizujFormu() {
        PretraziKupacForm f = (PretraziKupacForm) forma;

        // Postavi table model
        kupacTableModel = new KupacTableModel();
        f.getKupacTable().setModel(kupacTableModel);

        // Prikazi dugmad u zavisnosti od moda
        f.getObrisiButton().setVisible(mod == ModForme.BRISANJE);
        f.getPromeniButton().setVisible(mod == ModForme.PROMENA);
    }
}
