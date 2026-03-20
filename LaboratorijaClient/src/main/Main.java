/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import connection.Connection;
import controller.PrijavaRadnikKontroler;
import forms.MainForm;
import forms.PrijavaRadnikForm;
import javax.swing.JOptionPane;

/**
 *
 * @author totic
 */
public class Main {

    public static void main(String[] args) {
        if (Connection.getInstance().getSocket() == null) {
            JOptionPane.showMessageDialog(null, "Neuspesna konekcija sa serverom. Kontaktirajte administratora", "Greska", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        PrijavaRadnikForm forma = new PrijavaRadnikForm();
        
        PrijavaRadnikKontroler kontroler = new PrijavaRadnikKontroler(forma);
        
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }

}
