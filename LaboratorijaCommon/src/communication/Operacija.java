/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package communication;

/**
 *
 * @author totic
 */

/*
SK1 kreiraj zahtev za analizu
SK2 pretrazi zahtev za analizu
SK3 promeni zahtev za analizu
SK5 kreiraj kupca
SK6 pretrazi kupca
SK7 promeni kupca
SK8 obrisi kupca
SK9 prijavi radnika
SK14 ubaci proizvod
SK15 pretrazi proizvod
*/
public enum Operacija {
    // Operacije za Zahtev za analizu
    KREIRAJ_ZAHTEV_ZA_ANALIZU,
    PROMENI_ZAHTEV_ZA_ANALIZU,
    OBRISI_ZAHTEV_ZA_ANALIZU,
    PRETRAZI_ZAHTEV_ZA_ANALIZU,
    VRATI_LISTU_ZAHTEV_ZA_ANALIZU,
    
    // Operacije za Kupca
    KREIRAJ_KUPCA,
    PROMENI_KUPCA,
    OBRISI_KUPCA,
    PRETRAZI_KUPCA,
    VRATI_LISTU_KUPAC,
    VRATI_LISTU_SVI_KUPAC,
    
    // Operacije za Radnika
    KREIRAJ_RADNIKA,
    PROMENI_RADNIKA,
    OBRISI_RADNIKA,
    PRETRAZI_RADNIKA,
    PRIJAVI_RADNIKA,
    VRATI_LISTU_RADNIK,
    VRATI_LISTU_SVI_RADNIK,
    
    // Operacije za Proizvod
    UBACI_PROIZVOD,
    PROMENI_PROIZVOD,
    OBRISI_PROIZVOD,
    PRETRAZI_PROIZVOD,
    VRATI_LISTU_PROIZVOD,
    VRATI_LISTU_SVI_PROIZVOD,
    
    // Operacije za Mesto
    UBACI_MESTO,
    PROMENI_MESTO,
    OBRISI_MESTO,
    PRETRAZI_MESTO,
    VRATI_LISTU_MESTO,
    VRATI_LISTU_SVI_MESTO,
    
    // Operacije za Tip usluge
    UBACI_TIP_USLUGE,
    PROMENI_TIP_USLUGE,
    OBRISI_TIP_USLUGE,
    PRETRAZI_TIP_USLUGE,
    VRATI_LISTU_TIP_USLUGE,
    VRATI_LISTU_SVI_TIP_USLUGE
}
