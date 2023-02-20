CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE files_metadata (
  id VARCHAR(36) PRIMARY KEY DEFAULT uuid_generate_v4()::VARCHAR,
  name TEXT NOT NULL,
  size_bytes BIGINT NOT NULL CHECK (size_bytes >= 0),
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);
