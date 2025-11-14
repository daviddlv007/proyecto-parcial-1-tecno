# CAMBIOS IMPLEMENTADOS - Grupo 17SA
## Refactorización y Nuevas Funcionalidades

### 1. ARQUITECTURA DUAL SMTP SIMPLIFICADA
**Variable de control:** `MAIL_SENDER_TYPE` en `.env`

**Opciones:**
- `MAILERSEND` - API HTTP (puerto 443) - Funciona desde cualquier ubicación ✅ DEFAULT
- `SOCKET` - SMTP puro (puerto 25) - Solo desde red de facultad

**Cómo cambiar:**
```bash
# En archivo .env, cambiar línea:
MAIL_SENDER_TYPE=MAILERSEND    # o SOCKET

# Reconstruir:
docker-compose down && docker-compose up --build -d
```

### 2. LIMPIEZA DE CÓDIGO
**Eliminado:**
- ❌ `GmailSMTPClient.java` (puerto 587 bloqueado)
- ❌ 8 archivos de documentación innecesarios de la raíz
- ❌ Variables de configuración de Gmail en `.env`

**Conservado:**
- ✅ `README.md` 
- ✅ `test_replicable.sh`
- ✅ Docker files (compose, Dockerfile, entrypoint)
- ✅ Archivos SQL y config

### 3. RESPUESTAS HTML MODERNAS
**Nuevo archivo:** `src/conexion/HTMLResponseBuilder.java`

**Características:**
- Diseño moderno con gradientes (#667eea → #764ba2)
- Tablas responsive con hover effects
- Badges de estado (Activo/Inactivo)
- Tarjetas de estadísticas
- Gráficos de barras y círculos (CSS puro, sin JavaScript)
- Estilo minimalista y profesional

**Aplicado en:**
- ✅ Mensajes de éxito/error
- ✅ Comando AYUDA (mejorado)
- ⏳ Comandos de listado (parcial - estructura base lista)

### 4. MÓDULO DE REPORTES Y ESTADÍSTICAS
**Nuevo archivo:** `src/negocio/ReportGenerator.java`

**4 Comandos nuevos:**

#### REPACT - Reporte de Actividades por Tipo
**Sintaxis:** `REPACT`  
**Salida:** Gráfico de barras + total
```
📊 Actividades por Tipo
■■■■■■■■■■■ Ciclismo (25)
■■■■■■■ Natación (15)
■■■■ Yoga (8)
TOTAL: 48 actividades
```

#### REPUSU - Reporte de Usuarios por Rol
**Sintaxis:** `REPUSU`  
**Salida:** Gráficos circulares + total
```
👥 Usuarios por Rol
⭕ Admin (10%)
⭕ Instructor (35%)
⭕ Alumno (55%)
TOTAL: 120 usuarios
```

#### REPVEH - Reporte de Vehículos por Tipo
**Sintaxis:** `REPVEH`  
**Salida:** Gráfico de barras + total

#### REPPAG - Reporte de Pagos por Método
**Sintaxis:** `REPPAG`  
**Salida:** Tabla + estadísticas
```
💰 Pagos por Método
Método          Cantidad    Total (Bs.)
Efectivo        45          12,500.00
Transferencia   30          8,750.00
TOTAL PAGOS: 75
TOTAL RECAUDADO: Bs. 21,250.00
```

**Métodos agregados a DAOs:**
- `ActividadDAO.contarPorTipo()` → Map<String, Integer>
- `UsuarioDAO.contarPorRol()` → Map<String, Integer>
- `VehiculoDAO.contarPorTipo()` → Map<String, Integer>
- `PagoDAO.estadisticasPorMetodo()` → Map<String, Object[]>

### 5. TOTAL DE COMANDOS
**Antes:** 45 comandos (1 AYUDA + 44 CRUD)  
**Ahora:** 49 comandos (1 AYUDA + 44 CRUD + 4 REPORTES)

---

## PRUEBAS

### Prueba 1: Cambio de modo SMTP
```bash
# Cambiar a modo SOCKET
sed -i 's/MAIL_SENDER_TYPE=.*/MAIL_SENDER_TYPE=SOCKET/' .env
docker-compose restart app

# Cambiar a modo MAILERSEND
sed -i 's/MAIL_SENDER_TYPE=.*/MAIL_SENDER_TYPE=MAILERSEND/' .env
docker-compose restart app
```

**Salida esperada en logs:**
```
MAILERSEND: "✓ Correo enviado exitosamente vía MailerSend API"
SOCKET: "✓ Correo enviado exitosamente vía SMTP Socket (puerto 25)"
```

### Prueba 2: Comando AYUDA
**Email a:** grupo17sa@tecnoweb.org.bo  
**Asunto:** `AYUDA`  
**Esperar:** HTML moderno con tabla de 49 comandos organizados por categorías

### Prueba 3: Reportes con Gráficos
**Email 1:** Asunto `REPACT`  
**Esperar:** Gráfico de barras de actividades por tipo

**Email 2:** Asunto `REPUSU`  
**Esperar:** Gráficos circulares de usuarios por rol

**Email 3:** Asunto `REPVEH`  
**Esperar:** Gráfico de barras de vehículos por tipo

**Email 4:** Asunto `REPPAG`  
**Esperar:** Tabla de pagos con totales

### Prueba 4: Comandos Existentes
**Email:** Asunto `LISROL["*"]`  
**Esperar:** Tabla HTML con roles del sistema (Admin, Instructor, Alumno)

---

## RESUMEN TÉCNICO
- **Archivos creados:** 2 (HTMLResponseBuilder.java, ReportGenerator.java)
- **Archivos modificados:** 7 (Main.java, CommandProcessor.java, 4 DAOs, .env, docker-entrypoint.sh)
- **Archivos eliminados:** 9 (GmailSMTPClient.java + 8 docs)
- **Nuevas funcionalidades:** 4 comandos de reportes con gráficos
- **Mejoras de UI:** Respuestas HTML modernas y minimalistas
- **Simplificación:** Arquitectura dual (antes triple)