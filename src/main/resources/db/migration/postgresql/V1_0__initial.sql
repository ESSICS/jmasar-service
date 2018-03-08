CREATE TABLE IF NOT EXISTS config (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  next INTEGER REFERENCES config(id) ON DELETE CASCADE,
  created TIMESTAMP NOT NULL,
  active INTEGER NOT NULL DEFAULT 1,
  description TEXT NOT NULL,
  system TEXT,
  UNIQUE(name)
);

CREATE INDEX IF NOT EXISTS config_name_idx ON config(name);

CREATE TABLE IF NOT EXISTS config_pv (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  tags TEXT,
  groupName TEXT,
  readonly INT NOT NULL DEFAULT 0,
  UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS config_pv_relation (
  config_id INTEGER REFERENCES config(id) ON DELETE CASCADE NOT NULL,
  config_pv_id INTEGER REFERENCES config_pv(id) ON DELETE CASCADE NOT NULL
);

CREATE INDEX IF NOT EXISTS config_pv_idx ON config_pv_relation(config_id, config_pv_id);
CREATE INDEX IF NOT EXISTS pv_name_idx ON config_pv(name);

CREATE TABLE IF NOT EXISTS username(
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS snapshot (
  id SERIAL PRIMARY KEY,
  config_id INTEGER REFERENCES config(id) ON DELETE CASCADE NOT NULL,
  created TIMESTAMP NOT NULL,
  username_id INTEGER REFERENCES username(id) ON DELETE CASCADE,
  comment TEXT,
  approve BOOLEAN
);

CREATE INDEX IF NOT EXISTS snapshot_config_idx ON snapshot(config_id);
CREATE INDEX IF NOT EXISTS snapshot_config_created_idx ON snapshot(config_id,created);

CREATE TABLE IF NOT EXISTS snapshot_pv (
  id SERIAL PRIMARY KEY,
  snapshot_id INTEGER REFERENCES snapshot(id) ON DELETE CASCADE NOT NULL,
  dtype INTEGER NOT NULL,
  severity INTEGER NOT NULL,
  status INTEGER NOT NULL,
  time BIGINT NOT NULL,
  timens INTEGER NOT NULL,
  clazz TEXT NOT NULL,
  value TEXT NOT NULL DEFAULT '0',
  fetch_status BOOLEAN
);

CREATE INDEX IF NOT EXISTS username_idx ON username(name);
CREATE INDEX IF NOT EXISTS snapshot_pv_idx ON snapshot_pv(snapshot_id);

