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
import domen.Radnik;
import domen.ZahtevZaAnalizu;
import forms.ZahtevDetaljiForm;
import models.StavkaTableModel;

/**
 *
 * @author totic
 */
public class ZahtevDetaljiKontroler extends OpstiKontrolerKI{
    
    private ZahtevZaAnalizu zahtev;

    public ZahtevDetaljiKontroler(OpstaEkranskaForma forma, ZahtevZaAnalizu zahtev) {
        super(forma);
        this.zahtev = zahtev;
        inicijalizujFormu();
    }
    
    

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void postaviListenere() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void inicijalizujFormu() {
        
        ZahtevDetaljiForm f = (ZahtevDetaljiForm) forma;
        f.getStatusCombo().setEnabled(false);
        
        Response zahtevResponse = sendReceive(Operacija.PRETRAZI_ZAHTEV_ZA_ANALIZU, zahtev);
        if(!zahtevResponse.isSuccess()){
            f.prikaziErrorPane("GRESKA", zahtevResponse.getException());
            f.dispose();
            return;
        }
        this.zahtev = (ZahtevZaAnalizu) zahtevResponse.getResult();
        
        // PODACI ZAHTEVA
        f.getRadnikField().setText(zahtev.getRadnik().getIme() + " " + zahtev.getRadnik().getPrezime());
        f.getKupacField().setText(zahtev.getKupac().getIme() + " " + zahtev.getKupac().getPrezime());
        f.getDatumField().setText(zahtev.getDatum().toString());
        f.getPrioritetCheck().setSelected(zahtev.isPrioritet());
        f.getUkupnaCenaField().setText(Double.toString(zahtev.getUkupnaCenaZahteva()));
        f.getStatusCombo().setSelectedItem(zahtev.getStatus());
        
        
        // STAVKE
        f.getTabelaStavke().setModel(new StavkaTableModel(zahtev.getStavke()));
    }
    
}
