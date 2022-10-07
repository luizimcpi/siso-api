CREATE TABLE public.users
(
  id uuid NOT NULL PRIMARY KEY,
  email character varying(200),
  password character varying(1000),
  active boolean NOT NULL,
  password_reset boolean NOT NULL,
  role character varying(200),
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL
);

INSERT INTO public.users
(id, email, "password", active, password_reset, role, created_at, updated_at)
VALUES('75d7b8f7-549e-490f-a55d-985380f02ffe'::uuid, 'luizimcpi@gmail.com', '$2a$10$YaeJpu.AFRQZ4pVvJt63FuGnBP/oSfkkBuCPSbQnihUGvSgh49pwe', true, false, 'bb927857-e7d5-4422-8572-e4e53195203e', now(), now());
