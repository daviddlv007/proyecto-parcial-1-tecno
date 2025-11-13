# PRUEBAS FUNCIONALES POR EMAIL (Método Manual)

## ⚠️ IMPORTANTE: Puertos SMTP bloqueados en servidor
Los puertos 25, 465, y 587 están bloqueados por firewall.
**Solución:** Enviar correos desde Gmail web interface manualmente.

---

## 🎯 CONFIGURACIÓN DE PRUEBAS

### PASO 1: Iniciar Sistema Docker (Terminal 1)
```bash
cd /root/proyectos/proyecto-email-grupo17sa
docker run -v $(pwd)/config.properties:/app/config.properties:ro mail-sistema-grupo17sa
```

**Dejar esta terminal abierta** - aquí verás las respuestas en tiempo real.

---

### PASO 2: Enviar Comandos desde Gmail

1. Ir a: https://mail.google.com
2. Iniciar sesión con: **davidlanzadlv7@gmail.com**
3. Click en "Redactar"
4. Llenar:
   - **Para:** grupo17sa@tecnoweb.org.bo
   - **Asunto:** [COMANDO AQUÍ]
   - **Mensaje:** (dejar vacío o cualquier texto)
5. Click "Enviar"
6. **Esperar 5-10 segundos** → Ver respuesta en Terminal Docker

---

## 📋 SUITE DE PRUEBAS COMPLETA (45 comandos)

### ✅ FASE 1: VERIFICACIÓN INICIAL (5 comandos)

#### 1. Ayuda del sistema
```
Asunto: AYUDA
```
✓ Debe mostrar lista de comandos disponibles

---

#### 2. Listar roles existentes
```
Asunto: LISROL["*"]
```
✓ Debe mostrar 4 roles (Admin, Instructor, Alumno, Gerente)

---

#### 3. Listar usuarios existentes
```
Asunto: LISUSU["*"]
```
✓ Debe mostrar usuarios del sistema

---

#### 4. Listar actividades
```
Asunto: LISACT["*"]
```
✓ Debe mostrar actividades disponibles

---

#### 5. Listar sesiones
```
Asunto: LISSES["*"]
```
✓ Debe mostrar sesiones programadas

---

### ➕ FASE 2: OPERACIONES INSERT (11 comandos)

#### 6. Insertar Rol
```
Asunto: INSROL["Contador","Encargado del área contable"]
```
✓ Respuesta: "Rol insertado con ID: X"

---

#### 7. Insertar Tipo Vehículo
```
Asunto: INSTIV["Camioneta","Vehículo tipo camioneta"]
```
✓ Respuesta: "Tipo de vehículo insertado con ID: X"

---

#### 8. Insertar Tipo Actividad
```
Asunto: INSTIA["Yoga Avanzado","Clase avanzada de yoga"]
```
✓ Respuesta: "Tipo de actividad insertado con ID: X"

---

#### 9. Insertar Tipo Pago
```
Asunto: INSTIP["Transferencia Bancaria","Pago por transferencia"]
```
✓ Respuesta: "Tipo de pago insertado con ID: X"

---

#### 10. Insertar Método Pago
```
Asunto: INSMEP["Banco Union","1","Cuenta 123456789"]
```
✓ Respuesta: "Método de pago insertado con ID: X"
**Nota:** Usar tipo_pago_id del comando 9

---

#### 11. Insertar Vehículo
```
Asunto: INSVEH["1","Toyota","Hilux","ABC-1234","2020","150000"]
```
✓ Respuesta: "Vehículo insertado con ID: X"
**Nota:** Usar tipo_vehiculo_id del comando 7

---

#### 12. Insertar Actividad
```
Asunto: INSACT["1","Yoga Matutino","2025-01-15","2025-03-15","08:00:00","10:00:00","500.00","L-M-V","20","Aula 1"]
```
✓ Respuesta: "Actividad insertada con ID: X"
**Nota:** Usar tipo_actividad_id del comando 8

