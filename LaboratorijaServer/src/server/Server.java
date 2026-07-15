/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import util.ConfigReader;
import database.ConnectionPool;
import forms.ServerForm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author totic
 */
public class Server extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>(); // ne static lista jer vise threadova bi brisalo elemente liste, NIJE SAFE
    private final int PORT;
    private ServerForm serverForm;
    private Thread proveraKonekcijeThread;
    private volatile boolean radiProveru = false;
    
    public Server(ServerForm forma){
        this.serverForm = forma;
        ConfigReader cr = new ConfigReader();
        String port = cr.getProperty("server_port");
        if(port == null){
            PORT = 9000;
            LOGGER.log(Level.INFO, "Neuspesno procitana konfiguracija. Podesen port 9000 za slusanje.");
        }else{
            PORT = Integer.parseInt(port);
        }    
        LOGGER.log(Level.INFO, "Server konfigurisan.");
    }
    
    @Override
    public void run() {
        boolean success = proveriKonekcijuSaBazom();
        if (!success){
            serverForm.osveziBazaKonekcijaLabel(false);
            LOGGER.log(Level.SEVERE, "Nije moguca konekcija sa bazom podataka.");
            if(serverForm != null)
                serverForm.prikaziErrorPane("Neuspesna veza sa bazom", null);             
            return;
        }else{
            LOGGER.log(Level.INFO, "Uspesna konekcija sa bazom podataka.");
            serverForm.osveziBazaKonekcijaLabel(true);
        }
        if(serverForm != null)
            serverForm.osveziServerStatusLabel(true);
        LOGGER.log(Level.INFO, "Server pokrenut. Slusanje na portu: " + PORT);
        
        pokreniPeriodicnuProveru();
        
        try {
            serverSocket = new ServerSocket(PORT);
            while (!isInterrupted()) {
                Socket cs = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(cs, this);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "Server process prekinut");
            if(serverForm != null){
                serverForm.osveziBazaKonekcijaLabel(false);
                serverForm.osveziServerStatusLabel(false);
            }
            return;
        }
    }
    
    public boolean proveriKonekcijuSaBazom(){
        int attempts = 0;
        if(serverForm != null){
            serverForm.getPokreni().setEnabled(false);
            serverForm.getZaustavi().setEnabled(false);
        }
        while (attempts < 3) {
            ConnectionPool pool = ConnectionPool.getInstance();
            if (pool.proveriKonekciju()) {
                if (serverForm != null) {
                    serverForm.getPokreni().setEnabled(false);
                    serverForm.getZaustavi().setEnabled(true);
                }
                return true;
            }
            attempts++;
            LOGGER.log(Level.WARNING, "Baza nije spremna. Pokušaj " + attempts + " od 3...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if(serverForm != null){
            serverForm.getPokreni().setEnabled(true);
            serverForm.getZaustavi().setEnabled(false);
        }
        return false;
    }

    private void pokreniPeriodicnuProveru() {
        radiProveru = true;
        proveraKonekcijeThread = new Thread(() -> {
            while (radiProveru) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
                LOGGER.log(Level.WARNING, "Pokusaj ponovog povezivanja sa bazom...");
                boolean ziva = ConnectionPool.getInstance().proveriKonekciju();

                if (!ziva) {
                    LOGGER.log(Level.WARNING, "Konekcija sa bazom je izgubljena.");
                }
                if (serverForm != null) {
                    serverForm.osveziBazaKonekcijaLabel(ziva);
                }
            }
        });
        proveraKonekcijeThread.start();
    }    
    
    public void zaustavi() {
        radiProveru = false;
        if (proveraKonekcijeThread != null) {
            proveraKonekcijeThread.interrupt();
        }        
        interrupt(); // interruptuje se accept funkcija
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                //
            }
        }
        // OVDE MORA SINHRONIZACIJA da ne bi bio ConcurrentModificaitonException. 
        // objasnnjenje: lista ima iterator. ide se po listi i istovremeno se izbacuju elementi iz nje, pravi se problem.
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.zaustavi();
            }
        }
        LOGGER.log(Level.INFO, "Server zaustavljen");
        if(serverForm != null){
            serverForm.osveziServerStatusLabel(false);
            serverForm.osveziBazaKonekcijaLabel(false);
        }
    }

    public void removeClient(ClientHandler client) {
        // zaustavi se zove u sinhronizovanom bloku, sinhronizacija ovde bila bi dupla, ali ostavio sam je
        synchronized(clients){
            clients.remove(client);
        }        
    }

    public ServerForm getServerForm() {
        return serverForm;
    }

    public void setServerForm(ServerForm serverForm) {
        this.serverForm = serverForm;
    }
    
    
    
}

