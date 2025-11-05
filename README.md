# Sistema de Gestión vía E-Mail - Proyecto 1

Sistema básico que procesa solicitudes mediante correo electrónico para gestionar una tabla `Persona`.

## Arquitectura del Proyecto

```
Cliente (cualquier correo)
    ↓ envía: LISPER["*"]
    ↓
Sistema Java (lee POP3)
    ↓ parsea comando
    ↓ ejecuta SQL
    ↓ responde SMTP
    ↓
Cliente recibe respuesta
```

## Entorno Local de Desarrollo

### Servicios Docker:
- **PostgreSQL** (puerto 5433): Base de datos
- **MailHog** (puertos 1025/8025): Servidor SMTP de prueba con interfaz web

### Configuración:

```bash
# 1. Levantar servicios
docker-compose up -d

# 2. Verificar que estén corriendo
docker-compose ps

# 3. Probar el sistema completo (SIN necesidad de correos reales)
java -cp "bin:lib/*" SimuladorComandos

# 4. Ver resultados en Web UI
http://localhost:8025

# 5. Ejecutar sistema con servidor real (cuando tengas credenciales)
./run.sh
```

## Comandos Disponibles

### AYUDA
Muestra todos los comandos disponibles con ejemplos.

**Formato:**
```
Subject: AYUDA
```

**Respuesta:** Tabla HTML con todos los comandos

---

### LISPER - Listar Personas
Lista registros de la tabla persona según patrón.

**Formato:**
```
Subject: LISPER["*"]
Subject: LISPER["Juan"]
```

**Parámetros:**
- `*` = Todos los registros
- `patron` = Busca por nombre que contenga el patrón

**Respuesta:** Lista de personas en formato HTML

---

### INSPER - Insertar Persona
Registra una nueva persona.

**Formato:**
```
Subject: INSPER["Juan","Pérez"]
```

**Parámetros:**
1. nombre (string)
2. apellido (string)

**Respuesta:** Confirmación de éxito o error

---

### MODPER - Modificar Persona
Actualiza datos de una persona existente.

**Formato:**
```
Subject: MODPER["1","Carlos","Martínez"]
```

**Parámetros:**
1. id (número)
2. nombre (string)
3. apellido (string)

**Respuesta:** Confirmación de éxito o error

---

### DELPER - Eliminar Persona
Elimina una persona por ID.

**Formato:**
```
Subject: DELPER["1"]
```

**Parámetros:**
1. id (número)

**Respuesta:** Confirmación de éxito o error

---

## Validaciones Implementadas

1. **Comando existe**: Verifica que el comando esté en la lista válida
2. **Cantidad de parámetros**: Valida que coincida con lo esperado
3. **Tipo de parámetros**: Verifica números donde corresponde
4. **Registros existentes**: Para UPDATE/DELETE verifica que exista el ID

## Estructura del Proyecto

```
mail-system-basic/
├── src/
│   ├── conexion/
│   │   ├── POP3Client.java      # Lee correos
│   │   ├── SMTPClient.java       # Envía correos
│   │   └── CommandParser.java    # Parsea comandos
│   ├── datos/
│   │   ├── DBConnection.java     # Conexión PostgreSQL
│   │   └── PersonaDAO.java       # CRUD Persona
│   ├── negocio/
│   │   └── PersonaService.java   # Lógica de negocio
│   ├── servicio/
│   │   └── MailService.java      # Loop principal
│   └── Main.java                  # Punto de entrada
├── lib/
│   ├── javax.mail-1.6.2.jar
│   ├── activation-1.1.1.jar
│   └── postgresql-42.2.18.jar
├── config.properties              # Configuración
├── docker-compose.yml
├── init.sql
└── README.md
```

## Configuración para Servidor Real

Cuando esté listo para conectar al servidor de la facultad:

**config.properties:**
```properties
# Servidor real
mail.pop3.host=mail.tecnoweb.org.bo
mail.pop3.user=grupo01sa@tecnoweb.org.bo
mail.pop3.password=grup001grup001*

mail.smtp.host=mail.tecnoweb.org.bo
mail.smtp.user=grupo01sa@tecnoweb.org.bo

db.host=www.tecnoweb.org.bo
db.name=db_grupo01sa
db.user=grupo01sa
db.password=grup001grup001*
```

## Pruebas

### 1. Usando MailHog (local):

1. Accede a http://localhost:8025
2. Envía correo de prueba con el comando
3. El sistema procesa y responde
4. Verifica respuesta en MailHog UI

### 2. Usando cliente real (Gmail, Outlook):

Configura el servidor SMTP local o remoto y envía desde tu correo personal.

## Troubleshooting

**Error de conexión a DB:**
```bash
docker-compose down
docker-compose up -d
```

**Ver logs del sistema:**
```bash
tail -f logs/sistema.log
```

**Probar conexión manual a PostgreSQL:**
```bash
docker exec -it mail_system_db psql -U admin -d db_mail_system
\dt
SELECT * FROM persona;
```
