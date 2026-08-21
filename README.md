# Sistem za privatnu laboratoriju

Ovaj projekat predstavlja softversku implementaciju poslovnog sistema za (neku) privatnu laboratoriju. Model je jednostavan i lak za razumevanje i ispunjava neke bitne poslovne zahteve ovakvih sistema.

U pitanju je klijent-server java aplikacija koju čine klijentska aplikacija, serverska aplikacija i biblioteke koje sadrži klase neophodne za rad serverskog i klijentskog programa.

Način korišćenja:
1) pokrenuti mySql bazu podataka i u njoj izvršiti skriptu za postavljanje strukture baze (DDL)
2) pokrenuti skriptu za popunjavanje baze sa sample podacima (DML)
3) buildovati common program, serverski i klijentski program
4) konfigurisati config.properties fajl u serveru tako da se pravilno pristupa bazi podataka. Podesiti i ostala podešavanja
5) pokrenuti serverski program. Ukoliko je pokrenut UI, kliknuti na dugme "pokreni"
6) pokrenuti klijentski program i ulogovati se sa nekim od postojećih korisnika u bazi (neki korisnici su admin a neki nisu pa shodno tome imaju drugačiji UI)

Karakteristike:
- U config.properties podesiti podatke za konekciju sa bazom podataka i za maksimalan podržan broj konekcija nad bazom, kao i serverski port
- Običan korisnik može manipulisati zahtevima za analizu i kupcima. Administrator korisnik se mora ručno dodati u bazu podataka i on ima pristup ostalim funkcionalnostima (npr dodavanje proizvoda, tipa usluge, ovlašćenja i ostalih radnika)
- Nakon pokretanja, server će pokušati da uspostavi konekciju sa bazom u 3 pokušaja nakon čega se zaustavlja
- Ukoliko se za vreme rada servera izgubi konekcija sa bazom, server će na svakih 10 sekundi pokušati da obnovi konekciju sa bazom (ili dok se ne zaustavi)
- Server loguje sve bitne radnje
- Klijentski program je jednostavan i lak za korišćenje
- Ovaj projekat koristi java Swing i AWT, Logger, Socket i MySqlConnector.

