#!/bin/bash
# ================================================================
# SCRIPT DE PRUEBAS 100% FUNCIONALES - 65 COMANDOS
# Estrategia: INSERT → LISTAR → MODIFY → DELETE → REPORTES
# 2 registros por tabla, el 2do se puede eliminar sin dependencias
# Incluye 4 reportes: REPACT, REPUSU, REPVEH, REPPAG
# ================================================================

set -e

echo "╔════════════════════════════════════════════════════════════╗"
echo "║   PRUEBA 100% - 65 COMANDOS (CRUD + AYUDA + 4 REPORTES)   ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

DB_HOST="www.tecnoweb.org.bo"
DB_PORT="5432"
DB_NAME="db_grupo17sa"
DB_USER="grupo17sa"
DB_PASS="grup017grup017*"

# [1/6] Verificar Docker
echo -e "${YELLOW}[1/6]${NC} Verificando imagen Docker..."
if docker images | grep -q "mail-sistema-grupo17sa"; then
    echo -e "${GREEN}✓${NC} Imagen encontrada"
else
    echo -e "${YELLOW}⚠${NC}  Compilando imagen..."
    docker build -t mail-sistema-grupo17sa .
fi
echo ""

# [2/6] Limpiar BD
echo -e "${YELLOW}[2/6]${NC} Limpiando base de datos..."
PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -f limpiar_bd.sql > /dev/null 2>&1
echo -e "${GREEN}✓${NC} BD limpia (3 usuarios, 1 vehículo, 1 actividad base)"
echo ""

# [3/6] Conteo inicial
echo -e "${YELLOW}[3/6]${NC} Registros iniciales:"
PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -c "
SELECT 
    (SELECT COUNT(*) FROM rol) as rol,
    (SELECT COUNT(*) FROM tipo_vehiculo) as tvh,
    (SELECT COUNT(*) FROM tipo_actividad) as tac,
    (SELECT COUNT(*) FROM tipo_pago) as tpg,
    (SELECT COUNT(*) FROM metodo_pago) as mpg,
    (SELECT COUNT(*) FROM usuario) as usr,
    (SELECT COUNT(*) FROM vehiculo) as veh,
    (SELECT COUNT(*) FROM actividad) as act;
" 2>/dev/null | grep -v "WARNING" | grep -v "DETAIL" | grep -v "HINT"
echo ""

# [4/6] Generar TestComandos.java
echo -e "${YELLOW}[4/6]${NC} Generando TestComandos.java..."

cat > /tmp/TestComandos.java << 'JAVA_EOF'
import conexion.CommandParser;
import negocio.CommandProcessor;

