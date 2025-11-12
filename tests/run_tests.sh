#!/bin/bash
# Script de prueba completo del sistema

set -e

echo "════════════════════════════════════════════════════════"
echo "   TESTS - Mail System"
echo "════════════════════════════════════════════════════════"
echo ""

# Verificar que los servicios estén corriendo
echo "1️⃣  Verificando servicios..."
docker-compose ps | grep -q "Up" || {
    echo "❌ Error: Servicios no están corriendo"
    echo "   Ejecuta: docker-compose up -d"
    exit 1
}
echo "✅ Servicios activos"
echo ""

# Esperar a que los servicios estén listos
echo "2️⃣  Esperando servicios..."
sleep 5
echo "✅ Servicios listos"
echo ""

# Ejecutar simulador de comandos
echo "3️⃣  Ejecutando simulador de comandos..."
docker-compose exec -T mail_app java -cp /app/bin:/app/lib/* SimuladorComandos
echo ""

# Verificar base de datos
echo "4️⃣  Verificando datos en PostgreSQL..."
docker-compose exec -T postgres psql -U admin -d db_mail_system -c "\
SELECT COUNT(*) as total_registros FROM persona;"
echo ""

echo "════════════════════════════════════════════════════════"
echo "✅ TESTS COMPLETADOS"
echo "════════════════════════════════════════════════════════"
echo ""
echo "📧 Ver correos enviados: http://localhost:8025"
echo ""
