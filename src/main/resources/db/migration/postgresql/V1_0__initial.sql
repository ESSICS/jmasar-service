CREATE FUNCTION update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
  BEGIN
    NEW.last_modified = NOW();
    RETURN NEW;
  END;
$$;

CREATE TYPE node_type AS ENUM ('FOLDER', 'CONFIGURATION');

CREATE TABLE IF NOT EXISTS node(
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  type node_type,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER node_updated_at_modtime BEFORE UPDATE ON node FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE INDEX IF NOT EXISTS node_idx ON node(id, name, type);

CREATE TABLE IF NOT EXISTS node_closure(
  ancestor INTEGER NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  descendant INTEGER NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  depth INTEGER NOT NULL
);

INSERT INTO node values(0, 'Save & Restore Root', 'FOLDER');
INSERT INTO node_closure values(0, 0, 0);

CREATE TABLE IF NOT EXISTS config (
  node_id INTEGER REFERENCES node(id) ON DELETE CASCADE,
  active INTEGER NOT NULL DEFAULT 1,
  description TEXT NOT NULL,
  system TEXT
);

CREATE TABLE IF NOT EXISTS config_pv (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  tags TEXT,
  groupName TEXT,
  readonly INT NOT NULL DEFAULT 0,
  UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS config_pv_relation (
  config_id INTEGER REFERENCES node(id) ON DELETE CASCADE NOT NULL,
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
  config_id INTEGER REFERENCES node(id) ON DELETE CASCADE,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  username_id INTEGER REFERENCES username(id) ON DELETE CASCADE,
  comment TEXT,
  approve BOOLEAN
);

CREATE INDEX IF NOT EXISTS snapshot_config_idx ON snapshot(config_id);

CREATE TABLE IF NOT EXISTS snapshot_pv (
  snapshot_id INTEGER REFERENCES snapshot(id) ON DELETE CASCADE NOT NULL,
  config_pv_id INTEGER REFERENCES config_pv(id) ON DELETE CASCADE NOT NULL,
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
