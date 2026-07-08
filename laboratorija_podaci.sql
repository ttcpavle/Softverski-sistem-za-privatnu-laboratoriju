/*
Skripta za popunjavanje baze `laboratorija` test podacima
Redosled ubacivanja postovan zbog FK ogranicenja:
mesto -> radnik -> kupac -> proizvod -> tipusluge -> zahtevzaanalizu -> stavkazahteva -> ovlascenje
*/

USE `laboratorija`;

/* ===== MESTO ===== */
INSERT INTO `mesto` (`idMesto`, `zipKod`, `naziv`) VALUES
(1, 11000, 'Beograd'),
(2, 21000, 'Novi Sad'),
(3, 18000, 'Nis'),
(4, 34000, 'Kragujevac');

/* ===== RADNIK ===== */
/* prosta korisnicka imena i lozinke radi lakseg testiranja prijave */
INSERT INTO `radnik` (`idRadnik`, `JMBG`, `ime`, `prezime`, `korisnickoIme`, `lozinka`) VALUES
(1, '0101990710001', 'Marko', 'Markovic', 'marko', '123'),
(2, '0202985710002', 'Ana', 'Anic', 'ana', '123'),
(3, '0303980710003', 'Petar', 'Petrovic', 'petar', '123');

/* ===== KUPAC ===== */
INSERT INTO `kupac` (`idKupac`, `ime`, `prezime`, `mail`, `telefon`, `datumRodjenja`, `idMesto`) VALUES
(1, 'Jovan', 'Jovanovic', 'jovan@mail.com', '0641234567', '1990-05-10', 1),
(2, 'Milica', 'Milic', 'milica@mail.com', '0652345678', '1992-08-21', 2),
(3, 'Stefan', 'Stefanovic', 'stefan@mail.com', '0663456789', '1985-01-15', 3),
(4, 'Jelena', 'Jelic', 'jelena@mail.com', '0674567890', '1995-11-30', 4);

/* ===== PROIZVOD ===== */
/* vremeIzdavanjaRez je u tabeli int, a u domenu se cita kao TIME - ostavljam vrednosti u sekundama */
INSERT INTO `proizvod` (`idProizvod`, `naziv`, `vremeIzdavanjaRez`, `cena`) VALUES
(1, 'Analiza krvi', 3600, 1500.00),
(2, 'Analiza urina', 1800, 800.00),
(3, 'Hormonski status', 7200, 3500.00),
(4, 'Lipidni status', 5400, 2200.00);

/* ===== TIPUSLUGE ===== */
INSERT INTO `tipusluge` (`idTipUsluge`, `nazivUsluge`, `opisUsluge`) VALUES
(1, 'Administrator', 'Puna ovlascenja nad sistemom'),
(2, 'Radnik', 'Osnovna ovlascenja za rad sa zahtevima');

/* ===== ZAHTEVZAANALIZU ===== */
INSERT INTO `zahtevzaanalizu` (`idZahtev`, `datum`, `status`, `prioritet`, `ukupnaCenaZahteva`, `idRadnik`, `idKupac`) VALUES
(1, '2025-01-10', 'ZAVRSEN', 0, 2300.00, 1, 1),
(2, '2025-02-15', 'U_OBRADI', 1, 1500.00, 2, 2),
(3, '2025-03-05', 'NOV', 0, 3500.00, 1, 3),
(4, '2025-03-20', 'NOV', 1, 3000.00, 3, 4);

/* ===== STAVKAZAHTEVA ===== */
INSERT INTO `stavkazahteva` (`idZahtev`, `rbStavka`, `kolicina`, `jedinicnaCena`, `ukupnaCena`, `idProizvod`) VALUES
(1, 1, 1, 1500.00, 1500.00, 1),
(1, 2, 1, 800.00, 800.00, 2),
(2, 1, 1, 1500.00, 1500.00, 1),
(3, 1, 1, 3500.00, 3500.00, 3),
(4, 1, 1, 800.00, 800.00, 2),
(4, 2, 1, 2200.00, 2200.00, 4);

/* ===== OVLASCENJE ===== */
INSERT INTO `ovlascenje` (`idRadnik`, `idTipUsluge`, `datumOd`, `datumDo`) VALUES
(1, 1, '2024-01-01', '2026-01-01'),
(2, 2, '2024-01-01', '2026-01-01'),
(3, 2, '2024-06-01', '2026-06-01');
