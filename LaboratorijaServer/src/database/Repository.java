/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package database;

import domen.OpstiDomenskiObjekat;

/**
 *
 * @author totic
 */
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
   
}
