package controller;

import communication.Operacija;
import communication.Response;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import forms.PretraziProizvodForm;
import forms.ProizvodDetaljiForm;
import forms.ZahtevDetaljiForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTable;
import models.ProizvodTableModel;

/**
 *
 * @author totic
 */
public class PretraziProizvodKontroler extends OpstiKontrolerKI {

    private ProizvodTableModel proizvodTableModel;

    public PretraziProizvodKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }

    @Override
    public OpstiDomenskiObjekat formToOdo() {
        PretraziProizvodForm f = (PretraziProizvodForm) forma;
        Proizvod p = new Proizvod();
        p.setNaziv(f.getNazivProizvodField().getText().trim());
        return p;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        // nije potrebna implementacija
    }

    @Override
    protected void postaviListenere() {
        PretraziProizvodForm f = (PretraziProizvodForm) forma;

        f.getPretraziButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Proizvod proizvod = (Proizvod) formToOdo();

                Response response = sendReceive(Operacija.VRATI_LISTU_PROIZVOD, proizvod);
                if (response == null) {
                    return;
                }
                if (!response.isSuccess()) {
                    forma.prikaziErrorPane("Greska pri pretrazi proizvoda", response.getException());
                    return;
                }

                List<Proizvod> pronadjeniProizvodi = (List<Proizvod>) response.getResult();
                proizvodTableModel.setProizvodi(pronadjeniProizvodi);

                if (pronadjeniProizvodi.isEmpty()) {
                    forma.prikaziErrorPane("Sistem ne moze da nadje proizvode po zadatim kriterijumima", null);
                } else {
                    forma.prikaziInfoPane("Sistem je nasao proizvode po zadatim kriterijumima");
                }
            }
        });

        f.getDetaljiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable tabela = f.getProizvodTable();
                int izabraniRed = tabela.getSelectedRow();

                if (izabraniRed == -1) {
                    forma.prikaziErrorPane("Nije izabran red u tabeli", null);
                    return;
                }

                int konvertovaniRed = tabela.convertRowIndexToModel(izabraniRed);
                Proizvod izabraniProizvod = proizvodTableModel.getProizvodAt(konvertovaniRed);

                ProizvodDetaljiForm detaljiForm = new ProizvodDetaljiForm();
                ProizvodDetaljiKontroler detalji = new ProizvodDetaljiKontroler(detaljiForm, izabraniProizvod);
                detaljiForm.setLocationRelativeTo(null);
                detaljiForm.setVisible(true);
            }
        });
    }

    @Override
    protected void inicijalizujFormu() {
        PretraziProizvodForm f = (PretraziProizvodForm) forma;

        proizvodTableModel = new ProizvodTableModel();
        f.getProizvodTable().setModel(proizvodTableModel);
    }
}