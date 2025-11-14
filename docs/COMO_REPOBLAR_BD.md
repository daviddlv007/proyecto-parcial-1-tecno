# 📚 Cómo Repoblar la Base de Datos

## 🎯 Problema
Los tests limpian la base de datos y después de ejecutarlos solo quedan algunos registros de prueba en lugar de los 14 usuarios, 14 vehículos, 16 pagos, etc.

## ✅ Soluciones Disponibles

### **Opción 1: Automática (Test se autolimpia)**

El script `test_replicable.sh` **ahora repuebla automáticamente** después de ejecutar los tests.

```bash
./test_replicable.sh
```

**Resultado:**
- Ejecuta 65 tests (100% funcional)
- Al final restaura automáticamente los datos iniciales
- Deja la BD con: 14 usuarios, 14 vehículos, 14 actividades, 16 inscripciones, 16 pagos

---

### **Opción 2: Manual - Reiniciar Contenedor**

```bash
docker-compose restart app
```

**¿Qué hace?**
- Detiene y reinicia el contenedor
- Ejecuta automáticamente `docker-entrypoint.sh`
- Este ejecuta `db_schema.sql` que hace:
  - DROP de todas las tablas
  - CREATE de todas las tablas
  - INSERT de todos los datos iniciales

**Cuándo usar:** Después de hacer pruebas manuales que modificaron la BD

---

### **Opción 3: Manual - Reconstruir Contenedor**

```bash
docker-compose down
docker-compose up -d
```

**¿Qué hace?**
- Elimina completamente el contenedor
- Crea uno nuevo desde la imagen
- Ejecuta `db_schema.sql` automáticamente

**Cuándo usar:** Después de modificar código fuente en `src/`

---

### **Opción 4: Manual - Ejecutar Script SQL**

```bash
docker exec email-app psql postgresql://grupo17sa:grup017grup017*@mail.tecnoweb.org.bo:5432/db_grupo17sa -f /app/db_schema.sql
```

**¿Qué hace?**
- Ejecuta directamente el script SQL
- No reinicia el contenedor
- Repuebla los datos sin afectar la aplicación Java

**Cuándo usar:** Cuando solo quieres restaurar datos sin reiniciar el servicio

---

### **Opción 5: Crear Script Auxiliar**

Puedes crear un script `repoblar.sh`:

```bash
#!/bin/bash
echo "🔄 Repoblando base de datos..."

docker exec email-app psql \
  postgresql://grupo17sa:grup017grup017*@mail.tecnoweb.org.bo:5432/db_grupo17sa \
  -f /app/db_schema.sql

echo "✓ Base de datos repoblada"
echo ""
echo "Verificando registros:"

docker exec email-app psql \
  postgresql://grupo17sa:grup017grup017*@mail.tecnoweb.org.bo:5432/db_grupo17sa \
  -c "SELECT 
    (SELECT COUNT(*) FROM usuario) as usuarios,
    (SELECT COUNT(*) FROM vehiculo) as vehiculos,
    (SELECT COUNT(*) FROM actividad) as actividades,
    (SELECT COUNT(*) FROM inscripcion) as inscripciones,
    (SELECT COUNT(*) FROM pago) as pagos;" \
  2>&1 | grep -v WARNING | grep -v DETAIL | grep -v HINT
```

Dar permisos de ejecución:
```bash
chmod +x repoblar.sh
```

Usar:
```bash
./repoblar.sh
```

---

## 📊 Datos Esperados Después de Repoblar

| Tabla | Cantidad | Descripción |
|-------|----------|-------------|
| rol | 4 | Administrador, Instructor, Alumno, Auxiliar |
| tipo_vehiculo | 4 | Auto, Camioneta, Motocicleta, Bus |
| tipo_actividad | 4 | Teórica, Práctica, Evaluación, Taller |
| tipo_pago | 4 | Contado, Cuotas, Mensual, Beca |
| metodo_pago | 4 | Efectivo, Transferencia, Tarjeta, QR |
| usuario | 14 | 1 admin, 3 instructores, 8 alumnos, 2 auxiliares |
| vehiculo | 14 | 4 autos, 3 camionetas, 4 motos, 3 buses |
| actividad | 14 | Distribuidas en los 4 tipos |
| sesion | 14 | Sesiones programadas con vehículos e instructores |
| inscripcion | 16 | Alumnos inscritos en sesiones |
| pago | 16 | Distribuidos entre los 4 métodos de pago |

---

## 🔍 Verificar Datos

```bash
docker exec email-app psql \
  postgresql://grupo17sa:grup017grup017*@mail.tecnoweb.org.bo:5432/db_grupo17sa \
  -c "SELECT 
    (SELECT COUNT(*) FROM rol) as rol,
    (SELECT COUNT(*) FROM tipo_vehiculo) as tvh,
    (SELECT COUNT(*) FROM tipo_actividad) as tac,
    (SELECT COUNT(*) FROM usuario) as usr,
    (SELECT COUNT(*) FROM vehiculo) as veh,
    (SELECT COUNT(*) FROM actividad) as act,
    (SELECT COUNT(*) FROM sesion) as ses,
    (SELECT COUNT(*) FROM inscripcion) as ins,
    (SELECT COUNT(*) FROM pago) as pag;" \
  2>&1 | grep -v WARNING
```

**Output esperado:**
```
 rol | tvh | tac | usr | veh | act | ses | ins | pag 
-----+-----+-----+-----+-----+-----+-----+-----+-----
   4 |   4 |   4 |  14 |  14 |  14 |  14 |  16 |  16
```

---

## ⚙️ Cómo Funciona (Técnicamente)

### 1. **docker-entrypoint.sh** (Se ejecuta al iniciar contenedor)
```bash
# Líneas 51-58 del archivo
echo "Inicializando base de datos con datos de prueba..."
PGPASSWORD=${POSTGRES_PASSWORD} psql \
  -h ${DB_HOST} \
  -U ${POSTGRES_USER} \
  -d ${POSTGRES_DB} \
  -f /app/db_schema.sql
```

### 2. **db_schema.sql** (Script de población)
```sql
-- Paso 1: Eliminar datos anteriores
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS inscripcion CASCADE;
-- ... etc

-- Paso 2: Crear estructura
CREATE TABLE rol (...);
CREATE TABLE tipo_vehiculo (...);
-- ... etc

-- Paso 3: Poblar datos
INSERT INTO rol VALUES (...);
INSERT INTO usuario VALUES (...);
-- ... etc (total 16 INSERTs en tabla pago)
```

### 3. **test_replicable.sh** (Repoblación post-test)
```bash
# Línea 890+
PGPASSWORD="$DB_PASS" psql \
  -h "$DB_HOST" \
  -U "$DB_USER" \
  -d "$DB_NAME" \
  -f db_schema.sql
```

---

## 🚀 Recomendación

**Usa Opción 1 (test automático)** para:
- Desarrollo diario
- Tests de integración
- CI/CD pipelines

**Usa Opción 2 (restart)** para:
- Restaurar datos rápidamente
- Después de pruebas manuales

**Usa Opción 3 (rebuild)** para:
- Después de cambios en código
- Problemas de corrupción de datos

**Usa Opción 4 (script directo)** para:
- Restaurar sin interrumpir servicio
- Automatización personalizada
