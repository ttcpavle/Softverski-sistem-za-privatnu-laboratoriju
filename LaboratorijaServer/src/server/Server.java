/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import util.ConfigReader;
import communication.Response;
import database.ConnectionPool;
import forms.ServerForm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;

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
    
    public Server(){
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
            serverForm.osveziStatusLabel(false);
            LOGGER.log(Level.SEVERE, "Nije moguca konekcija sa bazom podataka.");
            if(serverForm != null)
                serverForm.prikaziErrorPane("Neuspesna veza sa bazom", null);             
            return;
        }else{
            LOGGER.log(Level.INFO, "Uspesna konekcija sa bazom podataka.");
        }
        
        serverForm.osveziStatusLabel(true);
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
            return;
        }
    }
    
    public boolean proveriKonekcijuSaBazom(){
        int attempts = 0;
        while (attempts < 3) {
            ConnectionPool pool = ConnectionPool.getInstance();
            if (pool.proveriKonekciju()) {
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
                boolean ziva = ConnectionPool.getInstance().proveriKonekciju();
                if (serverForm != null) {
                    serverForm.osveziStatusLabel(ziva);
                }
                if (!ziva) {
                    LOGGER.log(Level.WARNING, "Konekcija sa bazom je izgubljena.");
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

