# SISTEMA DE GESTIÓN VÍA EMAIL - MANUAL COMPLETO
## Grupo 17SA - INF513 Tecnología Web

---

## 📌 DESCRIPCIÓN

Sistema empresarial completo que opera vía correo electrónico usando **sockets puros** (sin JavaMail). Implementa CRUD para **11 tablas** (5 catálogos + 6 principales) con **45 comandos totales**.

**Tecnologías:** Java 11, Sockets puros (POP3/SMTP), JDBC, PostgreSQL, Docker

---

## 🏗️ ARQUITECTURA

```
Email → POP3 Socket (110) → Parser → CommandProcessor → DAOs → PostgreSQL (11 tablas)
                                           ↓
Email ← SMTP Socket (25)  ← HTML Response ←─────────────────────────────────┘
```

**Componentes:**
- `Main.java` - Loop infinito (10s)
- `POP3Client.java` - Recepción de correos
- `SMTPClientSocket.java` - Envío de respuestas
- `CommandParser.java` - Parser de comandos
- `CommandProcessor.java` - Lógica de negocio (45 comandos)
- `11 DAOs` - Acceso a datos JDBC puro

---

## 💾 BASE DE DATOS

**Servidor:** www.tecnoweb.org.bo:5432  
**Database:** db_grupo17sa  
**Usuario:** grupo17sa  
**Contraseña:** grup017grup017*

**Estructura (11 tablas):**

**Catálogos (5):**
1. `rol` - Roles de sistema
2. `tipo_vehiculo` - Tipos de vehículos
3. `tipo_actividad` - Tipos de actividades
4. `tipo_pago` - Modalidades de pago
5. `metodo_pago` - Métodos de pago

**Principales (6):**
6. `usuario` - Usuarios del sistema
7. `vehiculo` - Vehículos disponibles
8. `actividad` - Actividades/cursos
9. `sesion` - Sesiones programadas
10. `inscripcion` - Inscripciones de alumnos
11. `pago` - Pagos realizados

---

## ⚙️ INSTALACIÓN

### 1. Crear/limpiar base de datos
```bash
cd /root/proyectos/proyecto-email-grupo17sa
PGPASSWORD="grup017grup017*" psql -h www.tecnoweb.org.bo -U grupo17sa -d db_grupo17sa -f limpiar_bd.sql
```

### 2. Compilar Docker
```bash
docker build -t mail-sistema-grupo17sa .
```

### 3. Ejecutar sistema
```bash
docker run -v $(pwd)/config.properties:/app/config.properties:ro mail-sistema-grupo17sa
```

---

## 📋 COMANDOS (45 TOTAL)

**Formato:** `COMANDO["param1","param2",...]`

### TABLAS CATÁLOGO (20 comandos)

| Tabla | Listar | Insertar | Modificar | Eliminar |
|-------|--------|----------|-----------|----------|
| Rol | `LISROL["*"]` | `INSROL["nombre","desc"]` (2) | `MODROL["id","nom","desc","true"]` (4) | `DELROL["id"]` |
| Tipo Vehículo | `LISTVH["*"]` | `INSTVH["nombre","desc"]` (2) | `MODTVH["id","nom","desc","true"]` (4) | `DELTVH["id"]` |
| Tipo Actividad | `LISTAC["*"]` | `INSTAC["nombre","desc"]` (2) | `MODTAC["id","nom","desc","true"]` (4) | `DELTAC["id"]` |
| Tipo Pago | `LISTPG["*"]` | `INSTPG["nombre","desc"]` (2) | `MODTPG["id","nom","desc","true"]` (4) | `DELTPG["id"]` |
| Método Pago | `LISMPG["*"]` | `INSMPG["nombre","desc"]` (2) | `MODMPG["id","nom","desc","true"]` (4) | `DELMPG["id"]` |

### TABLAS PRINCIPALES (24 comandos)

