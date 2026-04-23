package models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class DomenskiComboBoxModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {

    private List<T> elementi;
    private T izabrani;

    public DomenskiComboBoxModel(List<T> elementi) {
        this.elementi = elementi != null ? elementi : new ArrayList<>();
        this.izabrani = this.elementi.isEmpty() ? null : this.elementi.get(0);
    }

    @Override
    public int getSize() {
        return elementi.size();
    }

    @Override
    public T getElementAt(int index) {
        return elementi.get(index);
    }

    @Override
    public void setSelectedItem(Object item) {
        this.izabrani = (T) item;
        fireContentsChanged(this, -1, -1);
    }

    @Override
    public T getSelectedItem() {
        return izabrani;
    }
}
