package models;

import domen.StavkaZahteva;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.table.AbstractTableModel;

public class StavkaTableModel extends AbstractTableModel{

    private List<StavkaZahteva> stavke;
    private String[] columns = {"Naziv proizvoda", "Kolicina", "Jedinicna cena", "Ukupna cena stavke"};
    private Consumer<String> naGresku;

    public StavkaTableModel() {
        this.stavke = new ArrayList<>();
    }

    public StavkaTableModel(List<StavkaZahteva> stavke) {
        this.stavke = stavke;
    }
    
    
    
    public void setStavke(List<StavkaZahteva> stavke) {
        this.stavke = stavke;
        fireTableDataChanged();
    }
 
    public List<StavkaZahteva> getStavke() {
        return stavke;
    }

    public void setNaGresku(Consumer<String> naGresku) {
        this.naGresku = naGresku;
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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 1 ? Integer.class : super.getColumnClass(columnIndex);
    }

    // samo kolicina je izmenljiva direktno u tabeli
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex != 1) {
            return;
        }
        int novaKolicina;
        try {
            novaKolicina = value instanceof Integer ? (Integer) value : Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException ex) {
            if (naGresku != null) naGresku.accept("Kolicina mora biti pozitivan ceo broj");
            return;
        }
        if (novaKolicina <= 0) {
            if (naGresku != null) naGresku.accept("Kolicina mora biti pozitivan ceo broj");
            return;
        }
        StavkaZahteva stavka = stavke.get(rowIndex);
        stavka.setKolicina(novaKolicina);
        stavka.setUkupnaCena(novaKolicina * stavka.getJedinicnaCena());
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

}
