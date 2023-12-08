INSERT INTO Institucija(id, name, address, contact_email) VALUES (1, 'Tehnički fakultet u Rijeci', 'Vukovarska 58, Rijeka', 'rwt.ticketing@gmail.com');




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