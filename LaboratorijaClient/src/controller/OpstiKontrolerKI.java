package controller;

import communication.Operacija;
import communication.Request;
import communication.Response;
import connection.Connection;
import domen.OpstiDomenskiObjekat;
import domen.OpstaEkranskaForma;

public abstract class OpstiKontrolerKI {

    /*
    IDEJA: funkcije pretrazi,zapamti,promeni,sacuvaj,obrisi... u sebi koriste formToOdo i odoToForm. U formToOdo kreira se odo na osnovu forme "forma"
    U  funkciji odoToForm uzima se objekat koji se dobio od servera i koristi u operacijama kao npr pretrazi.
    */
    protected OpstaEkranskaForma forma;

    public OpstiKontrolerKI(OpstaEkranskaForma forma) {
        this.forma = forma;
    }  
    
    public abstract OpstiDomenskiObjekat formToOdo();
    public abstract void odoToForm(OpstiDomenskiObjekat odo);
    protected abstract void postaviListenere();
    protected abstract void inicijalizujFormu();

    protected Response sendReceive(Operacija operacija, OpstiDomenskiObjekat odo) {
        try {
            Request req = new Request(operacija, odo);
            Connection.getInstance().getSender().send(req);
            return (Response) Connection.getInstance().getReceiver().receive();
        } catch (Exception e) {
            forma.prikaziErrorPane("Greska u komunikaciji", e);
            return null;
        }
    }
}
