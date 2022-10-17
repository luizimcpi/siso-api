CREATE TABLE public.user_role
(
  user_id BIGSERIAL NOT NULL,
  role_id SERIAL NOT NULL,
  PRIMARY KEY(user_id, role_id)
);

INSERT INTO public.user_role
("user_id", "role_id")
VALUES(1, 1);