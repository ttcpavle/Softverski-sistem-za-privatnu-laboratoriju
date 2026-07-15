/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import forms.ProizvodDetaljiForm;

/**
 *
 * @author totic
 */
public class ProizvodDetaljiKontroler extends OpstiKontrolerKI{

    private Proizvod proizvod;
    public ProizvodDetaljiKontroler(OpstaEkranskaForma forma, Proizvod proizvod) {
        super(forma);
        this.proizvod = proizvod;
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
        ProizvodDetaljiForm f = (ProizvodDetaljiForm) forma;
        
        f.getIdField().setText(String.valueOf(proizvod.getIdProizvod()));
        f.getNazivField().setText(proizvod.getNaziv());
        f.getCenaField().setText(String.valueOf(proizvod.getCena()));
        f.getVremeCekanjaSatiField().setText(String.valueOf(proizvod.getVremeCekanjaSati()));
        f.getOpisArea().setText(proizvod.getOpis());
        
    }
    
}
