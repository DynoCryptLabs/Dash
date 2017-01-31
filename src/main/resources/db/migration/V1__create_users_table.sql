CREATE TABLE IF NOT EXISTS "users"
(
  id            SERIAL PRIMARY KEY,
  first_name    VARCHAR(255) NOT NULL,
  last_name     VARCHAR(255) NOT NULL,
  email         VARCHAR(255) NOT NULL,
  password_hash VARCHAR(255),
  access_token  VARCHAR(255),
  is_active     BOOLEAN DEFAULT TRUE,
  is_admin      BOOLEAN DEFAULT TRUE,
  created_at    TIMESTAMP DEFAULT now(),
  updated_at    TIMESTAMP DEFAULT now(),

  UNIQUE (email),
  UNIQUE (access_token)
);