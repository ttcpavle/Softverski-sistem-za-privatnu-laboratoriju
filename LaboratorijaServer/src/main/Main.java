/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import database.ConnectionPool;
import forms.ServerForm;
import server.LogSystem;
import server.Server;

/**
 *
 * @author totic
 */
public class Main {
    public static void main(String[] args){
        LogSystem.setup();
        Server server = new Server();
        ServerForm forma = new ServerForm();
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }
}
