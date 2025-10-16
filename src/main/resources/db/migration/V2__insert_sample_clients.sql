-- Insert sample clients for testing and development
INSERT INTO clients (first_name, last_name, age, birth_date) VALUES
    ('Juan', 'Pérez', 30, '1994-01-15'),
    ('María', 'García', 25, '1999-06-20'),
    ('Carlos', 'Rodríguez', 45, '1979-03-10'),
    ('Ana', 'Martínez', 28, '1996-11-25'),
    ('Luis', 'López', 35, '1989-08-05'),
    ('Carmen', 'Sánchez', 42, '1982-02-18'),
    ('José', 'Fernández', 38, '1986-09-30'),
    ('Laura', 'González', 33, '1991-04-12'),
    ('Miguel', 'Díaz', 50, '1974-07-22'),
    ('Isabel', 'Torres', 29, '1995-12-08');

-- Verify the data was inserted
-- SELECT COUNT(*) as total_clients FROM clients;

