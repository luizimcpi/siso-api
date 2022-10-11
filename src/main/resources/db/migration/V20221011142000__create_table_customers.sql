CREATE TABLE public.customers
(
  id uuid NOT NULL PRIMARY KEY,
  user_id uuid REFERENCES public.users (id) NOT NULL,
  name character varying(500),
  email character varying(200),
  document character varying(100),
  birth_date DATE NOT NULL DEFAULT CURRENT_DATE,
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL
);