---

#### 13. Insertar Usuario (Alumno)
```
Asunto: INSUSU["Carlos","Méndez","1995-03-20","M","CI","9876543","carlos@mail.com","71234567","Av. América 100","pass456","3"]
```
✓ Respuesta: "Usuario insertado con ID: X"
**Nota:** rol_id=3 (Alumno)

---

#### 14. Insertar Sesión
```
Asunto: INSSES["1","2025-12-20","09:00","11:00","2","1","Aula Virtual","15"]
```
✓ Respuesta: "Sesión insertada con ID: X"
**Nota:** Usar actividad_id del comando 12, instructor_id existente

---

#### 15. Insertar Pago
```
Asunto: INSPAG["1","2025-11-13","350.00","Confirmado","Pago inicial"]
```
✓ Respuesta: "Pago insertado con ID: X"
**Nota:** Usar metodo_pago_id del comando 10

---

#### 16. Insertar Inscripción
```
Asunto: INSINS["1","1","1","350.00","Confirmada","1","Primera inscripción"]
```
✓ Respuesta: "Inscripción insertada con ID: X"
**Nota:** Usar alumno_id (cmd 13), sesion_id (cmd 14), pago_id (cmd 15)

---

### 📝 FASE 3: OPERACIONES MODIFY (11 comandos)

#### 17. Modificar Rol
```
Asunto: MODROL["<ID_CONTADOR>","Contador Sr","Contador Senior","true"]
```
✓ Respuesta: "Rol modificado correctamente"
**Nota:** Reemplazar <ID_CONTADOR> con el ID del comando 6

---

#### 18. Modificar Tipo Vehículo
```
Asunto: MODTIV["<ID_CAMIONETA>","Camioneta 4x4","Camioneta doble cabina","true"]
```
✓ Respuesta: "Tipo de vehículo modificado correctamente"

---

#### 19. Modificar Tipo Actividad
```
Asunto: MODTIA["<ID_YOGA>","Yoga Integral","Clase integral de yoga","true"]
```
✓ Respuesta: "Tipo de actividad modificado correctamente"

---

#### 20. Modificar Tipo Pago
```
Asunto: MODTIP["<ID_TRANSFER>","Transfer Bancaria","Pago por transferencia bancaria","true"]
```
✓ Respuesta: "Tipo de pago modificado correctamente"

---

#### 21. Modificar Método Pago
```
Asunto: MODMEP["<ID_METODO>","Banco BCP","<ID_TIPO_PAGO>","Cuenta 987654321","true"]
```
✓ Respuesta: "Método de pago modificado correctamente"

---

#### 22. Modificar Vehículo
```
Asunto: MODVEH["<ID_VEHICULO>","<ID_TIPO>","Toyota","Fortuner","ABC-1234","2021","160000","true"]
```
✓ Respuesta: "Vehículo modificado correctamente"

---

#### 23. Modificar Actividad
```
Asunto: MODACT["<ID_ACTIVIDAD>","<ID_TIPO>","Yoga Vespertino","2025-01-15","2025-03-15","17:00:00","19:00:00","550.00","L-M-V","25","Aula 2","true"]
```
✓ Respuesta: "Actividad modificada correctamente"

---

#### 24. Modificar Usuario
```
Asunto: MODUSU["<ID_USUARIO>","Carlos Alberto","Méndez López","1995-03-20","M","CI","9876543","carlos.mendez@mail.com","71234567","Av. América 200","newpass","3","true"]
```
✓ Respuesta: "Usuario modificado correctamente"

---

#### 25. Modificar Sesión
```
Asunto: MODSES["<ID_SESION>","<ID_ACTIVIDAD>","2025-12-21","10:00","12:00","2","<ID_VEHICULO>","Sala Principal","20","true"]
```
✓ Respuesta: "Sesión modificada correctamente"

---

#### 26. Modificar Pago
```
Asunto: MODPAG["<ID_PAGO>","<ID_METODO>","2025-11-13","400.00","Confirmado","Pago completo","true"]
```
✓ Respuesta: "Pago modificado correctamente"

