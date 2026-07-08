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
import domen.Kupac;
import domen.Mesto;
import domen.OpstiDomenskiObjekat;
import domen.Proizvod;
import domen.Radnik;
import domen.TipUsluge;
import domen.ZahtevZaAnalizu;
import java.io.IOException;

import java.net.Socket;
import java.net.SocketException;

import java.util.logging.Level;
import java.util.logging.Logger;
import operacije.PrijaviRadnika;
import operacije.VratiListuSviKupac;
import operacije.VratiListuSviRadnik;

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
        Controller c = new Controller();
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
                        serverResponse = c.KreirajZahtevZaAnalizu((ZahtevZaAnalizu) argumentObj);
                        break;
 
                    case PROMENI_ZAHTEV_ZA_ANALIZU:
                        serverResponse = c.PromeniZahtevZaAnalizu((ZahtevZaAnalizu) argumentObj);
                        break;
 
                    case OBRISI_ZAHTEV_ZA_ANALIZU:
                        //serverResponse = c.ObrisiZahtevZaAnalizu((ZahtevZaAnalizu) argumentObj);
                        break;
 
                    case PRETRAZI_ZAHTEV_ZA_ANALIZU:
                        serverResponse = c.PretraziZahtevZaAnalizu((ZahtevZaAnalizu) argumentObj);
                        break;
 
                    case VRATI_LISTU_ZAHTEV_ZA_ANALIZU:
                         serverResponse = c.vratiListuZahtevZaAnalizu((ZahtevZaAnalizu)argumentObj);
                         break;
 
                    // ===== KUPAC =====
                    case KREIRAJ_KUPCA:
                        serverResponse = c.KreirajKupac((Kupac) argumentObj);
                        break;
 
                    case PROMENI_KUPCA:
                        serverResponse = c.PromeniKupac((Kupac) argumentObj);
                        break;
 
                    case OBRISI_KUPCA:
                        serverResponse = c.ObrisiKupac((Kupac) argumentObj);
                        break;
 
                    case PRETRAZI_KUPCA:
                        serverResponse = c.PretraziKupac((Kupac) argumentObj);
                        break;
 
                    case VRATI_LISTU_KUPAC:
                        if (argumentObj instanceof Mesto) {
                            serverResponse = c.vratiListuKupacPoKriterijumuMesto((Mesto) argumentObj);
                        } else {
                            serverResponse = c.vratiListuKupacPoKriterijumuKupac((Kupac) argumentObj);
                        }
                        break;
 
                    case VRATI_LISTU_SVI_KUPAC:
                        serverResponse = c.vratiListuSviKupac();
                        break;
 
                    // ===== RADNIK =====
                    case KREIRAJ_RADNIKA:
                        //serverResponse = c.KreirajRadnik((Radnik) argumentObj);
                        break;
 
                    case PROMENI_RADNIKA:
                        //serverResponse = c.PromeniRadnik((Radnik) argumentObj);
                        break;
 
                    case OBRISI_RADNIKA:
                        //serverResponse = c.ObrisiRadnik((Radnik) argumentObj);
                        break;
 
                    case PRETRAZI_RADNIKA:
                        serverResponse = c.PretraziRadnik((Radnik) argumentObj);
                        break;
 
                    case PRIJAVI_RADNIKA:
                        serverResponse = c.PrijaviRadnik((Radnik) argumentObj);
                        break;
 
                    case VRATI_LISTU_RADNIK:
//                        if (argumentObj instanceof TipUsluge) {
//                            serverResponse = c.vratiListuRadnikPoKriterijumuTipUsluge((TipUsluge) argumentObj);
//                        } else {
//                            serverResponse = c.vratiListuRadnikPoKriterijumuRadnik((Radnik) argumentObj);
//                        }
                        break;
 
                    case VRATI_LISTU_SVI_RADNIK:
                        serverResponse = c.vratiListuSviRadnik();
                        break;
 
                    // ===== PROIZVOD =====
                    case UBACI_PROIZVOD:
                        serverResponse = c.UbaciProizvod((Proizvod) argumentObj);
                        break;
 
                    case PROMENI_PROIZVOD:
                        //serverResponse = c.PromeniProizvod((Proizvod) argumentObj);
                        break;
 
                    case OBRISI_PROIZVOD:
                        //serverResponse = c.ObrisiProizvod((Proizvod) argumentObj);
                        break;
 
                    case PRETRAZI_PROIZVOD:
                        serverResponse = c.PretraziProizvod((Proizvod) argumentObj);
                        break;
 
                    case VRATI_LISTU_PROIZVOD:
                        serverResponse = c.vratiListuProizvodPoKriterijumuProizvod((Proizvod) argumentObj);
                        break;
 
                    case VRATI_LISTU_SVI_PROIZVOD:
                        serverResponse = c.vratiListuSviProizvod();
                        break;
 
                    // ===== MESTO =====
                    case UBACI_MESTO:
                        //serverResponse = c.UbaciMesto((Mesto) argumentObj);
                        break;
 
                    case PROMENI_MESTO:
                        //serverResponse = c.PromeniMesto((Mesto) argumentObj);
                        break;
 
                    case OBRISI_MESTO:
                        //serverResponse = c.ObrisiMesto((Mesto) argumentObj);
                        break;
 
                    case PRETRAZI_MESTO:
                        //serverResponse = c.PretraziMesto((Mesto) argumentObj);
                        break;
 
                    case VRATI_LISTU_MESTO:
                        //serverResponse = c.vratiListuMestoPoKriterijumuMesto((Mesto) argumentObj);
                        break;
 
                    case VRATI_LISTU_SVI_MESTO:
                        //serverResponse = c.vratiListuSviMesto();
                        break;
 
                    // ===== TIP USLUGE =====
                    case UBACI_TIP_USLUGE:
                        //serverResponse = c.UbaciTipUsluge((TipUsluge) argumentObj);
                        break;
 
                    case PROMENI_TIP_USLUGE:
                        //serverResponse = c.PromeniTipUsluge((TipUsluge) argumentObj);
                        break;
 
                    case OBRISI_TIP_USLUGE:
                        //serverResponse = c.ObrisiTipUsluge((TipUsluge) argumentObj);
                        break;
 
                    case PRETRAZI_TIP_USLUGE:
                        //serverResponse = c.PretraziTipUsluge((TipUsluge) argumentObj);
                        break;
 
                    case VRATI_LISTU_TIP_USLUGE:
                        //serverResponse = c.vratiListuTipUslugePoKriterijumuTipUsluge((TipUsluge) argumentObj);
                        break;
 
                    case VRATI_LISTU_SVI_TIP_USLUGE:
                        //serverResponse = c.vratiListuSviTipUsluge();
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
