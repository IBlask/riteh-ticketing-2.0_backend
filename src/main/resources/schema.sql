CREATE TABLE users(
  user_id varchar(64) not null,
  firstname varchar(64) not null,
  lastname varchar(64) not null,
  password varchar(255) not null,
  PRIMARY KEY (user_id)
);