-- Create clients table
CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    birth_date DATE NOT NULL,
    CONSTRAINT age_positive CHECK (age >= 0)
);

-- Create index on birth_date for potential queries
CREATE INDEX idx_clients_birth_date ON clients(birth_date);

-- Create index on full name for potential searches
CREATE INDEX idx_clients_full_name ON clients(first_name, last_name);

