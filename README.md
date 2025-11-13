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
- ✅ **Manejo robusto** de errores de red

## 🚀 Inicio Rápido

### 1. Compilar imagen Docker
```bash
docker build -t mail-sistema-grupo17sa .
```

### 2. Ejecutar sistema
```bash
docker run -v $(pwd)/config.properties:/app/config.properties:ro mail-sistema-grupo17sa
```

### 3. Probar (opción 1 - RECOMENDADO)
```bash
bash test_replicable.sh
```
**Resultado:** ✅ 61/61 tests exitosos en ~15 segundos

### 4. Probar (opción 2 - manual)
1. Enviar correo desde Gmail a: `grupo17sa@tecnoweb.org.bo`
2. Asunto: `AYUDA` o `LISROL["*"]`
3. Ver respuesta en consola Docker (5-10 segundos)

## 📚 Documentación

- **[MANUAL_PROYECTO.md](MANUAL_PROYECTO.md)** - Documentación completa del sistema
- **[PRUEBAS_MANUALES.md](PRUEBAS_MANUALES.md)** - Guía de 45 pruebas manuales vía Gmail
- **[test_replicable.sh](test_replicable.sh)** - Suite de pruebas automatizadas
- **[VALIDACION_RAPIDA.md](VALIDACION_RAPIDA.md)** - 7 tests rápidos (5-10 min)

## 🗄️ Base de Datos

**PostgreSQL** en `www.tecnoweb.org.bo:5432`

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
- Docker

## 👨‍💻 Autores

**Grupo 17SA**  
INF513 - Tecnología Web  
Universidad Mayor de San Andrés

## 📄 Licencia

Proyecto académico - 2025
