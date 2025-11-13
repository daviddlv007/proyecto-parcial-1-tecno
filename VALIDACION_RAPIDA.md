# 🧪 PRUEBA RÁPIDA DE VALIDACIÓN (5 minutos)

## 📧 CONFIGURACIÓN
- **Email origen:** davidlanzadlv7@gmail.com
- **Email destino:** grupo17sa@tecnoweb.org.bo
- **Email validación:** david.lanza.valdivia@gmail.com (opcional)

---

## 🚀 INICIO RÁPIDO

### Terminal 1: Docker
```bash
cd /root/proyectos/proyecto-email-grupo17sa
docker run -v $(pwd)/config.properties:/app/config.properties:ro mail-sistema-grupo17sa
```

---

### Terminal 2: Enviar desde Gmail Web

#### ✅ PRUEBA 1: Comando Básico (AYUDA)
1. Ir a Gmail: https://mail.google.com
2. Login: davidlanzadlv7@gmail.com
3. Redactar → Para: grupo17sa@tecnoweb.org.bo
4. Asunto: `AYUDA`
5. Enviar
6. **Esperar 10 segundos** → Mirar Terminal 1

**Resultado esperado en Docker:**
```
📧 Correo recibido de: davidlanzadlv7@gmail.com
Asunto: AYUDA
Procesando comando: AYUDA

=== COMANDOS DISPONIBLES ===
[Lista de comandos...]
```

---

#### ✅ PRUEBA 2: Listar Roles
1. Asunto: `LISROL["*"]`
2. Enviar
3. Esperar 10s → Mirar Terminal 1

**Resultado esperado:**
```
📧 Correo recibido de: davidlanzadlv7@gmail.com
Asunto: LISROL["*"]
Procesando: LISROL

=== ROLES ENCONTRADOS ===
ID | Nombre | Descripción | Activo
1  | Admin  | Administrador | true
2  | Instructor | Instructor del sistema | true
3  | Alumno | Alumno del sistema | true
4  | Gerente | Gerente de operaciones | true
```

---

#### ✅ PRUEBA 3: Insertar Rol Nuevo
1. Asunto: `INSROL["Tesorero","Encargado de finanzas"]`
2. Enviar
3. Esperar 10s → Mirar Terminal 1

**Resultado esperado:**
```
✅ Rol insertado correctamente
ID asignado: 5
```

---

#### ✅ PRUEBA 4: Verificar Inserción
1. Asunto: `LISROL["Tesorero"]`
2. Enviar
3. Esperar 10s → Mirar Terminal 1

**Resultado esperado:**
```
=== ROLES ENCONTRADOS ===
ID | Nombre | Descripción | Activo
5  | Tesorero | Encargado de finanzas | true
```

---

#### ✅ PRUEBA 5: Modificar Rol
1. Asunto: `MODROL["5","Tesorero General","Encargado general de finanzas","true"]`
2. Enviar
3. Esperar 10s → Mirar Terminal 1

**Resultado esperado:**
```
✅ Rol modificado correctamente
```

---

#### ✅ PRUEBA 6: Eliminar Rol
1. Asunto: `DELROL["5"]`
2. Enviar
3. Esperar 10s → Mirar Terminal 1

**Resultado esperado:**
```
✅ Rol desactivado correctamente
```

---

#### ✅ PRUEBA 7: Verificar Eliminación
1. Asunto: `LISROL["*"]`
2. Enviar
3. Esperar 10s → Mirar Terminal 1

**Resultado esperado:**
```
Debe mostrar solo 4 roles (sin Tesorero, o con Tesorero activo=false)
```

---

## ✅ CHECKLIST DE VALIDACIÓN

- [ ] Docker corriendo sin errores
- [ ] Correos llegan desde davidlanzadlv7@gmail.com
- [ ] Respuestas aparecen en terminal Docker
- [ ] AYUDA funciona
- [ ] LISROL["*"] muestra 4 roles iniciales
- [ ] INSROL crea nuevo rol (ID=5)
- [ ] MODROL actualiza el rol
- [ ] DELROL desactiva el rol
- [ ] Tiempo de respuesta < 30 segundos

---

## 🎯 SI TODO FUNCIONA

Has validado:
1. ✅ El sistema recibe correos correctamente
2. ✅ Los comandos se procesan sin errores
3. ✅ Las operaciones CRUD funcionan 100%
4. ✅ El flujo email→Docker→respuesta está operativo

**Siguiente paso:** Ejecutar suite completa de 45 pruebas (ver PRUEBAS_MANUALES.md)

---

## ⚠️ SI HAY PROBLEMAS

### Docker no muestra nada:
```bash
# Ver logs completos
docker ps  # Obtener CONTAINER_ID
docker logs <CONTAINER_ID> -f
```

### Timeout o conexión PostgreSQL:
```bash
# Verificar conexión a BD
docker exec -it <CONTAINER_ID> psql -h www.tecnoweb.org.bo -p 5432 -U grupo17sa -d db_grupo17sa
# Password: grup017grup017*
```

### Sintaxis de comando incorrecta:
- Verificar comillas dobles: `LISROL["*"]` no `LISROL['*']`
- Verificar corchetes: `INSROL[...]` no `INSROL(...)`
- Verificar escape: En Gmail web no necesitas escapar

---

## 📧 VALIDACIÓN ALTERNATIVA

Si quieres confirmar que los correos llegan, puedes:

1. Enviar copia a ti mismo:
   - Para: grupo17sa@tecnoweb.org.bo
   - CC: david.lanza.valdivia@gmail.com
   
2. Verificar en "Enviados" de Gmail que salió correctamente

---

**TIEMPO ESTIMADO:** 5-10 minutos para las 7 pruebas básicas
