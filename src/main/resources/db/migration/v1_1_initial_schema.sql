CREATE TABLE IF NOT EXISTS upload (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  file_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
);

CREATE INDEX IF NOT EXISTS idx_upload_email ON upload(email);
