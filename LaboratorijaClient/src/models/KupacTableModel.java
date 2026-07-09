/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import domen.Kupac;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author totic
 */
public class KupacTableModel extends AbstractTableModel {

    private List<Kupac> kupci;

    private final String[] columns = {"ID", "Ime", "Prezime", "Email", "Telefon", "Mesto"};

    private final Class[] columnClasses = {Integer.class, String.class, String.class, String.class, String.class, String.class};

    public KupacTableModel() {
        this.kupci = new ArrayList<>();
    }

    public KupacTableModel(List<Kupac> kupci) {
        this.kupci = kupci;
    }

    public void setKupci(List<Kupac> kupci) {
        this.kupci = kupci;
        fireTableDataChanged();
    }

    public List<Kupac> getKupci() {
        return kupci;
    }

    @Override
    public int getRowCount() {
        return kupci == null ? 0 : kupci.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Kupac k = kupci.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return k.getIdKupac();
            case 1:
                return k.getIme();
            case 2:
                return k.getPrezime();
            case 3:
                return k.getMail();
            case 4:
                return k.getTelefon();
            case 5:
                return k.getMesto() != null ? k.getMesto().getNaziv() : "N/A";
            default:
                return null;
        }
    }

    public Kupac getKupacAt(int rowIndex) {
        return kupci.get(rowIndex);
    }
    
    public void ukloniKupca(int rowIndex) {
        kupci.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
