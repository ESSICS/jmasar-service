CREATE TABLE IF NOT EXISTS config (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  next INTEGER REFERENCES config(id) ON DELETE CASCADE,
  created TIMESTAMP NOT NULL,
  active INTEGER NOT NULL DEFAULT 1,
  description VARCHAR NOT NULL,
  system VARCHAR,
  UNIQUE(name)
);

CREATE INDEX IF NOT EXISTS config_name_idx ON config(name);

CREATE TABLE IF NOT EXISTS config_pv (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  tags VARCHAR,
  groupName VARCHAR,
  readonly INT NOT NULL DEFAULT 0,
  UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS config_pv_relation (
  config_id INTEGER NOT NULL REFERENCES config(id) ON DELETE CASCADE,
  config_pv_id INTEGER NOT NULL REFERENCES config_pv(id) ON DELETE CASCADE  
);

CREATE INDEX IF NOT EXISTS config_pv_idx ON config_pv_relation(config_id, config_pv_id);
CREATE INDEX IF NOT EXISTS pv_name_idx ON config_pv(name);

CREATE TABLE IF NOT EXISTS username(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS snapshot (
  id SERIAL PRIMARY KEY,
  config_id INTEGER  NOT NULL REFERENCES config(id) ON DELETE CASCADE,
  created TIMESTAMP NOT NULL,
  username_id INTEGER REFERENCES username(id) ON DELETE CASCADE,
  comment VARCHAR,
  approve BOOLEAN
);

CREATE INDEX IF NOT EXISTS snapshot_config_idx ON snapshot(config_id);
CREATE INDEX IF NOT EXISTS snapshot_config_created_idx ON snapshot(config_id,created);

CREATE TABLE IF NOT EXISTS snapshot_pv (
  id SERIAL PRIMARY KEY,
  snapshot_id INTEGER NOT NULL REFERENCES snapshot(id) ON DELETE CASCADE,
  dtype INTEGER NOT NULL,
  severity INTEGER NOT NULL,
  status INTEGER NOT NULL,
  time BIGINT NOT NULL,
  timens INTEGER NOT NULL,
  clazz VARCHAR NOT NULL,
  value VARCHAR NOT NULL DEFAULT '0',
  fetch_status BOOLEAN
);

CREATE INDEX IF NOT EXISTS username_idx ON username(name);
CREATE INDEX IF NOT EXISTS snapshot_pv_idx ON snapshot_pv(snapshot_id);
