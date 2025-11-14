-- =============================
-- SISTEMA DE GESTIÓN VÍA EMAIL
-- Grupo 17SA - INF513 TecnoWeb
-- Script unificado: Reseteo + Creación + Población
-- Ejecutado automáticamente al iniciar contenedor
-- =============================

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

-- Usuarios (Admin, Instructores, Alumnos, Auxiliares - Total: 14)
INSERT INTO usuario (nombre, apellido, fecha_nacimiento, genero, tipo_documento, numero_documento, email, telefono, direccion, contrasena, rol_id) VALUES
('Carlos', 'Admin', '1985-05-15', 'M', 'CI', '11111111', 'admin@sistema.com', '70000001', 'Av. Principal #100', 'admin123', 1),
('María', 'Instructor', '1990-03-20', 'F', 'CI', '22222222', 'maria@sistema.com', '70000002', 'Calle Segunda #200', 'inst123', 2),
('Juan', 'Pérez', '2000-07-10', 'M', 'CI', '33333333', 'juan@test.com', '70000003', 'Zona Norte #300', 'alumno123', 3),
('Ana', 'García', '1999-11-25', 'F', 'CI', '44444444', 'ana@test.com', '70000004', 'Barrio Sur #400', 'alumno123', 3),
('Pedro', 'Rodríguez', '1995-08-12', 'M', 'CI', '55555555', 'pedro@test.com', '70000005', 'Av. Arce #500', 'alumno123', 3),
('Laura', 'Martínez', '1998-04-18', 'F', 'CI', '66666666', 'laura@test.com', '70000006', 'Calle Comercio #600', 'alumno123', 3),
('Roberto', 'Sánchez', '1992-12-05', 'M', 'CI', '77777777', 'roberto@sistema.com', '70000007', 'Zona Central #700', 'inst123', 2),
('Carmen', 'López', '1988-09-30', 'F', 'CI', '88888888', 'carmen@sistema.com', '70000008', 'Barrio Miraflores #800', 'inst123', 2),
('Jorge', 'Fernández', '2001-02-14', 'M', 'CI', '99999999', 'jorge@test.com', '70000009', 'Zona Sur #900', 'alumno123', 3),
('Sofía', 'Ramírez', '2000-06-22', 'F', 'CI', '10101010', 'sofia@test.com', '70000010', 'Av. Ballivián #1000', 'alumno123', 3),
('Diego', 'Torres', '1997-01-08', 'M', 'CI', '20202020', 'diego@test.com', '70000011', 'Calle Murillo #1100', 'alumno123', 3),
('Patricia', 'Vargas', '1996-10-17', 'F', 'CI', '30303030', 'patricia@test.com', '70000012', 'Zona Este #1200', 'alumno123', 3),
('Luis', 'Auxiliar', '1994-03-25', 'M', 'CI', '40404040', 'luis@sistema.com', '70000013', 'Calle Ayacucho #1300', 'aux123', 4),
('Elena', 'Auxiliar2', '1993-07-11', 'F', 'CI', '50505050', 'elena@sistema.com', '70000014', 'Av. América #1400', 'aux123', 4);

-- Vehículos (Total: 14 - distribuidos entre los 4 tipos)
INSERT INTO vehiculo (placa, marca, modelo, anio, tipo_vehiculo_id, estado_vehiculo, capacidad) VALUES
('ABC-123', 'Toyota', 'Corolla', 2020, 1, 'disponible', 5),
('DEF-456', 'Nissan', 'Frontier', 2019, 2, 'disponible', 5),
('GHI-789', 'Honda', 'CBR', 2021, 3, 'disponible', 2),
('JKL-012', 'Mercedes', 'Sprinter', 2018, 4, 'mantenimiento', 20),
('MNO-345', 'Mazda', '3', 2021, 1, 'disponible', 5),
('PQR-678', 'Hyundai', 'Elantra', 2020, 1, 'disponible', 5),
('STU-901', 'Volkswagen', 'Gol', 2019, 1, 'mantenimiento', 5),
('VWX-234', 'Ford', 'Ranger', 2020, 2, 'disponible', 5),
('YZA-567', 'Chevrolet', 'Colorado', 2018, 2, 'disponible', 5),
('BCD-890', 'Yamaha', 'FZ', 2022, 3, 'disponible', 2),
('EFG-123', 'Suzuki', 'GSX', 2021, 3, 'disponible', 2),
('HIJ-456', 'Kawasaki', 'Ninja', 2020, 3, 'mantenimiento', 2),
('KLM-789', 'Volkswagen', 'Crafter', 2019, 4, 'disponible', 18),
('NOP-012', 'Iveco', 'Daily', 2020, 4, 'disponible', 16);

