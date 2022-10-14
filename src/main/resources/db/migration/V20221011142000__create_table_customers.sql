CREATE TABLE public.customers
(
  id BIGSERIAL PRIMARY KEY,
  user_id BIGSERIAL REFERENCES public.users (id) NOT NULL,
  name character varying(500) NOT NULL,
  email character varying(200) NOT NULL,
  document character varying(100),
  birth_date DATE NOT NULL DEFAULT CURRENT_DATE,
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL
);