---

#### 27. Modificar Inscripción
```
Asunto: MODINS["<ID_INSCRIPCION>","<ID_ALUMNO>","<ID_SESION>","<ID_PAGO>","400.00","Activa","Nueva observación","true"]
```
✓ Respuesta: "Inscripción modificada correctamente"

---

### 🔍 FASE 4: VERIFICACIÓN POST-MODIFY (11 comandos)

Repetir los comandos LIST para verificar cambios:

#### 28-38. Listar registros modificados
```
LISROL["Contador"]
LISTIV["Camioneta"]
LISTIA["Yoga"]
LISTIP["Transfer"]
LISMEP["BCP"]
LISVEH["Fortuner"]
LISACT["Vespertino"]
LISUSU["Carlos"]
LISSES["Principal"]
LISPAG["400"]
LISINS["Activa"]
```

---

### 🗑️ FASE 5: OPERACIONES DELETE (11 comandos)

**IMPORTANTE:** Eliminar en orden inverso para evitar problemas de FK

#### 39. Eliminar Inscripción
```
Asunto: DELINS["<ID_INSCRIPCION>"]
```
✓ Respuesta: "Inscripción desactivada correctamente"

---

#### 40. Eliminar Pago
```
Asunto: DELPAG["<ID_PAGO>"]
```
✓ Respuesta: "Pago desactivado correctamente"

---

#### 41. Eliminar Sesión
```
Asunto: DELSES["<ID_SESION>"]
```
✓ Respuesta: "Sesión desactivada correctamente"

---

#### 42. Eliminar Usuario
```
Asunto: DELUSU["<ID_USUARIO>"]
```
✓ Respuesta: "Usuario desactivado correctamente"

---

#### 43. Eliminar Actividad
```
Asunto: DELACT["<ID_ACTIVIDAD>"]
```
✓ Respuesta: "Actividad desactivada correctamente"

---

#### 44. Eliminar Vehículo
```
Asunto: DELVEH["<ID_VEHICULO>"]
```
✓ Respuesta: "Vehículo desactivado correctamente"

---

#### 45. Eliminar Método Pago
```
Asunto: DELMEP["<ID_METODO>"]
```
✓ Respuesta: "Método de pago desactivado correctamente"

---

## 📊 REGISTRO DE RESULTADOS

Crear tabla para documentar:

| # | Comando | Asunto Exacto | Resultado | Tiempo Respuesta | Observaciones |
|---|---------|---------------|-----------|------------------|---------------|
| 1 | AYUDA | AYUDA | ✅ / ❌ | Xs | |
| 2 | LISROL | LISROL["*"] | ✅ / ❌ | Xs | Cantidad roles: |
| ... | ... | ... | ... | ... | ... |

---

## 🔧 TROUBLESHOOTING

**Si no ves respuestas en Docker:**
1. Verificar que Docker esté corriendo: `docker ps`
2. Ver logs: `docker logs <container_id> -f`
3. Verificar config.properties tiene credenciales correctas

**Si el comando falla:**
1. Verificar sintaxis exacta (comillas dobles, corchetes)
2. Verificar que IDs de FK existen (LISTAR antes de INSERTAR/MODIFICAR)
3. Revisar formato de fechas (YYYY-MM-DD) y horas (HH:MM)

---

## ✅ CRITERIOS DE ÉXITO

- [ ] 45/45 comandos ejecutados
- [ ] Todas las respuestas recibidas en <30s
- [ ] Todos los INSERT retornan ID correcto
- [ ] Todos los MODIFY confirman actualización
- [ ] Todos los DELETE desactivan registro
- [ ] No hay errores de FK o sintaxis

---

**NOTA FINAL:** Este método manual es válido para validación funcional completa.
Para automatización futura, se necesitará servidor sin restricciones de firewall
o usar API REST como alternativa al sistema de emails.
