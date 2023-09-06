DROP TABLE IF EXISTS url;

create table url (
  id INT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at timestamp not null
);

DROP TABLE IF EXISTS url_check;

create table url_check (
  id INT PRIMARY KEY,
  status_code INT NOT NULL,
  title varchar(255),
  h1 varchar(255),
  description CLOB,
  url_id INT,
  created_at timestamp not null
);