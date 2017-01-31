CREATE TABLE IF NOT EXISTS "user_teams"
(
  user_id  INTEGER REFERENCES users(id),
  team_id  INTEGER REFERENCES teams(id),
  is_admin BOOLEAN DEFAULT FALSE,

  UNIQUE(user_id, team_id)
);