-- Actividades (Total: 14 - distribuidas entre los 4 tipos)
INSERT INTO actividad (nombre, descripcion, tipo_actividad_id, duracion_horas, nivel) VALUES
('Manejo Básico', 'Introducción al manejo de vehículos', 2, 2.0, 'Principiante'),
('Código Vial', 'Leyes y señalización de tránsito', 1, 1.5, 'Básico'),
('Manejo Defensivo', 'Técnicas de conducción segura', 2, 3.0, 'Intermedio'),
('Examen Práctico', 'Evaluación de habilidades de conducción', 3, 1.0, 'Todos'),
('Señalización Vial', 'Estudio de señales de tránsito', 1, 2.0, 'Básico'),
('Primeros Auxilios', 'Atención de emergencias en carretera', 1, 3.0, 'Básico'),
('Mecánica Básica', 'Conocimientos fundamentales del vehículo', 1, 2.5, 'Básico'),
('Práctica Nocturna', 'Manejo en condiciones de baja visibilidad', 2, 2.0, 'Avanzado'),
('Práctica en Autopista', 'Conducción en vías rápidas', 2, 3.0, 'Intermedio'),
('Práctica de Estacionamiento', 'Técnicas de estacionamiento paralelo y perpendicular', 2, 1.5, 'Principiante'),
('Conducción Urbana', 'Manejo en ciudad con tráfico', 2, 2.5, 'Intermedio'),
('Examen Teórico', 'Evaluación de conocimientos', 3, 0.5, 'Todos'),
('Simulador de Conducción', 'Práctica en simulador virtual', 4, 1.0, 'Principiante'),
('Conducción Ecológica', 'Técnicas de ahorro de combustible', 1, 1.5, 'Intermedio');

-- Sesiones (Total: 14)
INSERT INTO sesion (actividad_id, fecha, hora_inicio, hora_fin, instructor_id, vehiculo_id, lugar, capacidad_maxima) VALUES
(1, CURRENT_DATE + INTERVAL '1 day', '08:00', '10:00', 2, 1, 'Circuito de práctica', 4),
(2, CURRENT_DATE + INTERVAL '2 days', '14:00', '15:30', 2, NULL, 'Aula 101', 15),
(3, CURRENT_DATE + INTERVAL '3 days', '10:00', '13:00', 2, 2, 'Ruta urbana', 3),
(1, CURRENT_DATE + INTERVAL '1 day', '08:00', '10:00', 7, 5, 'Circuito básico', 4),
(2, CURRENT_DATE + INTERVAL '1 day', '10:30', '12:00', 8, NULL, 'Aula 102', 20),
(3, CURRENT_DATE + INTERVAL '2 days', '08:00', '11:00', 7, 6, 'Circuito avanzado', 3),
(8, CURRENT_DATE + INTERVAL '2 days', '14:00', '16:00', 2, 7, 'Ruta nocturna', 2),
(9, CURRENT_DATE + INTERVAL '3 days', '09:00', '12:00', 8, 8, 'Autopista Norte', 4),
(10, CURRENT_DATE + INTERVAL '3 days', '14:00', '15:30', 7, 9, 'Zona Centro', 5),
(11, CURRENT_DATE + INTERVAL '4 days', '08:00', '10:30', 2, 10, 'Ciudad', 4),
(12, CURRENT_DATE + INTERVAL '4 days', '11:00', '11:30', 8, NULL, 'Aula 101', 30),
(13, CURRENT_DATE + INTERVAL '5 days', '15:00', '16:00', 7, 11, 'Circuito virtual', 10),
(14, CURRENT_DATE + INTERVAL '5 days', '10:00', '11:30', 2, 12, 'Ruta ecológica', 4),
(4, CURRENT_DATE + INTERVAL '6 days', '08:00', '09:00', 8, 13, 'Circuito de examen', 1);

-- Inscripciones (Total: 16)
INSERT INTO inscripcion (alumno_id, sesion_id, tipo_pago_id, monto_total, estado_inscripcion) VALUES
(3, 1, 1, 500.00, 'confirmada'),
(4, 1, 2, 500.00, 'pendiente'),
(3, 2, 1, 300.00, 'confirmada'),
(5, 4, 1, 450.00, 'confirmada'),
(6, 4, 2, 450.00, 'confirmada'),
(7, 6, 1, 600.00, 'confirmada'),
(8, 5, 3, 350.00, 'pendiente'),
(9, 7, 1, 700.00, 'confirmada'),
(10, 8, 2, 550.00, 'confirmada'),
(11, 9, 1, 480.00, 'confirmada'),
(12, 10, 3, 420.00, 'pendiente'),
(5, 11, 1, 650.00, 'confirmada'),
(6, 12, 2, 200.00, 'confirmada'),
(7, 13, 1, 300.00, 'confirmada'),
(8, 14, 3, 380.00, 'confirmada'),
(9, 3, 1, 550.00, 'confirmada');

