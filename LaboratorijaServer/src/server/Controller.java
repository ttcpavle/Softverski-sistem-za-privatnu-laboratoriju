package server;

import communication.Response;
import domen.Kupac;
import domen.Mesto;
import domen.Proizvod;
import domen.Radnik;
import domen.TipUsluge;
import domen.ZahtevZaAnalizu;
import operacije.KreirajKupac;
import operacije.KreirajZahtevZaAnalizu;
import operacije.PromeniKupac;
import operacije.PromeniProizvod;
import operacije.PromeniZahtevZaAnalizu;
import operacije.PretraziKupac;
import operacije.PretraziProizvod;
import operacije.PretraziRadnik;
import operacije.PretraziZahtevZaAnalizu;
import operacije.PrijaviRadnika;
import operacije.UbaciProizvod;
import operacije.VratiListuKupac;
import operacije.VratiListuProizvodPoKriterijumuProizvod;
import operacije.VratiListuSviKupac;
import operacije.VratiListuSviMesto;
import operacije.VratiListuSviProizvod;
import operacije.VratiListuSviRadnik;
import operacije.VratiListuZahtevZaAnalizu;

public class Controller {

// ========== ZAHTEV ZA ANALIZU ==========
    public Response KreirajZahtevZaAnalizu(ZahtevZaAnalizu zahtev) {
        KreirajZahtevZaAnalizu so = new KreirajZahtevZaAnalizu();
        return so.opsteIzvrsenjeSO(zahtev);
    }

    public Response PromeniZahtevZaAnalizu(ZahtevZaAnalizu zahtev) {
        PromeniZahtevZaAnalizu so = new PromeniZahtevZaAnalizu();
        return so.opsteIzvrsenjeSO(zahtev);
    }

    public Response ObrisiZahtevZaAnalizu(ZahtevZaAnalizu zahtev) {
        // TODO: implementirati ObrisiZahtevZaAnalizu SO
        return null;
    }

    public Response PretraziZahtevZaAnalizu(ZahtevZaAnalizu zahtev) {
        PretraziZahtevZaAnalizu so = new PretraziZahtevZaAnalizu();
        return so.opsteIzvrsenjeSO(zahtev);
    }
    
    public Response vratiListuZahtevZaAnalizu(ZahtevZaAnalizu kriterijum) {
        VratiListuZahtevZaAnalizu so = new VratiListuZahtevZaAnalizu();
        return so.opsteIzvrsenjeSO(kriterijum);
    }
    

// ========== KUPAC ==========
    public Response KreirajKupac(Kupac kupac) {
        KreirajKupac so = new KreirajKupac();
        return so.opsteIzvrsenjeSO(kupac);
    }

    public Response PromeniKupac(Kupac kupac) {
        PromeniKupac so = new PromeniKupac();
        return so.opsteIzvrsenjeSO(kupac);
    }

    public Response ObrisiKupac(Kupac kupac) {
        // TODO: implementirati ObrisiKupac SO
        return null;
    }

    public Response PretraziKupac(Kupac kupac) {
        PretraziKupac so = new PretraziKupac();
        return so.opsteIzvrsenjeSO(kupac);
    }

    public Response vratiListuKupac(Kupac kriterijum) {
        VratiListuKupac so = new VratiListuKupac();
        return so.opsteIzvrsenjeSO(kriterijum);
    }

    public Response vratiListuSviKupac() {
        VratiListuSviKupac so = new VratiListuSviKupac();
        return so.opsteIzvrsenjeSO(null);
    }

// ========== RADNIK ==========
    public Response KreirajRadnik(Radnik radnik) {
        return null;
    }

    public Response PromeniRadnik(Radnik radnik) {
        return null;
    }

    public Response ObrisiRadnik(Radnik radnik) {
        return null;
    }

    public Response PretraziRadnik(Radnik radnik) {
        PretraziRadnik so = new PretraziRadnik();
        return so.opsteIzvrsenjeSO(radnik);
    }

    public Response PrijaviRadnik(Radnik radnik) {
        PrijaviRadnika pr = new PrijaviRadnika();
        return pr.opsteIzvrsenjeSO(radnik);
    }

    public Response vratiListuRadnikPoKriterijumuRadnik(Radnik kriterijum) {
        return null;
    }

    public Response vratiListuRadnikPoKriterijumuTipUsluge(TipUsluge kriterijum) {
        return null;
    }

    public Response vratiListuSviRadnik() {
        VratiListuSviRadnik so = new VratiListuSviRadnik();
        return so.opsteIzvrsenjeSO(null);
    }

// ========== PROIZVOD ==========
    public Response UbaciProizvod(Proizvod proizvod) {
        UbaciProizvod so = new UbaciProizvod();
        return so.opsteIzvrsenjeSO(proizvod);
    }

    public Response PromeniProizvod(Proizvod proizvod) {
        PromeniProizvod so = new PromeniProizvod();
        return so.opsteIzvrsenjeSO(proizvod);
    }

    public Response ObrisiProizvod(Proizvod proizvod) {
        // TODO: implementirati ObrisiProizvod SO
        return null;
    }

    public Response PretraziProizvod(Proizvod proizvod) {
        PretraziProizvod so = new PretraziProizvod();
        return so.opsteIzvrsenjeSO(proizvod);
    }

    public Response vratiListuProizvodPoKriterijumuProizvod(Proizvod kriterijum) {
        VratiListuProizvodPoKriterijumuProizvod so = new VratiListuProizvodPoKriterijumuProizvod();
        return so.opsteIzvrsenjeSO(kriterijum);
    }

    public Response vratiListuSviProizvod() {
        VratiListuSviProizvod so = new VratiListuSviProizvod();
        return so.opsteIzvrsenjeSO(null);
    }

// ========== MESTO ==========
    public Response UbaciMesto(Mesto mesto) {
        return null;
    }

    public Response PromeniMesto(Mesto mesto) {
        return null;
    }

    public Response ObrisiMesto(Mesto mesto) {
        return null;
    }

    public Response PretraziMesto(Mesto mesto) {
        return null;
    }

    public Response vratiListuMestoPoKriterijumuMesto(Mesto kriterijum) {
        return null;
    }

    public Response vratiListuSviMesto() {
        VratiListuSviMesto so = new VratiListuSviMesto();
        return so.opsteIzvrsenjeSO(null);
    }

// ========== TIP USLUGE ==========
    public Response UbaciTipUsluge(TipUsluge tipUsluge) {
        return null;
    }

    public Response PromeniTipUsluge(TipUsluge tipUsluge) {
        return null;
    }

    public Response ObrisiTipUsluge(TipUsluge tipUsluge) {
        return null;
    }

    public Response PretraziTipUsluge(TipUsluge tipUsluge) {
        return null;
    }

    public Response vratiListuTipUslugePoKriterijumuTipUsluge(TipUsluge kriterijum) {
        return null;
    }

    public Response vratiListuSviTipUsluge() {
        return null;
    }
}