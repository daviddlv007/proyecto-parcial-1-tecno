# 🚀 GUÍA RÁPIDA - Proyecto Funcional

## ✅ Lo que se ha completado

Has creado un proyecto **completamente funcional** con:

### 📦 Entorno Local
- **PostgreSQL** en Docker (puerto 5433)
- Base de datos `db_mail_system` con tabla `persona`
- 3 registros de prueba insertados

### 💻 Código Fuente Compilado
- ✅ Capa de datos (DAO) con CRUD completo
- ✅ Capa de negocio con validaciones
- ✅ Parser de comandos del Subject
- ✅ Generador de respuestas HTML
- ✅ Loop infinito que procesa correos

### 📋 5 Comandos Implementados
1. **AYUDA** - Muestra tabla de comandos
2. **LISPER** - Lista personas
3. **INSPER** - Inserta persona
4. **MODPER** - Modifica persona
5. **DELPER** - Elimina persona

---

## 🎯 Próximos Pasos

### Opción 1: Probar Localmente (Sin Servidor Real)

Para probar sin conectar al servidor de la facultad:

**Problema actual:** No tienes MailHog (servidor SMTP local) instalado porque Docker tuvo problemas.

**Solución rápida:**
```bash
# Instalar MailHog manualmente
sudo apt-get update
sudo apt-get install -y mailhog

# O descargarlo directamente
wget https://github.com/mailhog/MailHog/releases/download/v1.0.1/MailHog_linux_amd64
chmod +x MailHog_linux_amd64
./MailHog_linux_amd64 &
```

Luego accede a http://localhost:8025 para ver los correos.

---

### Opción 2: Conectar al Servidor Real (Recomendado)

Edita `config.properties`:

```properties
# ==== CORREO POP3 (Leer correos entrantes) ====
mail.pop3.host=mail.tecnoweb.org.bo
mail.pop3.port=110
mail.pop3.user=grupoXXsa@tecnoweb.org.bo  # Tu grupo
mail.pop3.password=tu_password

# ==== CORREO SMTP (Enviar respuestas) ====
mail.smtp.host=mail.tecnoweb.org.bo
mail.smtp.port=25
mail.smtp.user=grupoXXsa@tecnoweb.org.bo
mail.smtp.auth=false
mail.smtp.starttls=false

# Base de datos (mantener local)
db.host=localhost
db.port=5433
db.name=db_mail_system
db.user=admin
db.password=admin123
```

---

## 🏃 Ejecutar el Sistema

```bash
cd /home/ubuntu/proyectos/mail-system-basic

# 1. Asegurarse que PostgreSQL esté corriendo
docker-compose up -d

# 2. Ejecutar el sistema
./run.sh
```

Deberías ver:

```
════════════════════════════════════════════════════════════
   SISTEMA DE GESTIÓN VÍA E-MAIL - PROYECTO 1
   Tecnología Web - INF513
════════════════════════════════════════════════════════════

Conectando a base de datos: jdbc:postgresql://localhost:5433/db_mail_system
✓ Conexión exitosa a PostgreSQL
✓ SMTP Client configurado
╔════════════════════════════════════════════════════════════╗
║   SISTEMA DE GESTIÓN VÍA E-MAIL - INICIADO                ║
╚════════════════════════════════════════════════════════════╝

⏰ Intervalo de chequeo: 10 segundos
📨 Esperando correos entrantes...
⌨️  Presiona Ctrl+C para detener
```

---

## 📧 Probar con Correos Reales

### Desde tu Gmail/Outlook:

**1. Envía un correo a:** `grupoXXsa@tecnoweb.org.bo`

**Subject:** `AYUDA`

**Resultado:** Recibirás una tabla HTML con todos los comandos


**2. Listar todas las personas:**

**Subject:** `LISPER["*"]`

**Resultado:** Recibirás una tabla con Juan, María y Carlos


**3. Insertar una persona:**

**Subject:** `INSPER["Pedro","López"]`

**Resultado:** Confirmación de que se insertó


**4. Modificar persona ID=1:**

**Subject:** `MODPER["1","Juan Carlos","Pérez Ruiz"]`


**5. Eliminar persona ID=3:**

**Subject:** `DELPER["3"]`

---

## 🔍 Verificar la Base de Datos

```bash
# Ver todas las personas
docker exec mail_system_db psql -U admin -d db_mail_system -c "SELECT * FROM persona;"

# Insertar manualmente
docker exec mail_system_db psql -U admin -d db_mail_system -c "INSERT INTO persona (nombre, apellido) VALUES ('Test', 'Usuario');"
```

---

## ⚠️ Troubleshooting

