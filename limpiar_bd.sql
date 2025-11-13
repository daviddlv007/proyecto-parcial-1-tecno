-- ========================================
-- SCRIPT DE LIMPIEZA TOTAL DE BASE DE DATOS
-- Respeta orden de claves foráneas
-- ========================================

-- 1. Eliminar registros de tablas con FKs (orden inverso de dependencias)
DELETE FROM pago;
DELETE FROM inscripcion;
DELETE FROM sesion;
DELETE FROM actividad;
DELETE FROM vehiculo;
DELETE FROM usuario;

-- 2. Eliminar registros de tablas catálogo (sin FKs)
DELETE FROM metodo_pago;
DELETE FROM tipo_pago;
DELETE FROM tipo_actividad;
DELETE FROM tipo_vehiculo;
DELETE FROM rol;

-- 3. Resetear secuencias (para que los IDs empiecen desde 1)
ALTER SEQUENCE rol_id_seq RESTART WITH 1;
ALTER SEQUENCE tipo_vehiculo_id_seq RESTART WITH 1;
ALTER SEQUENCE tipo_actividad_id_seq RESTART WITH 1;
ALTER SEQUENCE tipo_pago_id_seq RESTART WITH 1;
ALTER SEQUENCE metodo_pago_id_seq RESTART WITH 1;
ALTER SEQUENCE usuario_id_seq RESTART WITH 1;
ALTER SEQUENCE vehiculo_id_seq RESTART WITH 1;
ALTER SEQUENCE actividad_id_seq RESTART WITH 1;
ALTER SEQUENCE sesion_id_seq RESTART WITH 1;
ALTER SEQUENCE inscripcion_id_seq RESTART WITH 1;
ALTER SEQUENCE pago_id_seq RESTART WITH 1;

-- 4. Insertar datos base (catálogos necesarios para FKs)
INSERT INTO rol (nombre_rol, descripcion) VALUES
('Admin', 'Administrador del sistema'),
('Instructor', 'Instructor de cursos'),
('Alumno', 'Estudiante inscrito');

INSERT INTO tipo_vehiculo (nombre_tipo, descripcion) VALUES
('Auto', 'Automóvil particular'),
('Camioneta', 'Camioneta o SUV'),
('Moto', 'Motocicleta');

INSERT INTO tipo_actividad (nombre_tipo, descripcion) VALUES
('Teórica', 'Clase teórica en aula'),
('Práctica', 'Clase práctica de manejo'),
('Evaluación', 'Examen teórico o práctico');

INSERT INTO tipo_pago (nombre_tipo, descripcion) VALUES
('Contado', 'Pago único completo'),
('Cuotas', 'Pago fraccionado'),
('Mensual', 'Pago mensual recurrente');

INSERT INTO metodo_pago (nombre_metodo, descripcion) VALUES
('Efectivo', 'Pago en efectivo'),
('Transferencia', 'Transferencia bancaria'),
('Tarjeta', 'Tarjeta de débito o crédito');

-- 5. Insertar datos mínimos en tablas principales
INSERT INTO usuario (nombre, apellido, fecha_nacimiento, genero, tipo_documento, numero_documento, email, telefono, direccion, contrasena, rol_id) VALUES
('Admin', 'Sistema', '1990-01-01', 'M', 'CI', '9999999', 'admin@escuela.bo', '70000000', 'Av. Principal #1', 'admin123', 1),
('Carlos', 'Pérez', '1985-05-15', 'M', 'CI', '7777777', 'carlos@escuela.bo', '70111111', 'Calle 1 #100', 'instructor123', 2),
('Ana', 'López', '2000-03-20', 'F', 'CI', '8888888', 'ana@mail.com', '70222222', 'Zona Sur #50', 'alumno123', 3);

INSERT INTO vehiculo (placa, marca, modelo, anio, tipo_vehiculo_id, estado_vehiculo, capacidad) VALUES
('TEST-001', 'Toyota', 'Yaris', 2020, 1, 'Disponible', 4);

INSERT INTO actividad (nombre, descripcion, tipo_actividad_id, duracion_horas, nivel) VALUES
('Curso Básico', 'Curso inicial de manejo', 2, 2, 1);

-- Mensaje de confirmación
SELECT 'Base de datos limpiada y con datos base inicializados' AS mensaje;
