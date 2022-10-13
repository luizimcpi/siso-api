CREATE TABLE public.roles
(
  id SERIAL PRIMARY KEY,
  name character varying(100)
);

INSERT INTO public.roles
("name")
VALUES('ROLE_ADMIN');

INSERT INTO public.roles
("name")
VALUES('ROLE_USER');