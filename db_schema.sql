-- =============================
-- SISTEMA DE GESTIÓN VÍA EMAIL
-- Grupo 17SA - INF513 TecnoWeb
-- Base de datos completa con datos iniciales
-- =============================

-- Conectar a la base de datos
\c db_grupo17sa;

-- Eliminar tablas si existen (orden inverso por dependencias)
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS inscripcion CASCADE;
DROP TABLE IF EXISTS sesion CASCADE;
DROP TABLE IF EXISTS actividad CASCADE;
DROP TABLE IF EXISTS vehiculo CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS metodo_pago CASCADE;
DROP TABLE IF EXISTS tipo_pago CASCADE;
DROP TABLE IF EXISTS tipo_actividad CASCADE;
DROP TABLE IF EXISTS tipo_vehiculo CASCADE;
DROP TABLE IF EXISTS rol CASCADE;

-- =============================
-- TABLAS DE REFERENCIA (Catálogos)
-- =============================

CREATE TABLE rol (
    id SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE tipo_vehiculo (
    id SERIAL PRIMARY KEY,
    nombre_tipo VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE tipo_actividad (
    id SERIAL PRIMARY KEY,
    nombre_tipo VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE tipo_pago (
    id SERIAL PRIMARY KEY,
    nombre_tipo VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE metodo_pago (
    id SERIAL PRIMARY KEY,
    nombre_metodo VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

-- =============================
-- TABLAS PRINCIPALES
-- =============================

-- 1. Usuarios / Personas
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    tipo_documento VARCHAR(50),
    numero_documento VARCHAR(50) UNIQUE,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(50),
    direccion TEXT,
    contrasena VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL REFERENCES rol(id),
    estado_usuario VARCHAR(20) DEFAULT 'activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Vehículos
CREATE TABLE vehiculo (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(20) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    anio INT,
    tipo_vehiculo_id INT REFERENCES tipo_vehiculo(id),
    estado_vehiculo VARCHAR(50) DEFAULT 'disponible',
    capacidad INT,
    fecha_mantenimiento DATE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Actividades
CREATE TABLE actividad (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo_actividad_id INT REFERENCES tipo_actividad(id),
    duracion_horas DECIMAL(4,2),
    estado_actividad VARCHAR(20) DEFAULT 'activa',
    nivel VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Sesiones
CREATE TABLE sesion (
    id SERIAL PRIMARY KEY,
    actividad_id INT REFERENCES actividad(id),
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    instructor_id INT REFERENCES usuario(id),
    vehiculo_id INT REFERENCES vehiculo(id),
    lugar TEXT,
    estado_sesion VARCHAR(20) DEFAULT 'programada',
    capacidad_maxima INT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Inscripciones
CREATE TABLE inscripcion (
    id SERIAL PRIMARY KEY,
    alumno_id INT REFERENCES usuario(id),
    sesion_id INT REFERENCES sesion(id),
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_inscripcion VARCHAR(20) DEFAULT 'pendiente',
    tipo_pago_id INT REFERENCES tipo_pago(id),
    monto_total DECIMAL(12,2) NOT NULL,
    curso_id INT,
    observaciones TEXT
);

-- 6. Pagos
CREATE TABLE pago (
    id SERIAL PRIMARY KEY,
    alumno_id INT REFERENCES usuario(id),
    inscripcion_id INT REFERENCES inscripcion(id),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(12,2) NOT NULL,
    metodo_pago_id INT REFERENCES metodo_pago(id),
    comprobante VARCHAR(100),
    observaciones TEXT
);

-- =============================
-- DATOS INICIALES
-- =============================

-- Roles
INSERT INTO rol (nombre_rol, descripcion) VALUES
('Administrador', 'Acceso total al sistema'),
('Instructor', 'Imparte clases y sesiones'),
('Alumno', 'Estudiante inscrito en actividades'),
('Auxiliar', 'Personal de apoyo');

-- Tipos de vehículo
INSERT INTO tipo_vehiculo (nombre_tipo, descripcion) VALUES
('Auto', 'Vehículo liviano estándar'),
('Camioneta', 'Vehículo tipo pickup o SUV'),
('Motocicleta', 'Vehículo de dos ruedas'),
('Bus', 'Vehículo de transporte masivo');

-- Tipos de actividad
INSERT INTO tipo_actividad (nombre_tipo, descripcion) VALUES
('Teórica', 'Clases en aula'),
('Práctica', 'Clases de manejo'),
('Evaluación', 'Exámenes teóricos o prácticos'),
('Taller', 'Actividades especiales');

-- Tipos de pago
INSERT INTO tipo_pago (nombre_tipo, descripcion) VALUES
('Contado', 'Pago único completo'),
('Cuotas', 'Pago en parcialidades'),
('Mensual', 'Pago mensual recurrente'),
('Beca', 'Exento de pago');

-- Métodos de pago
INSERT INTO metodo_pago (nombre_metodo, descripcion) VALUES
('Efectivo', 'Pago en efectivo'),
('Transferencia', 'Transferencia bancaria'),
('Tarjeta', 'Tarjeta de crédito/débito'),
('QR', 'Código QR bancario');

-- Usuarios (Admin, Instructores, Alumnos)
INSERT INTO usuario (nombre, apellido, fecha_nacimiento, genero, tipo_documento, numero_documento, email, telefono, direccion, contrasena, rol_id) VALUES
('Carlos', 'Admin', '1985-05-15', 'M', 'CI', '11111111', 'admin@sistema.com', '70000001', 'Av. Principal #100', 'admin123', 1),
('María', 'Instructor', '1990-03-20', 'F', 'CI', '22222222', 'maria@sistema.com', '70000002', 'Calle Segunda #200', 'inst123', 2),
('Juan', 'Pérez', '2000-07-10', 'M', 'CI', '33333333', 'juan@test.com', '70000003', 'Zona Norte #300', 'alumno123', 3),
('Ana', 'García', '1999-11-25', 'F', 'CI', '44444444', 'ana@test.com', '70000004', 'Barrio Sur #400', 'alumno123', 3);

-- Vehículos
INSERT INTO vehiculo (placa, marca, modelo, anio, tipo_vehiculo_id, estado_vehiculo, capacidad) VALUES
('ABC-123', 'Toyota', 'Corolla', 2020, 1, 'disponible', 5),
('DEF-456', 'Nissan', 'Frontier', 2019, 2, 'disponible', 5),
('GHI-789', 'Honda', 'CBR', 2021, 3, 'disponible', 2),
('JKL-012', 'Mercedes', 'Sprinter', 2018, 4, 'mantenimiento', 20);

-- Actividades
INSERT INTO actividad (nombre, descripcion, tipo_actividad_id, duracion_horas, nivel) VALUES
('Manejo Básico', 'Introducción al manejo de vehículos', 2, 2.0, 'Principiante'),
('Código Vial', 'Leyes y señalización de tránsito', 1, 1.5, 'Básico'),
('Manejo Defensivo', 'Técnicas de conducción segura', 2, 3.0, 'Intermedio'),
('Examen Práctico', 'Evaluación de habilidades de conducción', 3, 1.0, 'Todos');

-- Sesiones
INSERT INTO sesion (actividad_id, fecha, hora_inicio, hora_fin, instructor_id, vehiculo_id, lugar, capacidad_maxima) VALUES
(1, CURRENT_DATE + INTERVAL '1 day', '08:00', '10:00', 2, 1, 'Circuito de práctica', 4),
(2, CURRENT_DATE + INTERVAL '2 days', '14:00', '15:30', 2, NULL, 'Aula 101', 15),
(3, CURRENT_DATE + INTERVAL '3 days', '10:00', '13:00', 2, 2, 'Ruta urbana', 3);

-- Inscripciones
INSERT INTO inscripcion (alumno_id, sesion_id, tipo_pago_id, monto_total, estado_inscripcion) VALUES
(3, 1, 1, 500.00, 'confirmada'),
(4, 1, 2, 500.00, 'pendiente'),
(3, 2, 1, 300.00, 'confirmada');

-- Pagos
INSERT INTO pago (alumno_id, inscripcion_id, monto, metodo_pago_id, comprobante) VALUES
(3, 1, 500.00, 1, 'COMP-001'),
(4, 2, 250.00, 2, 'TRANS-001'),
(3, 3, 300.00, 1, 'COMP-002');

-- =============================
-- VERIFICACIÓN
-- =============================

-- Mostrar conteo de registros
SELECT 'rol' as tabla, COUNT(*) as registros FROM rol
UNION ALL SELECT 'tipo_vehiculo', COUNT(*) FROM tipo_vehiculo
UNION ALL SELECT 'tipo_actividad', COUNT(*) FROM tipo_actividad
UNION ALL SELECT 'tipo_pago', COUNT(*) FROM tipo_pago
UNION ALL SELECT 'metodo_pago', COUNT(*) FROM metodo_pago
UNION ALL SELECT 'usuario', COUNT(*) FROM usuario
UNION ALL SELECT 'vehiculo', COUNT(*) FROM vehiculo
UNION ALL SELECT 'actividad', COUNT(*) FROM actividad
UNION ALL SELECT 'sesion', COUNT(*) FROM sesion
UNION ALL SELECT 'inscripcion', COUNT(*) FROM inscripcion
UNION ALL SELECT 'pago', COUNT(*) FROM pago;

\echo '✓ Base de datos creada exitosamente con datos iniciales'
