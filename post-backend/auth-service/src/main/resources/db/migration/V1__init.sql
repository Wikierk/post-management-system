CREATE TABLE user_roles (
  user_id UUID,
  role TEXT,
  PRIMARY KEY (user_id, role)
);

CREATE TABLE users (
  user_id UUID PRIMARY KEY,
  email TEXT UNIQUE NOT NULL,
  password_hash TEXT,
  is_active BOOLEAN NOT NULL
);

CREATE TABLE user_identities (
  user_identity_id UUID PRIMARY KEY,
  provider TEXT NOT NULL,
  sub TEXT NOT NULL,
  user_id UUID NOT NULL
);

CREATE UNIQUE INDEX ON user_identities (user_id, provider);

CREATE UNIQUE INDEX ON user_identities (provider, sub);

ALTER TABLE user_roles ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE user_identities ADD FOREIGN KEY (user_id) REFERENCES users (user_id);
