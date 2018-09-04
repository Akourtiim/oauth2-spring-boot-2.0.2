drop table if exists product;
CREATE TABLE product (
  id integer,
  version integer,
  available boolean,
  name varchar(255) not null
);