| Tabla | Listar | Insertar | Modificar | Eliminar |
|-------|--------|----------|-----------|----------|
| Usuario | `LISUSU["*"]` | `INSUSU[...]` (11) | `MODUSU[...]` (12) | `DELUSU["id"]` |
| Vehículo | `LISVEH["*"]` | `INSVEH[...]` (7) | `MODVEH[...]` (8) | `DELVEH["id"]` |
| Actividad | `LISACT["*"]` | `INSACT[...]` (5) | `MODACT[...]` (6) | `DELACT["id"]` |
| Sesión | `LISSES["*"]` | `INSSES[...]` (8) | `MODSES[...]` (9) | `DELSES["id"]` |
| Inscripción | `LISINS["*"]` | `INSINS[...]` (7) | `MODINS[...]` (8) | `DELINS["id"]` |
| Pago | `LISPAG["*"]` | `INSPAG[...]` (6) | `MODPAG[...]` (7) | `DELPAG["id"]` |

**+ AYUDA** - Muestra todos los comandos disponibles

**Ejemplos:**
```
AYUDA
LISROL["*"]
INSROL["Gerente","Gerente de área"]
LISUSU["*"]
INSUSU["Juan","Pérez","1990-05-15","M","CI","123456","juan@mail.com","70123456","Calle 1","pass123","3"]
```

---

## 🧪 PRUEBAS

### Opción 1: Suite automatizada (100% replicable - RECOMENDADO)
```bash
bash test_replicable.sh
```

**Resultado:** ✅ **61/61 exitosas (100%)** - Sistema completamente validado

**Cobertura completa:**
- ✅ AYUDA (1 comando)
- ✅ INSERCIONES: 2 registros por tabla catálogo + principales (22 comandos)
- ✅ LISTADOS: Todas las tablas con patrón "*" (11 comandos)
- ✅ MODIFICACIONES: Catálogos y principales (11 comandos)
- ✅ ELIMINACIONES: En orden correcto de FK (11 comandos)
- ✅ VERIFICACIÓN: Conteos antes/después + listados detallados (5 comandos)

**Duración:** ~15 segundos

---

### Opción 2: Pruebas manuales vía Gmail (45 comandos)

**Archivo:** `PRUEBAS_MANUALES.md`

