package server;

import util.ConfigReader;
import database.ConnectionPool;
import forms.ServerForm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Server extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>(); // ne static lista jer vise threadova bi brisalo elemente liste
    private final int PORT;
    private ServerForm serverForm;
    private Thread proveraKonekcijeThread;
    private volatile boolean radiProveru = false;
    private boolean zadnjiStatusBaze = false;
    
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
            zadnjiStatusBaze = false;
            if(serverForm != null)
                serverForm.prikaziErrorPane("Neuspesna veza sa bazom", null);             
            return;
        }else{
            LOGGER.log(Level.INFO, "Uspesna konekcija sa bazom podataka.");
            serverForm.osveziBazaKonekcijaLabel(true);
            zadnjiStatusBaze = true;
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
            LOGGER.log(Level.INFO, "Server process prekinut");
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "Server socket zatvoren");
        } finally{
            zaustaviSveKlijenteIResurse();
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

        azurirajDugmadForme(true, false);
        return false;
    }

    private void pokreniPeriodicnuProveru() {
        radiProveru = true;
        proveraKonekcijeThread = new Thread(() -> {
            while (radiProveru) {
                boolean ziva = ConnectionPool.getInstance().proveriKonekciju();
                if (serverForm != null) {
                    serverForm.osveziBazaKonekcijaLabel(ziva);
                }
                try {
                    if (ziva) {
                        Thread.sleep(10000);
                        if(zadnjiStatusBaze == false){
                            LOGGER.log(Level.INFO, "Konekcija sa bazom je ponovo uspostavljena");
                            zadnjiStatusBaze = true;
                        }
                    } else {
                        zadnjiStatusBaze = false;
                        // brza provera statusa baze ako je izgubljena konekcija
                        LOGGER.log(Level.WARNING, "Konekcija sa bazom je izgubljena");
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        proveraKonekcijeThread.setName("ProveraKonekcije-Thread");
        proveraKonekcijeThread.start();
    }    
    

    
    public void zaustavi() {
        radiProveru = false;
        if (proveraKonekcijeThread != null) {
            proveraKonekcijeThread.interrupt();
        }

        interrupt();

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Greska pri zatvaranju ServerSocket-a", ex);
            }
        }
    }

    private void zaustaviSveKlijenteIResurse() {
        // CopyOnWriteArrayList sprecava ConcurrentModificationException bez potrebe za sinhronizacijom
        for (ClientHandler client : clients) {
            client.zaustavi();
        }
        clients.clear();

        LOGGER.log(Level.INFO, "Server zaustavljen");
        if (serverForm != null) {
            serverForm.osveziServerStatusLabel(false);
            serverForm.osveziBazaKonekcijaLabel(false);
            azurirajDugmadForme(true, false);
        }
    }

    private void azurirajDugmadForme(boolean pokreniEnabled, boolean zaustaviEnabled) {
        if (serverForm != null) {
            serverForm.getPokreni().setEnabled(pokreniEnabled);
            serverForm.getZaustavi().setEnabled(zaustaviEnabled);
        }
    }
    // ovu funkciju zove klijentska nit. Klijentska nit ce se vec ugasiti pravilno, ovo je samo uklanjanje iz liste
    public void removeClient(ClientHandler client) {

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

