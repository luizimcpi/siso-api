CREATE TABLE public.roles
(
  id uuid NOT NULL PRIMARY KEY,
  name character varying(100)
);

INSERT INTO public.roles
(id, "name")
VALUES('bb927857-e7d5-4422-8572-e4e53195203e'::uuid, 'ROLE_ADMIN');

INSERT INTO public.roles
(id, "name")
VALUES('08be22fd-9480-4b36-9d3b-faf30e0d7fa8'::uuid, 'ROLE_USER');