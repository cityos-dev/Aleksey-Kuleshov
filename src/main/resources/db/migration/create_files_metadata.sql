CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE files_metadata (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT,
  size_bytes INTEGER NOT NULL CHECK (size_bytes >= 0),
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);
