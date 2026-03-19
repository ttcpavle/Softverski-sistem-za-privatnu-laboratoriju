/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import connection.Connection;
import forms.MainForm;

/**
 *
 * @author totic
 */
public class Main {

    public static void main(String[] args) {
        // kreiranje svih kontrolera
        // kreiranje svih formi sa prosledjenim kontrolerom
        MainForm form = new MainForm();
        form.setLocationRelativeTo(null);
        form.setVisible(true);
        // pokusaj kreiranja konekcije odmah na pocetku. Korisnik ce odmah dobiti informaciju o konekciji preko forme
        if(Connection.getInstance().getSocket() == null){
            form.prikaziErrorPane("Neuspesna konekcija sa serverom. Kontaktirajte administratora.", null);
        }
    }

}