public class TestComandos {
    public static void main(String[] args) {
        CommandProcessor processor = new CommandProcessor();
        int exitosos = 0;
        int fallidos = 0;
        int total = 0;
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  PRUEBA 100% - FLUJO: INSERT→LIST→MODIFY→DELETE→REPORT   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // ==================== FASE 1: INSERCIONES (22 comandos) ====================
        System.out.println("═══ FASE 1: INSERCIONES (1 AYUDA + 21 INSERT) ═══\n");
        
        // [1] AYUDA
        total++; System.out.print("[" + total + "] AYUDA... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("AYUDA"));
            if (resp.contains("COMANDOS DISPONIBLES")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // CATÁLOGOS: Insertar 2 registros en cada tabla (IDs 4 y 5)
        // [2] INSROL #1
        total++; System.out.print("[" + total + "] INSROL #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSROL[\"Supervisor\",\"Supervisor de área\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [3] INSROL #2
        total++; System.out.print("[" + total + "] INSROL #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSROL[\"Secretario\",\"Personal administrativo\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [4] INSTVH #1
        total++; System.out.print("[" + total + "] INSTVH #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSTVH[\"Bus\",\"Bus escolar\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [5] INSTVH #2
        total++; System.out.print("[" + total + "] INSTVH #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSTVH[\"Camión\",\"Camión pesado\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [6] INSTAC #1
        total++; System.out.print("[" + total + "] INSTAC #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSTAC[\"Simulador\",\"Práctica en simulador\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [7] INSTAC #2
        total++; System.out.print("[" + total + "] INSTAC #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSTAC[\"Virtual\",\"Clase virtual online\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [8] INSTPG #1
        total++; System.out.print("[" + total + "] INSTPG #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSTPG[\"Beca\",\"Pago con beca\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [9] INSTPG #2
        total++; System.out.print("[" + total + "] INSTPG #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSTPG[\"Crédito\",\"Pago a crédito\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [10] INSMPG #1
        total++; System.out.print("[" + total + "] INSMPG #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSMPG[\"QR\",\"Código QR\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [11] INSMPG #2
        total++; System.out.print("[" + total + "] INSMPG #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("INSMPG[\"Cheque\",\"Pago con cheque\"]"));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // PRINCIPALES: Insertar 2 registros (usar IDs base + nuevos)
        // [12] INSUSU #1 (ID 4)
        total++; System.out.print("[" + total + "] INSUSU #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSUSU[\"Pedro\",\"Mamani\",\"1990-05-15\",\"M\",\"CI\",\"4444444\",\"pedro@test.com\",\"70444444\",\"Zona Este\",\"pass123\",\"3\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [13] INSUSU #2 (ID 5) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSUSU #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSUSU[\"María\",\"López\",\"1992-08-20\",\"F\",\"CI\",\"5555555\",\"maria@test.com\",\"70555555\",\"Zona Oeste\",\"pass456\",\"3\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [14] INSVEH #1 (ID 2)
        total++; System.out.print("[" + total + "] INSVEH #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSVEH[\"ABC-123\",\"Toyota\",\"Corolla\",\"2020\",\"1\",\"Disponible\",\"5\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [15] INSVEH #2 (ID 3) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSVEH #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSVEH[\"XYZ-789\",\"Nissan\",\"Sentra\",\"2021\",\"1\",\"Disponible\",\"5\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [16] INSACT #1 (ID 2)
        total++; System.out.print("[" + total + "] INSACT #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSACT[\"Manejo Nocturno\",\"Prácticas nocturnas\",\"2\",\"3\",\"2\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [17] INSACT #2 (ID 3) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSACT #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSACT[\"Estacionamiento\",\"Técnicas de estacionamiento\",\"2\",\"2\",\"1\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [18] INSSES #1 (ID 1) - act_id=1, veh_id=1, instructor_id=2
        total++; System.out.print("[" + total + "] INSSES #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSSES[\"1\",\"2025-12-15\",\"09:00\",\"11:00\",\"2\",\"1\",\"Aula 101\",\"20\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { 
                System.out.println("✗ Respuesta: " + resp); 
                fallidos++; 
            }
        } catch (Exception e) { 
            System.out.println("✗ EXCEPCIÓN: " + e.getMessage()); 
            e.printStackTrace();
            fallidos++; 
        }
        
        // [19] INSSES #2 (ID 2) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSSES #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSSES[\"2\",\"2025-12-16\",\"14:00\",\"16:00\",\"3\",\"2\",\"Aula 102\",\"15\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { 
                System.out.println("✗ Respuesta: " + resp); 
                fallidos++; 
            }
        } catch (Exception e) { 
            System.out.println("✗ EXCEPCIÓN: " + e.getMessage()); 
            e.printStackTrace();
            fallidos++; 
        }
        
        // [20] INSINS #1 (ID 1) - alumno_id=3, sesion_id=1
        total++; System.out.print("[" + total + "] INSINS #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSINS[\"3\",\"1\",\"1\",\"500.00\",\"Confirmada\",\"1\",\"Primera inscripción\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { 
                System.out.println("✗ Respuesta: " + resp); 
                fallidos++; 
            }
        } catch (Exception e) { 
            System.out.println("✗ EXCEPCIÓN: " + e.getMessage()); 
            e.printStackTrace();
            fallidos++; 
        }
        
        // [21] INSINS #2 (ID 2) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSINS #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSINS[\"4\",\"2\",\"1\",\"600.00\",\"Pendiente\",\"2\",\"Segunda inscripción\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { 
                System.out.println("✗ Respuesta: " + resp); 
                fallidos++; 
            }
        } catch (Exception e) { 
            System.out.println("✗ EXCEPCIÓN: " + e.getMessage()); 
            e.printStackTrace();
            fallidos++; 
        }
        
        // [22] INSPAG #1 (ID 1) - alumno_id=3, inscripcion_id=1
        total++; System.out.print("[" + total + "] INSPAG #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSPAG[\"3\",\"1\",\"500.00\",\"1\",\"COMP-001\",\"Primer pago\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { 
                System.out.println("✗ Respuesta: " + resp); 
                fallidos++; 
            }
        } catch (Exception e) { 
            System.out.println("✗ EXCEPCIÓN: " + e.getMessage()); 
            e.printStackTrace();
            fallidos++; 
        }
        
        // [23] INSPAG #2 (ID 2) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSPAG #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSPAG[\"4\",\"2\",\"600.00\",\"2\",\"COMP-002\",\"Segundo pago\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { 
                System.out.println("✗ Respuesta: " + resp); 
                fallidos++; 
            }
        } catch (Exception e) { 
            System.out.println("✗ EXCEPCIÓN: " + e.getMessage()); 
            e.printStackTrace();
            fallidos++; 
        }        // [19] INSSES #2 (ID 2) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSSES #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSSES[\"2\",\"2025-12-20\",\"14:00\",\"16:00\",\"2\",\"2\",\"Pista Práctica\",\"15\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [20] INSINS #1 (ID 1) - alumno_id=3, sesion_id=1, tipo_pago_id=1, monto=500, estado=Confirmada, curso_id=1, obs=...
        total++; System.out.print("[" + total + "] INSINS #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSINS[\"3\",\"1\",\"1\",\"500.00\",\"Confirmada\",\"1\",\"Primera inscripción\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [21] INSINS #2 (ID 2) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSINS #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSINS[\"4\",\"2\",\"1\",\"600.00\",\"Pendiente\",\"2\",\"Segunda inscripción\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [22] INSPAG #1 (ID 1) - alumno_id=3, inscripcion_id=1, monto=500, metodo_pago_id=1, comprobante, obs
        total++; System.out.print("[" + total + "] INSPAG #1... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSPAG[\"3\",\"1\",\"500.00\",\"1\",\"COMP-001\",\"Primer pago\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [23] INSPAG #2 (ID 2) - INDEPENDIENTE para eliminar
        total++; System.out.print("[" + total + "] INSPAG #2... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "INSPAG[\"4\",\"2\",\"600.00\",\"2\",\"COMP-002\",\"Segundo pago\"]"
            ));
            if (resp.contains("xito")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // ==================== FASE 1.5: VERIFICACIÓN DE INSERCIONES ====================
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("FASE 1.5: VERIFICACIÓN DE DATOS INSERTADOS");
        System.out.println("Imprimiendo TODOS los registros para validar IDs y FK");
        System.out.println("════════════════════════════════════════════════════════════\n");
        
        System.out.println(">>> CATÁLOGOS <<<\n");
        
        System.out.println("--- ROLES (debe mostrar IDs 1,2,3,4,5) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISROL[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- TIPOS DE VEHÍCULO (debe mostrar IDs 1,2,3,4,5) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISTVH[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- TIPOS DE ACTIVIDAD (debe mostrar IDs 1,2,3,4,5) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISTAC[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- TIPOS DE PAGO (debe mostrar IDs 1,2,3,4,5) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISTPG[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- MÉTODOS DE PAGO (debe mostrar IDs 1,2,3,4,5) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISMPG[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println(">>> TABLAS PRINCIPALES <<<\n");
        
        System.out.println("--- USUARIOS (debe mostrar IDs 1,2,3,4,5 - revisar rol_id) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISUSU[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- VEHÍCULOS (debe mostrar IDs 1,2,3 - revisar tipo_vehiculo_id) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISVEH[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- ACTIVIDADES (debe mostrar IDs 1,2,3 - revisar tipo_actividad_id) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISACT[\"*\"]"));
            System.out.println(resp);
        } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        System.out.println("");
        
        System.out.println("--- SESIONES (debe mostrar IDs 1,2 - revisar actividad_id, vehiculo_id, instructor_id) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISSES[\"*\"]"));
            System.out.println(resp);
            if (!resp.contains("Error") && !resp.isEmpty()) {
                System.out.println("✓ Sesiones insertadas correctamente");
            } else {
                System.out.println("✗ NO HAY SESIONES - esto causará fallos en INSINS");
            }
        } catch (Exception e) { 
            System.out.println("✗ ERROR AL LISTAR SESIONES: " + e.getMessage()); 
            System.out.println("  Esto causará fallos en cascada en INSINS y INSPAG");
        }
        System.out.println("");
        
        System.out.println("--- INSCRIPCIONES (debe mostrar IDs 1,2 - depende de sesion_id) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISINS[\"*\"]"));
            System.out.println(resp);
            if (!resp.contains("Error") && !resp.isEmpty()) {
                System.out.println("✓ Inscripciones insertadas correctamente");
            } else {
                System.out.println("✗ NO HAY INSCRIPCIONES - esto causará fallos en INSPAG");
            }
        } catch (Exception e) { 
            System.out.println("✗ ERROR AL LISTAR INSCRIPCIONES: " + e.getMessage());
            System.out.println("  Esto causará fallos en INSPAG");
        }
        System.out.println("");
        
        System.out.println("--- PAGOS (debe mostrar IDs 1,2 - depende de inscripcion_id) ---");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISPAG[\"*\"]"));
            System.out.println(resp);
            if (!resp.contains("Error") && !resp.isEmpty()) {
                System.out.println("✓ Pagos insertados correctamente");
            } else {
                System.out.println("✗ NO HAY PAGOS");
            }
        } catch (Exception e) { 
            System.out.println("✗ ERROR AL LISTAR PAGOS: " + e.getMessage());
        }
        System.out.println("");
        
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("FIN DE VERIFICACIÓN - Analizar IDs y FK arriba");
        System.out.println("════════════════════════════════════════════════════════════\n");
        
        // ==================== FASE 2: LISTADOS (11 comandos) ====================
        System.out.println("\n═══ FASE 2: LISTADOS FORMALES (11 LISTAR) ═══\n");
        
        // [24] LISROL
        total++; System.out.print("[" + total + "] LISROL... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISROL[\"*\"]"));
            if (resp.contains("Roles") || resp.contains("Supervisor")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [25] LISTVH
        total++; System.out.print("[" + total + "] LISTVH... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISTVH[\"*\"]"));
            if (resp.contains("Tipos de Veh") || resp.contains("Bus")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [26] LISTAC
        total++; System.out.print("[" + total + "] LISTAC... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISTAC[\"*\"]"));
            if (resp.contains("Tipos de Actividad") || resp.contains("Simulador")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [27] LISTPG
        total++; System.out.print("[" + total + "] LISTPG... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISTPG[\"*\"]"));
            if (resp.contains("Tipos de Pago") || resp.contains("Beca")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [28] LISMPG
        total++; System.out.print("[" + total + "] LISMPG... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISMPG[\"*\"]"));
            if (resp.contains("todos de Pago") || resp.contains("QR")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [29] LISUSU
        total++; System.out.print("[" + total + "] LISUSU... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISUSU[\"*\"]"));
            if (resp.contains("Usuarios") || resp.contains("Pedro")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [30] LISVEH
        total++; System.out.print("[" + total + "] LISVEH... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISVEH[\"*\"]"));
            if (resp.contains("culos") || resp.contains("ABC-123")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [31] LISACT
        total++; System.out.print("[" + total + "] LISACT... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISACT[\"*\"]"));
            if (resp.contains("Actividades") || resp.contains("Manejo Nocturno")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [37] LISSES
        total++; System.out.print("[" + total + "] LISSES... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISSES[\"*\"]"));
            if (resp.contains("Sesiones") || resp.contains("sesion") || resp.contains("html")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [38] LISINS
        total++; System.out.print("[" + total + "] LISINS... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISINS[\"*\"]"));
            if (resp.contains("Inscripciones") || resp.contains("inscripcion") || resp.contains("html")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [39] LISPAG
        total++; System.out.print("[" + total + "] LISPAG... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("LISPAG[\"*\"]"));
            if (resp.contains("Pagos") || resp.contains("pago") || resp.contains("html")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // ==================== FASE 3: MODIFICACIONES (11 comandos) ====================
        System.out.println("\n═══ FASE 3: MODIFICACIONES (11 MODIFY) ═══\n");
        
        // Modificar registros ID 4 (catálogos) y registros sin dependencias (principales)
        // [35] MODROL[4]
        total++; System.out.print("[" + total + "] MODROL[4]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODROL[\"4\",\"Supervisor Gral\",\"Supervisor general de operaciones\",\"true\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [36] MODTVH[4]
        total++; System.out.print("[" + total + "] MODTVH[4]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODTVH[\"4\",\"Bus Escolar\",\"Bus de transporte escolar\",\"true\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [37] MODTAC[4]
        total++; System.out.print("[" + total + "] MODTAC[4]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODTAC[\"4\",\"Simulador Pro\",\"Simulador profesional avanzado\",\"true\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [38] MODTPG[4]
        total++; System.out.print("[" + total + "] MODTPG[4]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODTPG[\"4\",\"Beca Total\",\"Beca 100 porciento\",\"true\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [39] MODMPG[4]
        total++; System.out.print("[" + total + "] MODMPG[4]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODMPG[\"4\",\"QR Bolivia\",\"QR TigoMoney o Simple\",\"true\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [40] MODUSU[5] - María (sin dependencias)
        total++; System.out.print("[" + total + "] MODUSU[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODUSU[\"5\",\"María Elena\",\"López Pérez\",\"1992-08-20\",\"F\",\"CI\",\"5555555\",\"maria@test.com\",\"70555555\",\"Zona Oeste #10\",\"pass456\",\"3\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [41] MODVEH[3] - XYZ-789 (sin dependencias)
        total++; System.out.print("[" + total + "] MODVEH[3]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODVEH[\"3\",\"XYZ-789\",\"Nissan\",\"Versa\",\"2022\",\"1\",\"Disponible\",\"5\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [42] MODACT[3] - Estacionamiento (sin dependencias)
        total++; System.out.print("[" + total + "] MODACT[3]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODACT[\"3\",\"Estacionamiento Avanzado\",\"Técnicas avanzadas\",\"2\",\"3\",\"2\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [43] MODSES[2] - Sesión sin dependencias
        total++; System.out.print("[" + total + "] MODSES[2]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODSES[\"2\",\"2\",\"2025-12-21\",\"15:00\",\"17:00\",\"2\",\"2\",\"Pista Práctica A\",\"12\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [44] MODINS[2] - Inscripción sin dependencias (id, alumno, sesion, tipo_pago, monto, estado, curso, obs)
        total++; System.out.print("[" + total + "] MODINS[2]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODINS[\"2\",\"4\",\"2\",\"1\",\"700.00\",\"Confirmada\",\"2\",\"Actualizada\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [45] MODPAG[2] - Pago sin dependencias (id, alumno, inscripcion, monto, metodo_pago, comprobante, obs)
        total++; System.out.print("[" + total + "] MODPAG[2]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse(
                "MODPAG[\"2\",\"4\",\"2\",\"700.00\",\"2\",\"COMP-002-MOD\",\"Modificado\"]"
            ));
            if (resp.contains("xito") || resp.contains("modificado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // ==================== FASE 4: ELIMINACIONES (11 comandos) ====================
        System.out.println("\n═══ FASE 4: ELIMINACIONES (11 DELETE en orden FK inverso) ═══\n");
        
        // ORDEN: pago → inscripcion → sesion → actividad → vehiculo → usuario → catálogos
        
        // [46] DELPAG[2] - Sin dependencias
        total++; System.out.print("[" + total + "] DELPAG[2]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELPAG[\"2\"]"));
            if (resp.contains("xito") || resp.contains("eliminado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [47] DELINS[2] - Sin dependencias (pago ya eliminado)
        total++; System.out.print("[" + total + "] DELINS[2]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELINS[\"2\"]"));
            if (resp.contains("xito") || resp.contains("eliminado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [48] DELSES[2] - Sin dependencias (inscripción ya eliminada)
        total++; System.out.print("[" + total + "] DELSES[2]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELSES[\"2\"]"));
            if (resp.contains("xito") || resp.contains("eliminado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [49] DELACT[3] - Sin dependencias
        total++; System.out.print("[" + total + "] DELACT[3]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELACT[\"3\"]"));
            if (resp.contains("xito") || resp.contains("eliminado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [50] DELVEH[3] - Sin dependencias
        total++; System.out.print("[" + total + "] DELVEH[3]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELVEH[\"3\"]"));
            if (resp.contains("xito") || resp.contains("eliminado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [51] DELUSU[5] - María sin dependencias
        total++; System.out.print("[" + total + "] DELUSU[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELUSU[\"5\"]"));
            if (resp.contains("xito") || resp.contains("eliminado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [52] DELROL[5] - Secretario sin uso
        total++; System.out.print("[" + total + "] DELROL[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELROL[\"5\"]"));
            if (resp.contains("xito") || resp.contains("desactivado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [53] DELTVH[5] - Camión sin uso
        total++; System.out.print("[" + total + "] DELTVH[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELTVH[\"5\"]"));
            if (resp.contains("xito") || resp.contains("desactivado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [54] DELTAC[5] - Virtual sin uso
        total++; System.out.print("[" + total + "] DELTAC[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELTAC[\"5\"]"));
            if (resp.contains("xito") || resp.contains("desactivado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [55] DELTPG[5] - Crédito sin uso
        total++; System.out.print("[" + total + "] DELTPG[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELTPG[\"5\"]"));
            if (resp.contains("xito") || resp.contains("desactivado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [56] DELMPG[5] - Cheque sin uso
        total++; System.out.print("[" + total + "] DELMPG[5]... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("DELMPG[\"5\"]"));
            if (resp.contains("xito") || resp.contains("desactivado")) { System.out.println("✓"); exitosos++; }
            else { System.out.println("✗"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // ==================== FASE 5: REPORTES Y ESTADÍSTICAS (4 comandos) ====================
        System.out.println("\n═══ FASE 5: REPORTES Y ESTADÍSTICAS (4 COMANDOS) ═══\n");
        
        // [62] REPACT - Reporte de actividades por tipo
        total++; System.out.print("[" + total + "] REPACT... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("REPACT"));
            if (resp.contains("REPORTE") || resp.contains("Actividad") || resp.contains("actividad") || resp.contains("html")) {
                System.out.println("✓"); exitosos++;
            } else { System.out.println("✗ No contiene indicadores de reporte"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [63] REPUSU - Reporte de usuarios por rol
        total++; System.out.print("[" + total + "] REPUSU... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("REPUSU"));
            if (resp.contains("REPORTE") || resp.contains("Usuario") || resp.contains("usuario") || resp.contains("html")) {
                System.out.println("✓"); exitosos++;
            } else { System.out.println("✗ No contiene indicadores de reporte"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [64] REPVEH - Reporte de vehículos por tipo
        total++; System.out.print("[" + total + "] REPVEH... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("REPVEH"));
            if (resp.contains("REPORTE") || resp.contains("Vehículo") || resp.contains("vehiculo") || resp.contains("html")) {
                System.out.println("✓"); exitosos++;
            } else { System.out.println("✗ No contiene indicadores de reporte"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // [65] REPPAG - Reporte de pagos por método
        total++; System.out.print("[" + total + "] REPPAG... ");
        try {
            String resp = processor.procesarComando(CommandParser.parse("REPPAG"));
            if (resp.contains("REPORTE") || resp.contains("Pago") || resp.contains("pago") || resp.contains("html")) {
                System.out.println("✓"); exitosos++;
            } else { System.out.println("✗ No contiene indicadores de reporte"); fallidos++; }
        } catch (Exception e) { System.out.println("✗ " + e.getMessage()); fallidos++; }
        
        // ==================== RESUMEN FINAL ====================
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                   RESUMEN FINAL                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println(String.format("║  Total comandos:     %3d                                   ║", total));
        System.out.println(String.format("║  Exitosos:           %3d (%.0f%%)                            ║", 
            exitosos, (exitosos * 100.0 / total)));
        System.out.println(String.format("║  Fallidos:           %3d (%.0f%%)                            ║", 
            fallidos, (fallidos * 100.0 / total)));
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        if (exitosos == total) {
            System.out.println("🎉 ¡100% EXITOSO! Todas las operaciones (CRUD + REPORTES) funcionan perfectamente.");
            System.exit(0);
        } else {
            System.out.println("⚠ " + fallidos + " prueba(s) fallaron. Revise los detalles arriba.");
            System.exit(1);
        }
    }
}
JAVA_EOF

echo -e "${GREEN}✓${NC} TestComandos.java generado (65 comandos)"
echo ""

# [5/6] Compilar y ejecutar
echo -e "${YELLOW}[5/6]${NC} Ejecutando tests..."
echo ""

docker run --rm \
    -v $(pwd)/config.properties:/app/config.properties:ro \
    -v /tmp/TestComandos.java:/app/TestComandos.java:ro \
    mail-sistema-grupo17sa \
    bash -c "javac -cp /app/bin:/app/lib/* TestComandos.java && java -cp /app/bin:/app/lib/*:. TestComandos"

RESULTADO=$?

echo ""

# [6/6] Conteo final
echo -e "${YELLOW}[6/6]${NC} Registros finales:"
PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -c "
SELECT 
    (SELECT COUNT(*) FROM rol) as rol,
    (SELECT COUNT(*) FROM tipo_vehiculo) as tvh,
    (SELECT COUNT(*) FROM tipo_actividad) as tac,
    (SELECT COUNT(*) FROM tipo_pago) as tpg,
    (SELECT COUNT(*) FROM metodo_pago) as mpg,
    (SELECT COUNT(*) FROM usuario) as usr,
    (SELECT COUNT(*) FROM vehiculo) as veh,
    (SELECT COUNT(*) FROM actividad) as act,
    (SELECT COUNT(*) FROM sesion) as ses,
    (SELECT COUNT(*) FROM inscripcion) as ins,
    (SELECT COUNT(*) FROM pago) as pag;
" 2>/dev/null | grep -v "WARNING" | grep -v "DETAIL" | grep -v "HINT"
echo ""

if [ $RESULTADO -eq 0 ]; then
    echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║  ✓✓✓ PRUEBA 100% EXITOSA - TODOS LOS COMANDOS OK ✓✓✓      ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════════════════════════╝${NC}"
else
    echo -e "${RED}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${RED}║  ✗ PRUEBA FALLIDA - Ver detalles arriba                   ║${NC}"
    echo -e "${RED}╚════════════════════════════════════════════════════════════╝${NC}"
fi

# ==================== REPOBLACIÓN AUTOMÁTICA ====================
echo ""
echo -e "${YELLOW}[7/7]${NC} Repoblando base de datos con datos iniciales..."
PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -f db_schema.sql > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓${NC} Base de datos repoblada exitosamente"
    echo ""
    echo "Registros restaurados:"
    PGPASSWORD="$DB_PASS" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -c "
    SELECT 
        (SELECT COUNT(*) FROM usuario) as usuarios,
        (SELECT COUNT(*) FROM vehiculo) as vehiculos,
        (SELECT COUNT(*) FROM actividad) as actividades,
        (SELECT COUNT(*) FROM sesion) as sesiones,
        (SELECT COUNT(*) FROM inscripcion) as inscripciones,
        (SELECT COUNT(*) FROM pago) as pagos;
    " 2>/dev/null | grep -v "WARNING" | grep -v "DETAIL" | grep -v "HINT"
    echo ""
    echo -e "${GREEN}✓ Sistema restaurado al estado inicial${NC}"
else
    echo -e "${RED}✗${NC} Error al repoblar base de datos"
fi

exit $RESULTADO
