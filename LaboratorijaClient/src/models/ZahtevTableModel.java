package models;

import domen.ZahtevZaAnalizu;
import java.time.LocalDate;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author totic
 */

public class ZahtevTableModel extends AbstractTableModel {

    private List<ZahtevZaAnalizu> zahtevi;
    
    private final String[] columns = {"ID", "Datum", "Status", "Prioritet", "Ukupna cena", "Radnik Ime", "Radnik prezime", "Kupac ime", "Kupac prezime"};

    private final Class[] columnClasses = {Integer.class, LocalDate.class, String.class, Boolean.class, Double.class, String.class, String.class, String.class, String.class};

    public ZahtevTableModel(List<ZahtevZaAnalizu> zahtevi) {
        this.zahtevi = zahtevi;
    }

    @Override
    public int getRowCount() {
        return zahtevi == null ? 0 : zahtevi.size();
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
        ZahtevZaAnalizu z = zahtevi.get(rowIndex);

        switch (columnIndex) {
            case 0: 
                return z.getIdZahtev();
            case 1: 
                return z.getDatum();
            case 2: 
                return z.getStatus();
            case 3: 
                return z.isPrioritet(); // JTable će ovde automatski iscrtati Checkbox
            case 4: 
                return z.getUkupnaCenaZahteva();
            case 5: 
                return z.getRadnik() != null ? z.getRadnik().getIme() : "N/A";
            case 6: 
                return z.getRadnik() != null ? z.getRadnik().getPrezime() : "N/A";
            case 7:
                return z.getKupac() != null ? z.getKupac().getIme() : "N/A";
            case 8:
                return z.getKupac() != null ? z.getKupac().getPrezime() : "N/A";
            default:
                return null;
        }
    }
}
