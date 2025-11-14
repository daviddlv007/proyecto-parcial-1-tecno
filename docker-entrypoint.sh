#!/bin/bash
# Genera config.properties desde variables de entorno
# Usado por Docker para inyectar configuración dinámica

cat > /app/config.properties << EOF
# Configuración generada desde variables de entorno
# Grupo 17SA - Docker Environment

# ==== CORREO SMTP (Enviar respuestas) ====
mail.sender.type=${MAIL_SENDER_TYPE}
mail.smtp.host=${MAIL_SMTP_HOST:-mail.tecnoweb.org.bo}
mail.smtp.user=${MAIL_SMTP_USER}

# ==== MailerSend API (alternativa a SMTP) ====
mailersend.api.token=${MAILERSEND_API_TOKEN:-}
mailersend.from.email=${MAILERSEND_FROM_EMAIL:-}
mailersend.from.name=${MAILERSEND_FROM_NAME:-Grupo 17SA}

# ==== CORREO POP3 (Leer correos entrantes) ====
mail.pop3.host=${MAIL_POP3_HOST}
mail.pop3.port=${MAIL_POP3_PORT}
mail.pop3.user=${MAIL_POP3_USER}
mail.pop3.password=${MAIL_POP3_PASSWORD}

# ==== BASE DE DATOS ====
db.host=${DB_HOST}
db.port=${DB_PORT}
db.name=${POSTGRES_DB}
db.user=${POSTGRES_USER}
db.password=${POSTGRES_PASSWORD}

# ==== SISTEMA ====
mail.check.interval=${MAIL_CHECK_INTERVAL:-5000}
mail.max.process=${MAIL_MAX_PROCESS:-10}
EOF

echo "✓ config.properties generado desde variables de entorno"
cat /app/config.properties

# Esperar a que servidor PostgreSQL remoto esté disponible
echo ""
echo "Conectando a servidor PostgreSQL (${DB_HOST}:${DB_PORT})..."
until nc -z ${DB_HOST} ${DB_PORT}; do
  echo "  Esperando conexión..."
  sleep 2
done
echo "✓ Servidor PostgreSQL disponible"

# Ejecutar script de inicialización (DROP + CREATE + INSERT)
echo ""
echo "Inicializando base de datos con datos de prueba..."
PGPASSWORD=${POSTGRES_PASSWORD} psql -h ${DB_HOST} -U ${POSTGRES_USER} -d ${POSTGRES_DB} -f /app/db_schema.sql

if [ $? -eq 0 ]; then
  echo ""
  echo "✓ Base de datos inicializada correctamente"
else
  echo ""
  echo "⚠ Error al inicializar base de datos"
  exit 1
fi

echo ""
echo "════════════════════════════════════════════════════════════"
echo "   Iniciando aplicación Java..."
echo "════════════════════════════════════════════════════════════"

# Ejecutar la aplicación Java
exec java -cp "/app/bin:/app/lib/*" Main