-- Pagos (Total: 16 - distribuidos entre los 4 métodos)
INSERT INTO pago (alumno_id, inscripcion_id, monto, metodo_pago_id, comprobante, observaciones) VALUES
(3, 1, 500.00, 1, 'COMP-001', 'Pago efectivo completo'),
(4, 2, 250.00, 2, 'TRANS-001', 'Primera cuota transferencia'),
(3, 3, 300.00, 1, 'COMP-002', 'Pago efectivo completo'),
(5, 4, 450.00, 2, 'TRANS-002', 'Transferencia BCP'),
(6, 5, 225.00, 3, 'TRX-001', 'Primera cuota - Tarjeta Visa'),
(6, 5, 225.00, 3, 'TRX-002', 'Segunda cuota - Tarjeta Visa'),
(7, 6, 600.00, 1, 'COMP-003', 'Efectivo completo'),
(9, 9, 700.00, 4, 'QR-001', 'Pago QR BNB'),
(10, 10, 275.00, 3, 'TRX-003', 'Primera cuota - Tarjeta Mastercard'),
(10, 10, 275.00, 3, 'TRX-004', 'Segunda cuota - Tarjeta Mastercard'),
(11, 11, 480.00, 1, 'COMP-004', 'Efectivo completo'),
(5, 13, 650.00, 2, 'TRANS-003', 'Transferencia Banco Sol'),
(6, 14, 100.00, 4, 'QR-002', 'Primera mensualidad QR'),
(6, 14, 100.00, 4, 'QR-003', 'Segunda mensualidad QR'),
(7, 15, 300.00, 1, 'COMP-005', 'Efectivo completo'),
(8, 16, 380.00, 2, 'TRANS-004', 'Transferencia BCP');

-- =============================
-- VERIFICACIÓN
-- =============================

\echo ''
\echo '════════════════════════════════════════════════════════════'
\echo '   VERIFICACIÓN DE POBLACIÓN DE DATOS'
\echo '════════════════════════════════════════════════════════════'
\echo ''

SELECT 'TABLA' as categoria, 'REGISTROS' as cantidad
UNION ALL SELECT '─────────────', '──────────'
UNION ALL SELECT 'rol', COUNT(*)::text FROM rol
UNION ALL SELECT 'tipo_vehiculo', COUNT(*)::text FROM tipo_vehiculo
UNION ALL SELECT 'tipo_actividad', COUNT(*)::text FROM tipo_actividad
UNION ALL SELECT 'tipo_pago', COUNT(*)::text FROM tipo_pago
UNION ALL SELECT 'metodo_pago', COUNT(*)::text FROM metodo_pago
UNION ALL SELECT 'usuario', COUNT(*)::text FROM usuario
UNION ALL SELECT 'vehiculo', COUNT(*)::text FROM vehiculo
UNION ALL SELECT 'actividad', COUNT(*)::text FROM actividad
UNION ALL SELECT 'sesion', COUNT(*)::text FROM sesion
UNION ALL SELECT 'inscripcion', COUNT(*)::text FROM inscripcion
UNION ALL SELECT 'pago', COUNT(*)::text FROM pago;

\echo ''
\echo '--- Distribución para REPUSU (Usuarios por Rol) ---'
SELECT r.nombre_rol, COUNT(*) as cantidad
FROM usuario u
JOIN rol r ON u.rol_id = r.id
GROUP BY r.nombre_rol
ORDER BY cantidad DESC;

\echo ''
\echo '--- Distribución para REPVEH (Vehículos por Tipo) ---'
SELECT tv.nombre_tipo, COUNT(*) as cantidad
FROM vehiculo v
JOIN tipo_vehiculo tv ON v.tipo_vehiculo_id = tv.id
GROUP BY tv.nombre_tipo
ORDER BY cantidad DESC;

\echo ''
\echo '--- Distribución para REPACT (Actividades por Tipo) ---'
SELECT ta.nombre_tipo, COUNT(*) as cantidad
FROM actividad a
JOIN tipo_actividad ta ON a.tipo_actividad_id = ta.id
GROUP BY ta.nombre_tipo
ORDER BY cantidad DESC;

\echo ''
\echo '--- Distribución para REPPAG (Pagos por Método) ---'
SELECT mp.nombre_metodo, COUNT(*) as cantidad, SUM(p.monto) as total
FROM pago p
JOIN metodo_pago mp ON p.metodo_pago_id = mp.id
GROUP BY mp.nombre_metodo
ORDER BY total DESC;

\echo ''
\echo '✓ Base de datos inicializada exitosamente con datos de prueba'
\echo '════════════════════════════════════════════════════════════'
