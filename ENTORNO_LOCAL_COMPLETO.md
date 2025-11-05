# 🎉 ENTORNO 100% LOCAL FUNCIONAL

## ✅ Lo que Tienes Ahora

**Ubicación:** `/home/ubuntu/proyectos/proyecto-parcial-1-tecno/mail-system-basic/`

### Servicios Activos:

1. **PostgreSQL** (puerto 5433)
   - Base de datos: `db_mail_system`
   - Tabla: `persona` con 3 registros
   - Acceso: `admin/admin123`

2. **MailHog** (puertos 1025 y 8025)
   - SMTP Server: `localhost:1025`
   - **Web UI: http://localhost:8025** ← Abre esto en tu navegador
   - Captura todos los correos enviados

---

## 🚀 Cómo Probar el Sistema Localmente

### Opción 1: Test Rápido de SMTP

```bash
cd /home/ubuntu/proyectos/proyecto-parcial-1-tecno/mail-system-basic
java -cp "bin:lib/*" TestMailHog
```

Luego abre: http://localhost:8025 y verás el correo enviado.

---

### Opción 2: Simulación Completa (RECOMENDADO)

```bash
java -cp "bin:lib/*" SimuladorComandos
```

**Esto ejecuta 7 comandos completos:**
1. AYUDA
2. LISPER["*"]
3. INSPER["Pedro","López"]
4. LISPER["Pedro"]
5. MODPER["4","Pedro Antonio","López García"]
6. LISPER["*"]
7. DELPER["4"]

**Ve los resultados en:** http://localhost:8025

Verás 7 correos con respuestas HTML completas, exactamente como se enviarían en producción.

---

## 🔍 Ver Contenido de la Base de Datos

```bash
docker exec mail_system_db psql -U admin -d db_mail_system -c "SELECT * FROM persona;"
```

---

## 📊 Flujo del Sistema

### En Local (Testing):
```
SimuladorComandos.java
    ↓ Parsea comando: LISPER["*"]
    ↓ PersonaService.listar("*")
    ↓ PersonaDAO SELECT en PostgreSQL
    ↓ HTMLGenerator crea tabla HTML
    ↓ SMTPClient.sendEmail()
    ↓
MailHog (localhost:1025)
    ↓ Almacena correo
    ↓
Web UI (localhost:8025)
    ↓ Visualizas el resultado
```

### En Producción (Servidor Real):
```
Cliente envía correo
    ↓ mail.tecnoweb.org.bo (SMTP)
    ↓
Sistema Java (loop infinito)
    ↓ POP3Client.getMessages() cada 10seg
    ↓ Parsea Subject
    ↓ PersonaService procesa
    ↓ PersonaDAO consulta DB
    ↓ HTMLGenerator respuesta
    ↓ SMTPClient.sendEmail()
    ↓
Cliente recibe respuesta
```

---

## 🔄 Migrar a Servidor Real

**1. Edita `config.properties`:**

```properties
# Cambiar de MailHog a servidor real
mail.smtp.host=mail.tecnoweb.org.bo
mail.smtp.port=25
mail.smtp.user=grupoXXsa@tecnoweb.org.bo

mail.pop3.host=mail.tecnoweb.org.bo
mail.pop3.port=110
mail.pop3.user=grupoXXsa@tecnoweb.org.bo
mail.pop3.password=TU_PASSWORD
```

**2. Ejecuta el sistema:**

```bash
./run.sh
```

El sistema empezará a leer correos reales cada 10 segundos.

---

## 🎯 Ventajas del Entorno Local

✅ **Sin dependencias externas** - Todo funciona offline
✅ **Testing rápido** - No necesitas enviar correos reales
✅ **Visualización inmediata** - Web UI muestra correos al instante
✅ **Debugging fácil** - Ves exactamente qué se envía
✅ **Migración simple** - Solo cambiar 4 líneas de config
✅ **Base de datos persistente** - Los datos se mantienen entre reinicios

---

## 📁 Archivos de Testing Creados

- `TestConexion.java` - Prueba DB y CRUD
- `TestMailHog.java` - Prueba envío simple
- **`SimuladorComandos.java`** - Simula flujo completo ⭐

---

## 🛠️ Comandos Útiles

### Ver servicios corriendo:
```bash
docker-compose ps
```

### Reiniciar servicios:
```bash
docker-compose restart
```

### Ver logs de MailHog:
```bash
docker logs -f mail_system_mailhog
```

### Limpiar base de datos:
```bash
docker exec mail_system_db psql -U admin -d db_mail_system -c "TRUNCATE persona RESTART IDENTITY;"
```

### Detener todo:
```bash
docker-compose down
```

### Levantar todo nuevamente:
```bash
docker-compose up -d
```

---

## ✅ Checklist de Verificación

- [✅] PostgreSQL corriendo (puerto 5433)
- [✅] MailHog corriendo (puertos 1025, 8025)
- [✅] Código Java compilado
- [✅] Test de conexión DB pasando
- [✅] Test de envío SMTP funcionando
- [✅] Simulador de comandos funcionando
- [✅] Web UI accesible en localhost:8025

---

## 🎓 Para tu Examen

1. **Desarrolla localmente** con el simulador
2. **Prueba rápidamente** sin enviar correos reales
3. **Visualiza respuestas** en tiempo real
4. **Cuando esté listo**, cambia config y conecta al servidor real
5. **Defiende** mostrando http://localhost:8025 con correos procesados

---

## 🔥 Próximo Paso

**Ejecuta el simulador y abre la Web UI:**

```bash
# Terminal 1
cd /home/ubuntu/proyectos/proyecto-parcial-1-tecno/mail-system-basic
java -cp "bin:lib/*" SimuladorComandos

# Navegador
http://localhost:8025
```

¡Verás los 7 correos con respuestas HTML profesionales! 🎉
