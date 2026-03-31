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
    
    public Server(){
        ConfigReader cr = new ConfigReader();
        String port = cr.getProperty("server_port");
        if(port == null){
            PORT = 9000;
            LOGGER.log(Level.INFO, "Neuspesno procitana konfiguracija. Podesen port 9000 za slusanje.");
        }else{
            PORT = Integer.parseInt(port);
        }    
    }
    
    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Server pokrenut. Slusanje na portu: " + PORT);
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
        ConnectionPool.getInstance();
        if(ConnectionPool.isInicijalizovan()){
            return true;
        }else{
            if(serverForm != null)
                serverForm.prikaziErrorPane("Neuspesna veza sa bazom", null);
            return false;
        }        
    }

    public void zaustavi() {
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

