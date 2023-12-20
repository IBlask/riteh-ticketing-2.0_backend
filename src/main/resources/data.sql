INSERT INTO Institucija(id, name, address, contact_email) VALUES (1, 'Tehnički fakultet u Rijeci', 'Vukovarska 58, Rijeka', 'rwt.ticketing@gmail.com');

INSERT INTO Sluzba (id, name, institucija_id) VALUES (1, '_D__Računalni centar', 1);
INSERT INTO Sluzba (id, name, institucija_id) VALUES (2, '_D__Tehnička služba', 1);

INSERT INTO Kategorija(id, name, sluzba_id, parent_id, active) VALUES (1, '_RC_Kvar PC-a__n/a', 1, null, 0);
INSERT INTO Kategorija(id, name, sluzba_id, parent_id, active) VALUES (2, '_RC_Kvar PC-a', 1, null, 1);
INSERT INTO Kategorija(id, name, sluzba_id, parent_id, active) VALUES (3, '_TS_Kvar projektora', 2, null, 1);

INSERT INTO Prostorija(oznaka, institucija_id) VALUES ('I5', 1);
INSERT INTO Prostorija(oznaka, institucija_id) VALUES ('I8', 1);


-- *****KORISNICI*****
-- sve lozinke su "pass"

-- student
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('st@riteh.hr', '_S_ime', '_S_prezime', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- djelatnik
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('dj@riteh.hr', '_D_ime', '_D_prezime', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- agent
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('ag@riteh.hr', '_A_ime', '_A_prezime',  1, 'riteh.ticketing@gmail.com','$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- voditelj
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('vd@riteh.hr', '_V_ime', '_V_prezime', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- super-voditelj
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('svd@riteh.hr', '_SV_ime', '_SV_prezime', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');



-- *****SUPER-VODITELJ - INSTITUCIJA*****
INSERT INTO Super_voditelj(user_id, institucija_id, active) VALUES ('svd@riteh.hr', 1, 1);

-- *****VODITELJ*****
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('vd@riteh.hr', 1, 'v', 1);

-- *****AGENT*****
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('ag@riteh.hr', 1, 'a', 1);