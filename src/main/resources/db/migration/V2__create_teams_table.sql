CREATE TABLE IF NOT EXISTS "teams"
(
  id   SERIAL PRIMARY KEY,
  name VARCHAR(255),
  created_at    TIMESTAMP DEFAULT now(),
  updated_at    TIMESTAMP DEFAULT now()
);