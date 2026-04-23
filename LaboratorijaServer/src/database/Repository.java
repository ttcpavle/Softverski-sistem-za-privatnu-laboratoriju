package database;

import domen.OpstiDomenskiObjekat;

public interface Repository {
    public boolean zatvoriBazu();
    public boolean commitTransakcije();
    public boolean rollbackTransakcije();

    public boolean pamtiSlog(OpstiDomenskiObjekat odo);

    /**
     * @param objekat sa postavljenim atributima po kojima se vrsi pretraga. U rezultat se upisuje pronadjeni slog
     */
    public boolean nadjiSlog(OpstiDomenskiObjekat odo);

    /**
     * @param objekat sa postavljenim atributima po kojima se vrsi pretraga. U rezultat se upisuju pronadjeni slogovi.
     */
    public boolean vratiSve(OpstiDomenskiObjekat odo);

    /**
     * Genericki select upit
     * @param  odo objekat na osnovu cijeg tipa se kreira lista tog istog tipa. Lista se cuva u atributu rezultat
     * @param tabela1 prva tabela za spajanje
     * @param joinType tip joina: "LEFT", "RIGHT"...
     * @param tabela2 druga tabela za spajanje
     * @param joinCondition se unosi unutar ON(..) klauzule
     * @param whereClause uslov na osnovu kog se filtrira tabela koja je nastala kao rezultat spajanja
     * @return uspesnost operacije
     */
    public boolean vratiSvePremaUslovu(OpstiDomenskiObjekat odo,
            String tabela1,
            String joinType,
            String tabela2,
            String joinCondition,
            String whereClause,
            String kolone);

    public boolean promeniSlog(OpstiDomenskiObjekat odo);

    /**
     * Brise slog iz tabele na osnovu primarnog kljuca objekta.
     * Za kompozitni kljuc vratiNazivKolonePK() vraca "kol1, kol2"
     * a vratiVrednostPK() vraca "val1, val2" — DBBroker ih razlaže u WHERE klauzulu.
     * @param odo objekat sa postavljenim PK atributima
     * @return uspesnost operacije
     */
    public boolean obrisiSlog(OpstiDomenskiObjekat odo);

    /**
     * Brise sve slogove iz tabele koji zadovoljavaju uslov vratiUslovZaNadjiSlog().
     * Korisno za brisanje svih stavki zahteva pre ponovnog umetanja.
     * @param odo objekat sa postavljenim atributima koji definisu WHERE uslov
     * @return uspesnost operacije
     */
    public boolean obrisiSvePremaUslovu(OpstiDomenskiObjekat odo);
}