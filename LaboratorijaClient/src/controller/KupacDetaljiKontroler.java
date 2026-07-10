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
import forms.KupacDetaljiForm;
import java.util.List;
import models.DomenskiComboBoxModel;

/**
 *
 * @author totic
 */
public class KupacDetaljiKontroler extends OpstiKontrolerKI{

    private Kupac kupac;
    
    public KupacDetaljiKontroler(OpstaEkranskaForma forma, Kupac kupac) {
        super(forma);
        this.kupac = kupac;
        inicijalizujFormu();
        
    }
    
    

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        
    }

    @Override
    protected void postaviListenere() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void inicijalizujFormu() {
        KupacDetaljiForm f = (KupacDetaljiForm) forma;
        Response kupacResponse = sendReceive(Operacija.PRETRAZI_KUPCA, kupac);
        if(kupacResponse.getException() != null){
            f.prikaziErrorPane("Greska pri ucitavanju kupca: " + kupacResponse.getException().getMessage(), null);
            f.dispose();
            return;
        }
        
        this.kupac = (Kupac) kupacResponse.getResult();
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
        f.getMestoCombo().setEnabled(false);
    }
    
}
