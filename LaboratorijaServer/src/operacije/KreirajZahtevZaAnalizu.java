/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import communication.Response;
import database.DBBroker;
import domen.OpstiDomenskiObjekat;

/**
 *
 * @author totic
 */
public class KreirajZahtevZaAnalizu extends OpstaSO{

    @Override
    protected Response preduslovi(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return null;
    }

    @Override
    protected Response izvrsenjeSO(OpstiDomenskiObjekat odo, DBBroker dbb) {
        return null;
    }
    
}
