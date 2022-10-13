CREATE TABLE public.users
(
  id BIGSERIAL PRIMARY KEY,
  email character varying(200),
  password character varying(1000),
  active boolean NOT NULL,
  password_reset boolean NOT NULL,
  role SERIAL NOT NULL,
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL
);

INSERT INTO public.users
(email, "password", active, password_reset, role, created_at, updated_at)
VALUES('luizimcpi@gmail.com', '$2a$10$YaeJpu.AFRQZ4pVvJt63FuGnBP/oSfkkBuCPSbQnihUGvSgh49pwe', true, false, 1, now(), now());
