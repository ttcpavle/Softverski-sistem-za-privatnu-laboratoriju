/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import domen.StavkaZahteva;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author totic
 */
public class StavkaTableModel extends AbstractTableModel{
    
    private List<StavkaZahteva> stavke;
    private String[] columns = {"Naziv proizvoda", "Kolicina", "Jedinicna cena", "Ukupna cena stavke"};

    public StavkaTableModel() {
        this.stavke = new ArrayList<>();
    }
    
    public void setStavke(List<StavkaZahteva> stavke) {
        this.stavke = stavke;
        fireTableDataChanged();
    }
 
    public List<StavkaZahteva> getStavke() {
        return stavke;
    }
    
    public void dodajStavku(StavkaZahteva stavka) {
        stavke.add(stavka);
        fireTableRowsInserted(stavke.size() - 1, stavke.size() - 1);
    }
 
    public void ukloniStavku(int rowIndex) {
        stavke.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    @Override
    public int getRowCount() {
        return stavke != null ? stavke.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StavkaZahteva izabranaStavka = stavke.get(rowIndex);
        switch(columnIndex){
            case 0:
                return izabranaStavka.getProizvod() != null ? izabranaStavka.getProizvod().getNaziv() : "N/A";
            case 1:
                return izabranaStavka.getKolicina();
            case 2:
                return izabranaStavka.getJedinicnaCena();
            case 3:
                return izabranaStavka.getUkupnaCena();
            default:
                return "N/A";
        }
    }
        
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }        
    
}
