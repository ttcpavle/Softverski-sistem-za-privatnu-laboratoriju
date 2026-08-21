package controller;

import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import forms.ProizvodDetaljiForm;

public class ProizvodDetaljiKontroler extends OpstiKontrolerKI{

    private Proizvod proizvod;
    public ProizvodDetaljiKontroler(OpstaEkranskaForma forma, Proizvod proizvod) {
        super(forma);
        this.proizvod = proizvod;
        inicijalizujFormu();
    }
    
    

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        return null;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        return;
    }

    @Override
    protected void postaviListenere() {
        return;
    }

    @Override
    protected void inicijalizujFormu() {
        ProizvodDetaljiForm f = (ProizvodDetaljiForm) forma;
        
        f.getIdField().setText(String.valueOf(proizvod.getIdProizvod()));
        f.getNazivField().setText(proizvod.getNaziv());
        f.getCenaField().setText(String.valueOf(proizvod.getCena()));
        f.getVremeCekanjaSatiField().setText(String.valueOf(proizvod.getVremeCekanjaSati()));
        f.getOpisArea().setText(proizvod.getOpis());
        f.prikaziInfoPane("Sistem je nasao proizvod");
    }
    
}
