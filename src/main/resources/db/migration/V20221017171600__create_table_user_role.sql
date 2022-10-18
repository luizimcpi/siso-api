CREATE TABLE public.user_role
(
  id BIGSERIAL NOT NULL,
  user_id BIGSERIAL NOT NULL,
  role_id SERIAL NOT NULL,
  PRIMARY KEY(id, user_id, role_id)
);

INSERT INTO public.user_role
("user_id", "role_id")
VALUES(1, 1);
