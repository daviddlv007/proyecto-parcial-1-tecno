# Sistema de Gestión vía E-Mail

**Grupo 17SA - INF513 Tecnología Web**

Sistema empresarial completo que opera vía correo electrónico usando **sockets puros** (sin JavaMail). Implementa CRUD para **11 tablas** con **45 comandos** totales.

## 🎯 Características Principales

- ✅ **11 tablas** con CRUD completo (5 catálogos + 6 principales)
- ✅ **45 comandos** funcionales (AYUDA + 44 CRUD)
- ✅ **100% tests pasando** (61/61 comandos validados)
- ✅ **Sockets puros** (POP3/SMTP nativos - sin JavaMail)
- ✅ **JDBC puro** con PreparedStatements
- ✅ **MIME decoding** (Base64 + Quoted-Printable)
- ✅ **Auto-reconexión BD** ante timeouts
- ✅ **Docker Compose** con PostgreSQL local
- ✅ **Variables de entorno** modulares

## 🚀 Inicio Rápido

### 1. Configurar variables de entorno (primera vez)
Edita el archivo `.env` con tus credenciales:
```bash
# Correo SMTP para enviar respuestas
MAIL_SMTP_USER=tu-correo@gmail.com
MAIL_SMTP_PASSWORD=tu-app-password

# POP3 para recibir correos
MAIL_POP3_USER=grupo17sa
MAIL_POP3_PASSWORD=grup017grup017*
```

### 2. Levantar sistema completo (BD + App)
```bash
docker-compose up --build -d
```

Esto levanta:
- **PostgreSQL** en puerto `5432` (con esquema inicializado)
- **Aplicación Java** conectada a la BD local

### 3. Verificar estado
```bash
docker-compose ps          # Ver estado de servicios
docker-compose logs app    # Ver logs de la aplicación
docker-compose logs db     # Ver logs de la base de datos
```

### 4. Probar el sistema

**Opción 1 - Automatizado (RECOMENDADO):**
```bash
bash test_replicable.sh
```
**Resultado:** ✅ 61/61 tests exitosos en ~15 segundos

**Opción 2 - Manual vía Gmail:**
1. Enviar correo a: `grupo17sa@tecnoweb.org.bo`
2. Asunto: `AYUDA` o `LISROL["*"]`
3. Ver respuesta en `docker-compose logs app` (5-10 segundos)

### 5. Detener sistema
```bash
docker-compose down              # Detener servicios (mantiene BD)
docker-compose down -v           # Detener y borrar BD
```

## 📁 Archivos de Configuración

```
.env                          # Variables de entorno (DB, emails)
docker-compose.yml            # Orquestación de servicios
Dockerfile                    # Imagen de la aplicación
docker-entrypoint.sh          # Script que genera config.properties
config.properties             # Configuración para producción (BD remota)
```

**Diferencia clave:**
- **Docker Compose:** Usa `.env` → genera `config.properties` automáticamente
- **Producción manual:** Usa `config.properties` directamente

## 📚 Documentación

- **[MANUAL_PROYECTO.md](MANUAL_PROYECTO.md)** - Documentación completa del sistema
- **[PRUEBAS_MANUALES.md](PRUEBAS_MANUALES.md)** - Guía de 45 pruebas manuales vía Gmail
- **[test_replicable.sh](test_replicable.sh)** - Suite de pruebas automatizadas
- **[VALIDACION_RAPIDA.md](VALIDACION_RAPIDA.md)** - 7 tests rápidos (5-10 min)

## 🗄️ Base de Datos

**Con Docker Compose:** PostgreSQL local en `db:5432`  
**Producción:** PostgreSQL en `www.tecnoweb.org.bo:5432`

11 tablas:
- **Catálogos:** rol, tipo_vehiculo, tipo_actividad, tipo_pago, metodo_pago
- **Principales:** usuario, vehiculo, actividad, sesion, inscripcion, pago

## 📋 Comandos Disponibles

**Formato:** `COMANDO["param1","param2",...]`

**Ejemplos:**
```
AYUDA
LISROL["*"]
INSROL["Gerente","Gerente de área"]
LISUSU["*"]
INSUSU["Juan","Pérez","1990-05-15","M","CI","123456","juan@mail.com","70123456","Calle 1","pass123","3"]
```

Ver [MANUAL_PROYECTO.md](MANUAL_PROYECTO.md) para lista completa de 45 comandos.

## 🧪 Testing

### Opción 1: Automatizado (15 segundos)
```bash
bash test_replicable.sh
```

### Opción 2: Manual vía Gmail (2-3 minutos)
Ver [PRUEBAS_MANUALES.md](PRUEBAS_MANUALES.md) para 45 comandos de ejemplo.

## 🏗️ Arquitectura

```
Email → POP3 Socket (110) → Parser → CommandProcessor → DAOs → PostgreSQL
                                         ↓
Email ← SMTP Socket (25)  ← HTML Response ←─────────────────────────────┘
```

**Tecnologías:**
- Java 11 (Eclipse Temurin)
- Sockets puros (java.net.Socket)
- JDBC puro (PostgreSQL driver)
- Docker + Docker Compose
- PostgreSQL 13

## 🔧 Comandos Docker Útiles

```bash
# Ver logs en tiempo real
docker-compose logs -f app

# Ejecutar comandos SQL en la BD
docker-compose exec db psql -U grupo17sa -d db_grupo17sa -c "\dt"

# Reiniciar solo la aplicación
docker-compose restart app

# Reconstruir todo desde cero
docker-compose down -v && docker-compose up --build -d
```

## 👨‍💻 Autores

**Grupo 17SA**  
INF513 - Tecnología Web  
Universidad Mayor de San Andrés

## 📄 Licencia

Proyecto académico - 2025
