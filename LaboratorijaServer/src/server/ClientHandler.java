/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import communication.Operacija;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import domen.OpstiDomenskiObjekat;
import java.io.IOException;

import java.net.Socket;
import java.net.SocketException;

import java.util.logging.Level;
import java.util.logging.Logger;
import operacije.PrijaviRadnika;

/**
 *
 * @author totic
 */
public class ClientHandler extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    private final Server server;
    private final Socket socket;
    private final Receiver receiver;
    private final Sender sender;

    public ClientHandler(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.receiver = new Receiver(socket);
        this.sender = new Sender(socket);
        this.server = server;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Request requestObj = null;
                try {
                    requestObj = (Request) receiver.receive();
                } catch (Exception ex) {
                    LOGGER.log(Level.INFO, "Klijent diskonektovan. Gasenje klijentske niti");
                    break;
                }
                OpstiDomenskiObjekat argumentObj = (OpstiDomenskiObjekat) requestObj.getArgument();
                Response serverResponse = null;
                Operacija op = requestObj.getOperation();

                switch (op) {
                    // ===== ZAHTEV ZA ANALIZU =====
                    case KREIRAJ_ZAHTEV_ZA_ANALIZU:
                        // TODO: implementirati
                        break;

                    case PROMENI_ZAHTEV_ZA_ANALIZU:
                        // TODO: implementirati
                        break;

                    case OBRISI_ZAHTEV_ZA_ANALIZU:
                        // TODO: implementirati
                        break;

                    case PRETRAZI_ZAHTEV_ZA_ANALIZU:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_ZAHTEV_ZA_ANALIZU:
                        // TODO: implementirati
                        break;

                    // ===== KUPAC =====
                    case KREIRAJ_KUPCA:
                        // TODO: implementirati
                        break;

                    case PROMENI_KUPCA:
                        // TODO: implementirati
                        break;

                    case OBRISI_KUPCA:
                        // TODO: implementirati
                        break;

                    case PRETRAZI_KUPCA:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_KUPAC:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_SVI_KUPAC:
                        // TODO: implementirati
                        break;

                    // ===== RADNIK =====
                    case KREIRAJ_RADNIKA:
                        // TODO: implementirati
                        break;

                    case PROMENI_RADNIKA:
                        // TODO: implementirati
                        break;

                    case OBRISI_RADNIKA:
                        // TODO: implementirati
                        break;

                    case PRETRAZI_RADNIKA:
                        // TODO: implementirati
                        break;

                    case PRIJAVI_RADNIKA:
                        PrijaviRadnika pr = new PrijaviRadnika();
                        serverResponse = pr.opsteIzvrsenjeSO(argumentObj);
                        break;

                    case VRATI_LISTU_RADNIK:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_SVI_RADNIK:
                        // TODO: implementirati
                        break;

                    // ===== PROIZVOD =====
                    case UBACI_PROIZVOD:
                        // TODO: implementirati
                        break;

                    case PROMENI_PROIZVOD:
                        // TODO: implementirati
                        break;

                    case OBRISI_PROIZVOD:
                        // TODO: implementirati
                        break;

                    case PRETRAZI_PROIZVOD:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_PROIZVOD:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_SVI_PROIZVOD:
                        // TODO: implementirati
                        break;

                    // ===== MESTO =====
                    case UBACI_MESTO:
                        // TODO: implementirati
                        break;

                    case PROMENI_MESTO:
                        // TODO: implementirati
                        break;

                    case OBRISI_MESTO:
                        // TODO: implementirati
                        break;

                    case PRETRAZI_MESTO:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_MESTO:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_SVI_MESTO:
                        // TODO: implementirati
                        break;

                    // ===== TIP USLUGE =====
                    case UBACI_TIP_USLUGE:
                        // TODO: implementirati
                        break;

                    case PROMENI_TIP_USLUGE:
                        // TODO: implementirati
                        break;

                    case OBRISI_TIP_USLUGE:
                        // TODO: implementirati
                        break;

                    case PRETRAZI_TIP_USLUGE:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_TIP_USLUGE:
                        // TODO: implementirati
                        break;

                    case VRATI_LISTU_SVI_TIP_USLUGE:
                        // TODO: implementirati
                        break;

                    default:
                        LOGGER.log(Level.INFO, "Nepoznata operacija: " + op);
                        serverResponse = new Response(null, new Exception("Nepoznata operacija: " + op), false);
                        break;
                }

                sender.send(serverResponse);

            } catch (Exception ex) {
                LOGGER.log(Level.INFO, "Klijentska nit prekinuta: " + ex.getMessage(), ex);
                break;
            }
        }
        cleanup();
    }

    private void cleanup() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Neuspesno zatvaranje socketa");
            }
        }
        server.removeClient(this);
    }

    // druga nit moze da zaustavi klijenta
    public void zaustavi() {
        interrupt();
    }
}
