package controller;

import communication.Operacija;
import communication.Response;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import forms.UbaciProizvodForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UbaciProizvodKontroler extends OpstiKontrolerKI {

    public UbaciProizvodKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        UbaciProizvodForm f = (UbaciProizvodForm) forma;

        Proizvod proizvod = new Proizvod();
        proizvod.setNaziv(f.getNazivField().getText().trim());
        proizvod.setOpis(f.getOpisArea().getText().trim());
        try {
            proizvod.setCena(Double.parseDouble(f.getCenaField().getText().trim()));
        } catch (NumberFormatException ex) {
            proizvod.setCena(-1);
        }

        try {
            proizvod.setVremeCekanjaSati(Integer.parseInt(f.getVremeIzdavanjaField().getText().trim()));
        } catch (NumberFormatException ex) {
            proizvod.setVremeCekanjaSati(-1);
        }

        return proizvod;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        // nije potrebna implementacija
    }

    @Override
    protected void postaviListenere() {
        UbaciProizvodForm f = (UbaciProizvodForm) forma;

        f.getUbaciProizvodButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Proizvod proizvod = (Proizvod) formToOdo();

                if (proizvod.getNaziv() == null || proizvod.getNaziv().isEmpty()) {
                    forma.prikaziErrorPane("Unesite naziv proizvoda", null);
                    return;
                }
                if (proizvod.getCena() <= 0) {
                    forma.prikaziErrorPane("Cena mora biti pozitivan broj", null);
                    return;
                }
                if (proizvod.getVremeCekanjaSati() <= 0) {
                    forma.prikaziErrorPane("Vreme cekanja mora biti pozitivan ceo broj sati", null);
                    return;
                }

                Response response = sendReceive(Operacija.UBACI_PROIZVOD, proizvod);
                if (response == null) return;

                if (response.isSuccess()) {
                    forma.prikaziInfoPane("Proizvod je uspesno ubacen!");
                    ocistiFormu();
                } else {
                    forma.prikaziErrorPane("Greska pri ubacivanju proizvoda", response.getException());
                }
            }
        });

        f.getGlavnaFormaButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forma.dispose();
            }
        });
    }

    @Override
    protected void inicijalizujFormu() {
        // nema podataka koje treba ucitati pri otvaranju forme
    }

    private void ocistiFormu() {
        UbaciProizvodForm f = (UbaciProizvodForm) forma;
        f.getNazivField().setText("");
        f.getCenaField().setText("");
        f.getVremeIzdavanjaField().setText("");
        f.getOpisArea().setText("");
    }
}