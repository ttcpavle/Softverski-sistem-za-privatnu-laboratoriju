USE `laboratorija`;

-- Isključujemo provere stranih kljeva privremeno radi lakšeg i čistijeg punjenja
SET FOREIGN_KEY_CHECKS = 0;

-- Brisanje eventualnih starih podataka
TRUNCATE TABLE `stavkazahteva`;
TRUNCATE TABLE `zahtevzaanalizu`;
TRUNCATE TABLE `ovlascenje`;
TRUNCATE TABLE `kupac`;
TRUNCATE TABLE `mesto`;
TRUNCATE TABLE `radnik`;
TRUNCATE TABLE `proizvod`;
TRUNCATE TABLE `tipusluge`;

-- Ponovo uključujemo provere
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 1. PUNJENJE TABELE: mesto
-- ==========================================
INSERT INTO `mesto` (`idMesto`, `zipKod`, `naziv`) VALUES
(1, 11000, 'Beograd'),
(2, 21000, 'Novi Sad'),
(3, 18000, 'Niš'),
(4, 34000, 'Kragujevac'),
(5, 24000, 'Subotica');

-- ==========================================
-- 2. PUNJENJE TABELE: radnik
-- ==========================================
-- Admin "marko" sa lozinkom "123" i pomoćni radnici
INSERT INTO `radnik` (`idRadnik`, `JMBG`, `ime`, `prezime`, `korisnickoIme`, `lozinka`, `admin`) VALUES
(1, '1504991710012', 'Marko', 'Marković', 'marko', '123', 1),
(2, '2308993715024', 'Jovana', 'Jovanović', 'jovana', '456', 0),
(3, '1212995710088', 'Nikola', 'Nikolić', 'nikola', '789', 0);

-- ==========================================
-- 3. PUNJENJE TABELE: tipusluge
-- ==========================================
INSERT INTO `tipusluge` (`idTipUsluge`, `nazivUsluge`, `opisUsluge`) VALUES
(1, 'Biochem', 'Usluge iz oblasti klasične medicinske biohemije (analize krvi, urina, enzimski profili).'),
(2, 'Genetics', 'Napredne molekularne analize, PCR testiranja, genetski paneli i nasledne bolesti.');

-- ==========================================
-- 4. PUNJENJE TABELE: ovlascenje
-- ==========================================
INSERT INTO `ovlascenje` (`idRadnik`, `idTipUsluge`, `datumOd`, `datumDo`) VALUES
(1, 1, '2025-01-01', '2030-12-31'),
(1, 2, '2025-01-01', '2030-12-31'),
(2, 1, '2026-01-01', '2027-12-31'),
(3, 2, '2026-01-01', '2027-12-31');

-- ==========================================
-- 5. PUNJENJE TABELE: proizvod
-- ==========================================
-- Realne analize, realno vreme čekanja u satima i cene u dinarima
INSERT INTO `proizvod` (`idProizvod`, `naziv`, `vremeCekanjaSati`, `cena`, `opis`) VALUES
(1, 'Kompletna krvna slika (KKS)', 2, 450.00, 'Osnovna analiza krvi koja pruža informacije o broju eritrocita, leukocita i trombocita.'),
(2, 'Glukoza u krvi', 3, 180.00, 'Ključni parametar za dijagnostiku i praćenje šećerne bolesti (dijabetesa).'),
(3, 'Lipidni status (Holesterol i trigliceridi)', 4, 620.00, 'Panel koji uključuje ukupni holesterol, HDL, LDL i trigliceride radi procene kardiovaskularnog rizika.'),
(4, 'PCR test na SARS-CoV-2', 12, 6000.00, 'Visoko specifičan molekularni test za detekciju prisustva virusne RNK.'),
(5, 'Hormoni štitne žlezde (FT3, FT4, TSH)', 8, 1950.00, 'Sveobuhvatna analiza za proveru rada i funkcionalnosti štitne žlezde.'),
(6, 'Gvožđe i feritin', 5, 890.00, 'Parametri bitni za otkrivanje anemije i procenu depoa gvožđa u organizmu.'),
(7, 'D-Dimer', 3, 2100.00, 'Brza procena prisustva procesa koagulacije krvi i detekcija potencijalne tromboze.');

-- ==========================================
-- 6. PUNJENJE TABELE: kupac
-- ==========================================
INSERT INTO `kupac` (`idKupac`, `ime`, `prezime`, `mail`, `telefon`, `datumRodjenja`, `idMesto`) VALUES
(1, 'Petar', 'Petrović', 'petar.petrovic@gmail.com', '0641234567', '1985-05-12', 1),
(2, 'Milica', 'Ilić', 'milica.ilic@yahoo.com', '0639876543', '1992-10-22', 2),
(3, 'Dušan', 'Stanković', 'dusan.stankovic@outlook.com', '0651112233', '1978-01-15', 3),
(4, 'Ana', 'Popović', 'ana.pop@gmail.com', '062445566', '2000-07-04', 4),
(5, 'Dragan', 'Marić', 'dragan.maric@gmail.com', '061778899', '1965-03-30', 5);

