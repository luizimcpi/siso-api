CREATE TABLE public.refresh_token
(
  id BIGSERIAL PRIMARY KEY,
  username character varying(200),
  refresh_token character varying(1000),
  revoked boolean NOT NULL,
  created_at timestamp without time zone NOT NULL
);