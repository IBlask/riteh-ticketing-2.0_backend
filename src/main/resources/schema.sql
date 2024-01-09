CREATE TABLE IF NOT EXISTS Institucija (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    contact_email varchar(64) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Users (
    user_id varchar(64) NOT NULL,
    firstname varchar(64) NOT NULL,
    lastname varchar(64) NOT NULL,
    institucija_id int NOT NULL,
    email varchar(64) NOT NULL,
    password varchar(255) NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (institucija_id) REFERENCES Institucija(id)
);

CREATE TABLE IF NOT EXISTS Super_voditelj (
    user_id varchar(64) NOT NULL,
    institucija_id int NOT NULL,
    active boolean NOT NULL,
    PRIMARY KEY (user_id, institucija_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (institucija_id) REFERENCES Institucija(id)
);



CREATE TABLE IF NOT EXISTS Sluzba (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    institucija_id int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (institucija_id) REFERENCES Institucija(id)
);

CREATE TABLE IF NOT EXISTS Zaposlenik_sluzbe (
    user_id varchar(64) NOT NULL,
    sluzba_id int NOT NULL,
    role char(1) NOT NULL,
    active boolean NOT NULL,
    PRIMARY KEY (user_id, sluzba_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (sluzba_id) REFERENCES Sluzba(id)
);



CREATE TABLE IF NOT EXISTS Prostorija (
    oznaka varchar(8) NOT NULL,
    institucija_id int NOT NULL,
    PRIMARY KEY (oznaka, institucija_id),
    FOREIGN KEY (institucija_id) REFERENCES Institucija(id)
);

CREATE TABLE IF NOT EXISTS Kategorija (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    sluzba_id int NOT NULL,
    parent_id int,
    active boolean NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sluzba_id) REFERENCES Sluzba(id),
    FOREIGN KEY (parent_id) REFERENCES Kategorija(id)
);



CREATE TABLE IF NOT EXISTS Ticket (
    id long NOT NULL AUTO_INCREMENT,
    parent_id long,
    title varchar(80) NOT NULL,
    description varchar(255),
    sluzba_id int NOT NULL,
    voditelj_user_id varchar(64) NOT NULL,
    prostorija varchar(8) NOT NULL,
    institucija_id int NOT NULL,
    prijavitelj_user_id varchar(64) NOT NULL,
    stvarni_prijavitelj_user_id varchar(64),
    kategorija_id int NOT NULL,
    created_at timestamp NOT NULL,
    status varchar(64) NOT NULL,
    priority tinyint,
    deadline date,
    est_fix_time time,
    real_fix_time time,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES Ticket(id),
    FOREIGN KEY (sluzba_id) REFERENCES Sluzba(id),
    FOREIGN KEY (voditelj_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (prostorija, institucija_id) REFERENCES Prostorija(oznaka, institucija_id),
    FOREIGN KEY (prijavitelj_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (stvarni_prijavitelj_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (kategorija_id) REFERENCES Kategorija(id)
);

CREATE TABLE IF NOT EXISTS Agent_Ticket (
    agent_user_id varchar(64) NOT NULL,
    ticket_id long NOT NULL,
    PRIMARY KEY (agent_user_id, ticket_id),
    FOREIGN KEY (agent_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (ticket_id) REFERENCES Ticket(id)
);