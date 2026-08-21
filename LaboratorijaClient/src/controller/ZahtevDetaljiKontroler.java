package controller;

import communication.Operacija;
import communication.Response;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.ZahtevZaAnalizu;
import forms.ZahtevDetaljiForm;
import models.StavkaTableModel;

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
        f.getPrioritetCheck().setEnabled(false);
        Response zahtevResponse = sendReceive(Operacija.PRETRAZI_ZAHTEV_ZA_ANALIZU, zahtev);
        if(!zahtevResponse.isSuccess()){
            f.prikaziErrorPane("Sistem ne moze da nadje zahtev za analizu: " + zahtevResponse.getException().getMessage(), null);
            f.dispose();
            return;
        }
        f.prikaziInfoPane("Sistem je nasao zahtev za analizu");
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
