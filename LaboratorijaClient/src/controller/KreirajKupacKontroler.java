package controller;

import communication.Operacija;
import communication.Response;
import domen.Kupac;
import domen.Mesto;
import domen.OpstaEkranskaForma;
import domen.OpstiDomenskiObjekat;
import forms.KreirajKupacForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import models.DomenskiComboBoxModel;

/**
 *
 * @author totic
 */
public class KreirajKupacKontroler extends OpstiKontrolerKI {

    public KreirajKupacKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }

    /**
     * Cita podatke sa forme i konstruise Kupac objekat.
     */
    @Override
    public OpstiDomenskiObjekat formToOdo() {
        KreirajKupacForm f = (KreirajKupacForm) forma;

        Kupac kupac = new Kupac();
        kupac.setIme(f.getImeField().getText().trim());
        kupac.setPrezime(f.getPrezimeField().getText().trim());
        kupac.setMail(f.getEmailField().getText().trim());
        kupac.setTelefon(f.getTelefonField().getText().trim());

        String datumTekst = f.getDatumField().getText().trim();
        if (!datumTekst.isEmpty()) {
            try {
                kupac.setDatumRodjenja(LocalDate.parse(datumTekst));
            } catch (DateTimeParseException ex) {
                kupac.setDatumRodjenja(null);
            }
        }

        Mesto mesto = (Mesto) f.getMestoCombo().getSelectedItem();
        kupac.setMesto(mesto);

        return kupac;
    }

    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        // nije potrebna implementacija (forma za kreiranje, ne za pregled)
    }

    @Override
    protected void postaviListenere() {
        KreirajKupacForm f = (KreirajKupacForm) forma;

        // --- Sacuvaj kupca ---
        f.getSacuvajButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kupac kupac = (Kupac) formToOdo();

                if (kupac.getIme().isEmpty()) {
                    forma.prikaziErrorPane("Unesite ime", null);
                    return;
                }
                if (kupac.getPrezime().isEmpty()) {
                    forma.prikaziErrorPane("Unesite prezime", null);
                    return;
                }
                if (kupac.getDatumRodjenja() == null) {
                    forma.prikaziErrorPane("Unesite ispravan datum rodjenja (format: GGGG-MM-DD)", null);
                    return;
                }
                if (kupac.getMesto() == null) {
                    forma.prikaziErrorPane("Izaberite mesto", null);
                    return;
                }

                Response response = sendReceive(Operacija.KREIRAJ_KUPCA, kupac);
                if (response == null) return;

                if (response.isSuccess()) {
                    forma.prikaziInfoPane("Kupac je uspesno kreiran!");
                    ocistiFormu();
                } else {
                    forma.prikaziErrorPane("Greska pri kreiranju kupca", response.getException());
                }
            }
        });

        // --- Ocisti formu ---
        f.getOcistiFormuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ocistiFormu();
            }
        });

        // --- Glavna forma ---
        f.getGlavnaFormaButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forma.dispose();
            }
        });
    }

    /**
     * Ucitava listu mesta sa servera za popunjavanje mestoCombo-a.
     */
    @Override
    protected void inicijalizujFormu() {
        KreirajKupacForm f = (KreirajKupacForm) forma;

        Response mestaResponse = sendReceive(Operacija.VRATI_LISTU_SVI_MESTO, null);
        if (mestaResponse != null && mestaResponse.isSuccess()) {
            List<Mesto> mesta = (List<Mesto>) mestaResponse.getResult();
            f.getMestoCombo().setModel(new DomenskiComboBoxModel<>(mesta));
        } else {
            forma.prikaziErrorPane("Greska pri ucitavanju mesta", null);
        }
    }

    private void ocistiFormu() {
        KreirajKupacForm f = (KreirajKupacForm) forma;
        f.getImeField().setText("");
        f.getPrezimeField().setText("");
        f.getDatumField().setText("");
        f.getEmailField().setText("");
        f.getTelefonField().setText("");
        if (f.getMestoCombo().getItemCount() > 0) f.getMestoCombo().setSelectedIndex(0);
    }
}
