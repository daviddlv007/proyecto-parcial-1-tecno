-- Script de inicialización de base de datos
-- Tabla simple: Persona con id autoincremental, nombre y apellido

CREATE TABLE IF NOT EXISTS persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de prueba
INSERT INTO persona (nombre, apellido) VALUES 
    ('Juan', 'Pérez'),
    ('María', 'González'),
    ('Carlos', 'Rodríguez');

-- Verificar inserción
SELECT * FROM persona;
