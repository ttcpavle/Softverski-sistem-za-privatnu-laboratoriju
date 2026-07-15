package models;

import domen.Proizvod;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author totic
 */
public class ProizvodTableModel extends AbstractTableModel {

    private List<Proizvod> proizvodi;

    private final String[] columns = {"ID", "Naziv", "Vreme cekanja (h)", "Cena"};
    private final Class[] columnClasses = {Integer.class, String.class, LocalTime.class, Double.class};

    public ProizvodTableModel() {
        this.proizvodi = new ArrayList<>();
    }

    public ProizvodTableModel(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
        fireTableDataChanged();
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    @Override
    public int getRowCount() {
        return proizvodi == null ? 0 : proizvodi.size();
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
        Proizvod p = proizvodi.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return p.getIdProizvod();
            case 1:
                return p.getNaziv();
            case 2:
                return p.getVremeCekanjaSati();
            case 3:
                return p.getCena();
            default:
                return null;
        }
    }

    public Proizvod getProizvodAt(int rowIndex) {
        return proizvodi.get(rowIndex);
    }
}