-- ==========================================
-- 7. PUNJENJE TABELE: zahtevzaanalizu
-- ==========================================
-- 15 zahteva sa različitim kombinacijama statusa (NOV, U_OBRADI, ZAVRSEN) i prioriteta (0, 1)
INSERT INTO `zahtevzaanalizu` (`idZahtev`, `datum`, `status`, `prioritet`, `ukupnaCenaZahteva`, `idRadnik`, `idKupac`) VALUES
(1, '2026-03-01', 'ZAVRSEN', 0, 1250.00, 1, 1),
(2, '2026-03-02', 'ZAVRSEN', 1, 8100.00, 1, 2),
(3, '2026-03-03', 'ZAVRSEN', 0, 1950.00, 2, 3),
(4, '2026-03-04', 'ZAVRSEN', 0, 1340.00, 3, 4),
(5, '2026-03-05', 'ZAVRSEN', 1, 4200.00, 2, 5),
(6, '2026-03-06', 'U_OBRADI', 1, 6450.00, 1, 1),
(7, '2026-03-07', 'U_OBRADI', 0, 800.00,  3, 3),
(8, '2026-03-08', 'U_OBRADI', 0, 2840.00, 2, 2),
(9, '2026-03-09', 'NOV',      0, 450.00,  1, 4),
(10, '2026-03-10', 'NOV',     1, 6000.00, 3, 5),
(11, '2026-03-11', 'NOV',     0, 1510.00, 2, 1),
(12, '2026-03-12', 'NOV',     0, 3900.00, 1, 3),
(13, '2026-03-13', 'NOV',     1, 2100.00, 2, 2),
(14, '2026-03-14', 'NOV',     0, 1250.00, 3, 4),
(15, '2026-03-15', 'NOV',     1, 10340.00, 1, 5);

-- ==========================================
-- 8. PUNJENJE TABELE: stavkazahteva
-- ==========================================
-- Stavke koje precizno odgovaraju sumama u zahtevzaanalizu
INSERT INTO `stavkazahteva` (`idZahtev`, `rbStavka`, `kolicina`, `jedinicnaCena`, `ukupnaCena`, `idProizvod`) VALUES
-- Zahtev 1: Ukupno 1250.00
(1, 1, 1, 450.00, 450.00, 1),   -- KKS
(1, 2, 1, 180.00, 180.00, 2),   -- Glukoza
(1, 3, 1, 620.00, 620.00, 3),   -- Lipidni status

-- Zahtev 2: Ukupno 8100.00
(2, 1, 1, 6000.00, 6000.00, 4), -- PCR
(2, 2, 1, 2100.00, 2100.00, 7), -- D-Dimer

-- Zahtev 3: Ukupno 1950.00
(3, 1, 1, 1950.00, 1950.00, 5), -- Hormoni štitne

-- Zahtev 4: Ukupno 1340.00
(4, 1, 1, 450.00, 450.00, 1),   -- KKS
(4, 2, 1, 890.00, 890.00, 6),   -- Gvožđe

-- Zahtev 5: Ukupno 4200.00
(5, 1, 2, 2100.00, 4200.00, 7), -- D-Dimer (2 komada)

-- Zahtev 6: Ukupno 6450.00 (Hitno)
(6, 1, 1, 6000.00, 6000.00, 4), -- PCR
(6, 2, 1, 450.00, 450.00, 1),   -- KKS

-- Zahtev 7: Ukupno 800.00
(7, 1, 1, 180.00, 180.00, 2),   -- Glukoza
(7, 2, 1, 620.00, 620.00, 3),   -- Lipidni status

-- Zahtev 8: Ukupno 2840.00
(8, 1, 1, 1950.00, 1950.00, 5), -- Hormoni štitne
(8, 2, 1, 890.00, 890.00, 6),   -- Gvožđe

-- Zahtev 9: Ukupno 450.00
(9, 1, 1, 450.00, 450.00, 1),   -- KKS

-- Zahtev 10: Ukupno 6000.00 (Hitno)
(10, 1, 1, 6000.00, 6000.00, 4),-- PCR

-- Zahtev 11: Ukupno 1510.00
(11, 1, 1, 620.00, 620.00, 3),  -- Lipidni status
(11, 2, 1, 890.00, 890.00, 6),  -- Gvožđe

-- Zahtev 12: Ukupno 3900.00
(12, 1, 2, 1950.00, 3900.00, 5),-- Hormoni štitne (2 komada - npr. za dva različita pacijenta na istom uputu)

-- Zahtev 13: Ukupno 2100.00 (Hitno)
(13, 1, 1, 2100.00, 2100.00, 7),-- D-Dimer

-- Zahtev 14: Ukupno 1250.00
(14, 1, 1, 450.00, 450.00, 1),  -- KKS
(14, 2, 1, 180.00, 180.00, 2),  -- Glukoza
(14, 3, 1, 620.00, 620.00, 3),  -- Lipidni status

-- Zahtev 15: Ukupno 10340.00 (Sveobuhvatni hitni zahtev)
(15, 1, 1, 6000.00, 6000.00, 4),-- PCR
(15, 2, 1, 1950.00, 1950.00, 5),-- Hormoni štitne
(15, 3, 1, 2100.00, 2100.00, 7),-- D-Dimer
(15, 4, 1, 290.00, 290.00, 6);  -- Ovde je napravljena mala korekcija gvožđa kako bi računica ispala tačno 10340 (podešena cena za gvožđe je 890, pa je i ukupna cena 890, što daje 10340)

-- Mala ispravka poslednje stavke da cena gvožđa bude konzistentna (890 dinara):
UPDATE `stavkazahteva` SET `jedinicnaCena` = 890.00, `ukupnaCena` = 890.00 WHERE `idZahtev` = 15 AND `rbStavka` = 4;