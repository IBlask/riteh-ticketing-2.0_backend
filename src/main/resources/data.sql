INSERT INTO Institucija(id, name, address, contact_email) VALUES (1, 'Tehnički fakultet u Rijeci', 'Vukovarska 58, Rijeka', 'rwt.ticketing@gmail.com');

INSERT INTO Sluzba (id, name, institucija_id) VALUES (1, 'Računalni centar', 1);
INSERT INTO Sluzba (id, name, institucija_id) VALUES (2, 'Tehnička služba', 1);

INSERT INTO Kategorija(id, name, sluzba_id, parent_id, active) VALUES (1, 'Kvar računala', 1, null, 1);
INSERT INTO Kategorija(id, name, sluzba_id, parent_id, active) VALUES (2, 'Kvar računalne opreme', 1, null, 1);
INSERT INTO Kategorija(id, name, sluzba_id, parent_id, active) VALUES (3, 'Tehnički kvar', 2, null, 1);

INSERT INTO Prostorija(oznaka, institucija_id) VALUES ('I5', 1);
INSERT INTO Prostorija(oznaka, institucija_id) VALUES ('I8', 1);
INSERT INTO Prostorija(oznaka, institucija_id) VALUES ('P1', 1);
INSERT INTO Prostorija(oznaka, institucija_id) VALUES ('P2', 1);


-- *****KORISNICI*****
-- sve lozinke su "pass"

-- korisnik
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('user1@riteh.hr', 'Marko', 'Markić', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('user2@riteh.hr', 'Maja', 'Majić', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- agenti
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('ag_rc1@riteh.hr', 'Ivan', 'Ivanović',  1, 'riteh.ticketing@gmail.com','$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('ag_rc2@riteh.hr', 'Ivo', 'Ivić',  1, 'riteh.ticketing@gmail.com','$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('ag_ts1@riteh.hr', 'Matej', 'Matejčić',  1, 'riteh.ticketing@gmail.com','$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('ag_ts2@riteh.hr', 'Ana', 'Anić',  1, 'riteh.ticketing@gmail.com','$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- voditelji
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('vd_rc@riteh.hr', 'Marta', 'Martić', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('vd_ts@riteh.hr', 'Pero', 'Perić', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');

-- super-voditelj
INSERT INTO users(user_id, firstName, lastName, institucija_id, email, password) VALUES ('svd@riteh.hr', 'Sara', 'Sarić', 1, 'riteh.ticketing@gmail.com', '$2a$10$lzPispYhwRVTmZz0n5Gfeu5P1/j/sB5OauKbIrT.K/eSu1lmQ4aJe');



-- *****SUPER-VODITELJ - INSTITUCIJA*****
INSERT INTO Super_voditelj(user_id, institucija_id, active) VALUES ('svd@riteh.hr', 1, 1);

-- *****VODITELJI*****
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('vd_rc@riteh.hr', 1, 'v', 1);
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('vd_ts@riteh.hr', 2, 'v', 1);

-- *****AGENTI*****
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('ag_rc1@riteh.hr', 1, 'a', 1);
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('ag_rc2@riteh.hr', 1, 'a', 1);
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('ag_ts1@riteh.hr', 1, 'a', 1);
INSERT INTO Zaposlenik_sluzbe(user_id, sluzba_id, role, active) VALUES ('ag_ts2@riteh.hr', 1, 'a', 1);