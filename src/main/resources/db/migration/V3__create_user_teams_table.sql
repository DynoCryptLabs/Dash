CREATE TABLE IF NOT EXISTS "user_teams"
(
  user_id  INTEGER REFERENCES users(id),
  team_id  INTEGER REFERENCES teams(id),
  is_admin BOOLEAN DEFAULT FALSE,
  created_at    TIMESTAMP DEFAULT now(),
  updated_at    TIMESTAMP DEFAULT now(),

  UNIQUE(user_id, team_id)
);