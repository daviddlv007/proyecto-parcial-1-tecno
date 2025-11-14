#!/bin/bash
# Script auxiliar para repoblar la base de datos con datos iniciales
# Uso: ./repoblar.sh

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

DB_HOST="mail.tecnoweb.org.bo"
DB_PORT="5432"
DB_NAME="db_grupo17sa"
DB_USER="grupo17sa"
DB_PASS="grup017grup017*"

echo ""
echo -e "${YELLOW}════════════════════════════════════════════════════════════${NC}"
echo -e "${YELLOW}   REPOBLACIÓN DE BASE DE DATOS - Grupo 17SA${NC}"
echo -e "${YELLOW}════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "${YELLOW}🔄 Ejecutando db_schema.sql...${NC}"
PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -f db_schema.sql 2>&1 | grep -v "WARNING" | grep -v "DETAIL" | grep -v "HINT"

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Base de datos repoblada exitosamente${NC}"
    echo ""
    echo -e "${YELLOW}📊 Registros actuales:${NC}"
    PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -c "
    SELECT 
        'Tabla' as tipo, 'Cantidad' as valor
    UNION ALL SELECT '─────────────', '────────'
    UNION ALL SELECT 'usuarios', COUNT(*)::text FROM usuario
    UNION ALL SELECT 'vehículos', COUNT(*)::text FROM vehiculo
    UNION ALL SELECT 'actividades', COUNT(*)::text FROM actividad
    UNION ALL SELECT 'sesiones', COUNT(*)::text FROM sesion
    UNION ALL SELECT 'inscripciones', COUNT(*)::text FROM inscripcion
    UNION ALL SELECT 'pagos', COUNT(*)::text FROM pago;
    " 2>/dev/null | grep -v "WARNING" | grep -v "DETAIL" | grep -v "HINT"
    echo ""
    echo -e "${GREEN}✓ Sistema restaurado al estado inicial${NC}"
    echo -e "${GREEN}  • 14 usuarios (1 admin, 3 instructores, 8 alumnos, 2 auxiliares)${NC}"
    echo -e "${GREEN}  • 14 vehículos (4 autos, 3 camionetas, 4 motos, 3 buses)${NC}"
    echo -e "${GREEN}  • 14 actividades (distribuidas en 4 tipos)${NC}"
    echo -e "${GREEN}  • 16 inscripciones${NC}"
    echo -e "${GREEN}  • 16 pagos (distribuidos en 4 métodos)${NC}"
else
    echo ""
    echo -e "${RED}✗ Error al repoblar base de datos${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}════════════════════════════════════════════════════════════${NC}"
echo ""