**Procedimiento:**
1. Iniciar sistema Docker en terminal
2. Enviar correos desde Gmail web interface (https://mail.google.com)
3. Ver respuestas en consola de Docker (5-10 segundos por comando)

**Ventajas:**
- Prueba el flujo completo real (POP3 + SMTP)
- Valida decodificación MIME de Gmail
- Demuestra funcionalidad end-to-end

**Desventajas:**
- Manual (45 correos)
- Requiere cuenta Gmail
- Más lento (5-10s por comando)

---

### Opción 3: Validación rápida (7 comandos, 5-10 minutos)

**Archivo:** `VALIDACION_RAPIDA.md`

**Comandos esenciales:**
1. AYUDA
2. LISROL["*"]
3. INSROL + verificación
4. MODROL + verificación  
5. DELROL + verificación
6. Verificar FK con INSUSU

**Uso:** Para demos o validación rápida del sistema

---

### Script de limpieza BD
```bash
PGPASSWORD="grup017grup017*" psql -h www.tecnoweb.org.bo -U grupo17sa -d db_grupo17sa -f limpiar_bd.sql
```

**Restablece:**
- 4 roles base (Admin, Instructor, Alumno, Supervisor)
- 3 usuarios base (IDs 1,2,3)
- 1 vehículo base (ID 1)
- 1 actividad base (ID 1)
- Todas las demás tablas vacías pero listas

---

## ✅ CARACTERÍSTICAS

- ✅ **11 tablas** con CRUD completo y validado (5 catálogos + 6 principales)
- ✅ **45 comandos** funcionales (AYUDA + 44 CRUD)
- ✅ **100% de pruebas automatizadas** pasando (61/61 comandos)
- ✅ **Sockets puros** (sin JavaMail) - POP3 y SMTP nativos
- ✅ **11 DAOs** con JDBC puro y PreparedStatements
- ✅ **Respuestas HTML** formateadas con charset UTF-8
- ✅ **Limpieza automática** de BD con datos base predefinidos
- ✅ **Pruebas 100% replicables** con Docker + script bash
- ✅ **Claves foráneas** respetadas en secuencia correcta
- ✅ **Código limpio** - src/conexion optimizado (3 archivos esenciales)
- ✅ **Stack traces** completos para debugging
- ✅ **Formato de salida mejorado** - listados legibles sin tablas HTML
- ✅ **MIME decoding** - Soporte Base64 y Quoted-Printable (Gmail compatible)
- ✅ **RFC 2822 folding** - Parsing de headers multi-línea
- ✅ **Auto-reconexión BD** - Recuperación automática de timeouts
- ✅ **Manejo robusto de errores** - No crashea por problemas de red
- ✅ **Formato HH:MM** - Soporta horas con/sin segundos
- ✅ **Patrón "*"** - Lista todos los registros correctamente
- ✅ **Salida limpia** - Sin mensajes debug, solo respuestas

---

## 📚 DOCUMENTACIÓN

El proyecto incluye documentación completa en 3 archivos principales:

### 1. MANUAL_PROYECTO.md (este archivo)
- Arquitectura general del sistema
- Instalación y configuración
- Comandos disponibles (45 totales)
- Características técnicas

### 2. test_replicable.sh
- Suite de pruebas automatizadas 100% funcional
- 61 comandos de prueba
- Validación completa de todas las tablas
- Ejecutable con: `bash test_replicable.sh`

### 3. PRUEBAS_MANUALES.md  
- Guía paso a paso para pruebas manuales vía Gmail
- 45 comandos de ejemplo con sintaxis exacta
- Instrucciones para enviar correos desde web
- Checklist de validación

### Documentación complementaria:

- **VALIDACION_RAPIDA.md** - 7 tests esenciales (5-10 minutos)
- **SOLUCION_MIME_DECODE.md** - Explicación técnica de bugs resueltos
- **db_schema.sql** - Esquema completo de base de datos
- **limpiar_bd.sql** - Script de reset de BD

---

## 🎓 DEFENSA

**Preparación:**
1. Limpiar BD: `bash limpiar_bd.sql`
2. Ejecutar sistema: `docker run -v $(pwd)/config.properties:/app/config.properties:ro mail-sistema-grupo17sa`
3. Sistema muestra respuestas en consola (puerto 25 bloqueado - no envía emails)

**Demostración sugerida:**

### Opción A: Pruebas automatizadas (RÁPIDO - 15 segundos)
```bash
bash test_replicable.sh
```
- Ejecuta 61 comandos automáticamente
- Muestra resultado: ✅ 61/61 exitosas
- Valida todas las tablas y operaciones

### Opción B: Demostración manual vía Gmail (VISUAL - 2-3 minutos)

1. **Mostrar AYUDA:**
   - Enviar correo desde Gmail con asunto: `AYUDA`
   - Esperar 5-10s → Ver respuesta en consola

2. **Listar roles:**
   - Asunto: `LISROL["*"]`
   - Ver los 4 roles base formateados

3. **Insertar rol:**
   - Asunto: `INSROL["Secretario","Personal administrativo"]`
   - Ver: "✓ Éxito - Rol insertado correctamente"

4. **Verificar inserción:**
   - Asunto: `LISROL["*"]`
   - Ver que ahora hay 5 roles (incluyendo "Secretario")

5. **Demostrar FK:**
   - Asunto: `INSUSU["Maria","Lopez","1992-08-10","F","CI","456789","maria@mail.com","72345678","Av. 6 de Agosto","pass123","3"]`
   - Explicar: rol_id=3 (Alumno) - clave foránea funcionando

**Puntos clave a mencionar:**
- ✅ 11 tablas con CRUD completo (45 comandos)
- ✅ Sockets puros (POP3/SMTP) sin JavaMail
- ✅ JDBC puro con PreparedStatements
- ✅ Decodificación MIME (Gmail compatible)
- ✅ Auto-reconexión BD ante timeouts
- ✅ 100% de tests pasando (61/61)
- ✅ Manejo robusto de errores de red

**Tiempo estimado:** 3-5 minutos demostración + preguntas

---

## 👨‍💻 INFORMACIÓN

**Grupo:** 17SA  
**Materia:** INF513 - Tecnología Web  
**Gestión:** 2025  
**Stack:** Java 11, Sockets puros, JDBC, PostgreSQL, Docker

---

**FIN DEL MANUAL**
