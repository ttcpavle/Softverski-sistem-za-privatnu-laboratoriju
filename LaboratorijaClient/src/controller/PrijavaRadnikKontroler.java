
package controller;

import communication.Operacija;
import communication.Response;
import domen.OpstiDomenskiObjekat;
import domen.Radnik;
import forms.MainForm;
import domen.OpstaEkranskaForma;
import forms.PrijavaRadnikForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author totic
 */
public class PrijavaRadnikKontroler extends OpstiKontrolerKI {

    public PrijavaRadnikKontroler(OpstaEkranskaForma forma) {
        super(forma);
        postaviListenere();
    }


    @Override
    public OpstiDomenskiObjekat formToOdo() {
        PrijavaRadnikForm f = (PrijavaRadnikForm) forma;

        Radnik r = new Radnik();
        r.setKorisnickoIme(f.getKorisnickoImeText());
        r.setLozinka(f.getLozinkaText());
        return r;
    }
    
    
    @Override
    public void odoToForm(OpstiDomenskiObjekat odo) {
        // ne vraca se nista za prijavu
    }

    @Override
    protected void postaviListenere() {
        PrijavaRadnikForm f = (PrijavaRadnikForm) forma;

        f.getLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Radnik radnik = (Radnik) formToOdo();
                Response response = sendReceive(Operacija.PRIJAVI_RADNIKA, radnik);           
                //odoToForm(response.getArgument()) nije potrebno ovde
                if(response.getException() == null){
                    forma.prikaziInfoPane("Uspesna prijava!");
                    MainForm mf = new MainForm(); // kontroler nije potreban za ovu formu jer ima jednostavnu logiku, treba samo da otvara druge forme
                    forma.dispose();
                    mf.setLocationRelativeTo(null);
                    mf.setVisible(true);
                }else{
                    forma.prikaziErrorPane("neuspesna prijava: ", response.getException());
                }
            }
        });

    }

    @Override
    protected void inicijalizujFormu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