### Error: Connection refused (POP3)
- Verifica credenciales en `config.properties`
- El servidor de la facultad puede estar bloqueado/caído

### Error: Can't connect to database
```bash
# Reiniciar PostgreSQL
docker-compose down
docker-compose up -d

# Esperar 5 segundos
sleep 5

# Verificar
docker exec mail_system_db psql -U admin -d db_mail_system -c "SELECT 1;"
```

### Ver logs en tiempo real
```bash
# En otra terminal
docker logs -f mail_system_db
```

---

## 📚 Arquitectura del Proyecto

```
┌─────────────────────────────────────────────────────┐
│  Cliente (Gmail, Outlook, etc)                      │
│  Envía: LISPER["*"]                                 │
└─────────────────┬───────────────────────────────────┘
                  │ SMTP
                  ▼
┌─────────────────────────────────────────────────────┐
│  mail.tecnoweb.org.bo (Servidor POP3/SMTP)          │
└─────────────────┬───────────────────────────────────┘
                  │ POP3 polling cada 10seg
                  ▼
┌─────────────────────────────────────────────────────┐
│  Sistema Java (WSL2)                                │
│  ┌─────────────────────────────────────┐            │
│  │ MailService (Loop infinito)         │            │
│  │  └─ Lee correo                      │            │
│  │  └─ CommandParser.parse()           │            │
│  │  └─ PersonaService.listar()         │            │
│  │  └─ PersonaDAO.listar()             │            │
│  └──────────────┬──────────────────────┘            │
└─────────────────┼───────────────────────────────────┘
                  │ JDBC
                  ▼
┌─────────────────────────────────────────────────────┐
│  PostgreSQL (Docker Container)                      │
│  puerto 5433                                        │
│  tabla: persona(id, nombre, apellido)               │
└─────────────────┬───────────────────────────────────┘
                  │
                  ▼
            Respuesta HTML
                  │
                  ▼
┌─────────────────────────────────────────────────────┐
│  Cliente recibe correo con resultado                │
└─────────────────────────────────────────────────────┘
```

---

## 🎓 Para el Examen

Este proyecto es una **base completa y funcional**. Puedes:

1. **Usar tal cual** para entender la arquitectura
2. **Agregar tu lógica de negocio** (en vez de Persona, usa tu entidad del negocio elegido)
3. **Ampliar comandos** según los 8 casos de uso requeridos
4. **Documentar** con diagramas UML (casos de uso, secuencia, clases, BD)

### Pasar de Persona a tu Negocio:

Si quieres gestionar "Productos":

1. Crea tabla `producto` en `init.sql`
2. Copia `Persona.java` → `Producto.java`
3. Copia `PersonaDAO.java` → `ProductoDAO.java`
4. Copia `PersonaService.java` → `ProductoService.java`
5. Actualiza `MailService.java` para usar ProductoService
6. Cambia comandos: LISPER → LISPRO, INSPER → INSPRO, etc.

---

## ✅ Checklist Pre-Defensa

- [ ] Sistema corre sin errores
- [ ] Comando AYUDA funciona
- [ ] Los 5 comandos responden correctamente
- [ ] Base de datos normalizada y documentada
- [ ] Documento técnico (análisis, diseño, implementación)
- [ ] Diagramas UML (casos de uso, secuencia, clases)
- [ ] Script de creación de BD documentado
- [ ] README con instrucciones de instalación
- [ ] Código comentado y estructurado

---

## 📁 Estructura Final del Proyecto

```
mail-system-basic/
├── src/
│   ├── conexion/
│   │   ├── CommandParser.java    ✅ Parsea Subject
│   │   ├── POP3Client.java       ✅ Lee correos
│   │   └── SMTPClient.java       ✅ Envía respuestas
│   ├── datos/
│   │   ├── DBConnection.java     ✅ Singleton conexión
│   │   ├── Persona.java          ✅ POJO
│   │   └── PersonaDAO.java       ✅ CRUD SQL
│   ├── negocio/
│   │   ├── HTMLGenerator.java    ✅ Respuestas HTML
│   │   └── PersonaService.java   ✅ Validaciones
│   ├── servicio/
│   │   └── MailService.java      ✅ Loop principal
│   └── Main.java                  ✅ Entry point
├── lib/                           ✅ JARs (mail, postgres)
├── bin/                           ✅ Clases compiladas
├── config.properties              ✅ Configuración
├── docker-compose.yml             ✅ PostgreSQL
├── init.sql                       ✅ Schema + datos
├── compile.sh                     ✅ Script compilación
├── run.sh                         ✅ Script ejecución
└── README.md                      ✅ Documentación
```

¡Todo listo para conectar al servidor real y empezar a enviar correos! 🎉
