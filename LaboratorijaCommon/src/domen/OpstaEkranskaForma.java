/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domen;

import domen.OpstiDomenskiObjekat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author totic
 */
public abstract class OpstaEkranskaForma extends JFrame{

    public OpstaEkranskaForma() {
    }

    
    

    protected abstract void ocistiFormu();
    
    public void prikaziErrorPane(String poruka, Exception ex){
        if(ex != null){
            JOptionPane.showMessageDialog(null,  poruka + "\n" + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null,  poruka, "Error",JOptionPane.ERROR_MESSAGE);
    }
    
    public void prikaziInfoPane(String poruka){
        JOptionPane.showMessageDialog(null,  poruka,"Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
