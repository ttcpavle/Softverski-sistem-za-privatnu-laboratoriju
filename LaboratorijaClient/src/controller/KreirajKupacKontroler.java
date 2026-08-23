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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import models.DomenskiComboBoxModel;

public class KreirajKupacKontroler extends OpstiKontrolerKI {

    // id novog kupca koji je kreiran
    private int idNoviKupac;
    // da li je kupac zapamcen
    private boolean sacuvano;

    public KreirajKupacKontroler(OpstaEkranskaForma forma) {
        super(forma);
        inicijalizujFormu();
        postaviListenere();
    }


    @Override
    public OpstiDomenskiObjekat formToOdo() {
        KreirajKupacForm f = (KreirajKupacForm) forma;

        Kupac kupac = new Kupac();
        kupac.setIdKupac(idNoviKupac);
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
                f.prikaziErrorPane("Los format datuma: unesite GGGG-MM-DD", null);
                return null;
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

        // --- Sacuvaj (zapamti) kupca ---
        f.getSacuvajButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kupac kupac = (Kupac) formToOdo();

                if (kupac == null) {
                    return;
                }
                if (kupac.getIme().isEmpty()) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti kupca: Unesite ime", null);
                    return;
                }
                if (kupac.getPrezime().isEmpty()) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti kupca: Unesite prezime", null);
                    return;
                }
                if (kupac.getDatumRodjenja() == null) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti kupca: Unesite ispravan datum rodjenja (format: GGGG-MM-DD)", null);
                    return;
                }
                if (kupac.getMesto() == null) {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti kupca: Izaberite mesto", null);
                    return;
                }

                Response response = sendReceive(Operacija.PROMENI_KUPCA, kupac);
                if (response == null) return;

                if (response.isSuccess()) {
                    sacuvano = true;
                    forma.prikaziInfoPane("Sistem je zapamtio kupca.");
                    zatvoriFormu();
                } else {
                    forma.prikaziErrorPane("Sistem ne moze da zapamti kupca: " + response.getException().getMessage(), null);
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
                zatvoriFormu();
            }
        });

        // Zatvaranje forme preko X dugmeta prozora - ista logika kao Glavna forma
        forma.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                zatvoriFormu();
            }
        });
    }


    // ucitava liste za combobox i odmah kreira prazan slog kupca
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

        javax.swing.SwingUtilities.invokeLater(this::kreirajPrazanKupac);
    }

    private void kreirajPrazanKupac() {
        Response response = sendReceive(Operacija.KREIRAJ_KUPCA, new Kupac());
        if (response != null && response.isSuccess()) {
            Kupac kreiraniKupac = (Kupac) response.getResult();
            idNoviKupac = kreiraniKupac.getIdKupac();
            sacuvano = false;
            forma.prikaziInfoPane("Sistem je kreirao kupca.");
        } else {
            String razlog = response != null ? response.getException().getMessage() : "greska u komunikaciji";
            forma.prikaziErrorPane("Sistem ne moze da kreira kupca: " + razlog, null);
            forma.dispose();
        }
    }


    // ako kupac nije zapamcen, brise se slog pre zatvaranja forme
    private void zatvoriFormu() {
        if (!sacuvano && idNoviKupac > 0) {
            sendReceive(Operacija.OBRISI_KUPCA, new Kupac(idNoviKupac));
        }
        forma.dispose();
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
