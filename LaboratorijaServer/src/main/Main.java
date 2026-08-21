package main;

import forms.ServerForm;
import server.LogSystem;

public class Main {
    public static void main(String[] args){
        LogSystem.setup();
        ServerForm forma = new ServerForm();
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }
}
