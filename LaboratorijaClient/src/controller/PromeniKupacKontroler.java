/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import communication.Operacija;
import communication.Response;
import domen.Kupac;
import domen.Mesto;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import forms.PromeniKupacForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import models.DomenskiComboBoxModel;

/**
 *
 * @author totic
 */
public class PromeniKupacKontroler extends OpstiKontrolerKI{

    private Kupac kupac;
    
    public PromeniKupacKontroler(OpstaEkranskaForma forma, Kupac kupac) {
        super(forma);
        this.kupac = kupac;
        System.out.println(kupac.getIdKupac());
        inicijalizujFormu();
        postaviListenere();
    }
    
    

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        PromeniKupacForm f = (PromeniKupacForm) forma;

        kupac.setIme(f.getImeField().getText().trim());
        kupac.setPrezime(f.getPrezimeField().getText().trim());
        kupac.setMail(f.getEmailField().getText().trim());
        kupac.setTelefon(f.getTelefonField().getText().trim());

        String datumTekst = f.getDatumField().getText().trim();
        if (!datumTekst.isEmpty()) {
            try {
                kupac.setDatumRodjenja(LocalDate.parse(datumTekst));
            } catch (DateTimeParseException ex) {
                kupac.setDatumRodjenja(null);
                f.prikaziErrorPane("Los format datuma: unesite GGGG-MM-DD", null);
                return null;
            }
        }

        Mesto mesto = (Mesto) f.getMestoCombo().getSelectedItem();
        kupac.setMesto(mesto);

        return kupac;        
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void postaviListenere() {
        PromeniKupacForm f = (PromeniKupacForm) forma;
        
        f.getPotvrdiIzmeneButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Kupac kupac = (Kupac) formToOdo();

                if(kupac == null){
                    return;
                }
                if (kupac.getIme().isEmpty()) {
                    forma.prikaziErrorPane("Unesite ime", null);
                    return;
                }
                if (kupac.getPrezime().isEmpty()) {
                    forma.prikaziErrorPane("Unesite prezime", null);
                    return;
                }
                if (kupac.getDatumRodjenja() == null) {
                    forma.prikaziErrorPane("Unesite ispravan datum rodjenja (format: GGGG-MM-DD)", null);
                    return;
                }
                if (kupac.getMesto() == null) {
                    forma.prikaziErrorPane("Izaberite mesto", null);
                    return;
                }

                Response response = sendReceive(Operacija.PROMENI_KUPCA, kupac);
                if (response == null) return;

                if (response.isSuccess()) {
                    forma.prikaziInfoPane("Kupac je uspesno azuriran!");
                } else {
                    forma.prikaziErrorPane("Greska pri azuriiranju kupca", response.getException());
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
        PromeniKupacForm f = (PromeniKupacForm) forma;

        f.getImeField().setText(kupac.getIme());
        f.getPrezimeField().setText(kupac.getPrezime());
        f.getEmailField().setText(kupac.getMail());
        f.getTelefonField().setText(kupac.getTelefon());

        f.getDatumField().setText(kupac.getDatumRodjenja().toString());
        
        Response mestaResponse = sendReceive(Operacija.VRATI_LISTU_SVI_MESTO, null);
        if (mestaResponse != null && mestaResponse.isSuccess()) {
            List<Mesto> mesta = (List<Mesto>) mestaResponse.getResult();
            f.getMestoCombo().setModel(new DomenskiComboBoxModel<>(mesta));
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju mesta", null);
        }
        f.getMestoCombo().setSelectedItem(kupac.getMesto());
    }
    
}
