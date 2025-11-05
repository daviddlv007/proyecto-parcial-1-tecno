# 📧 EJEMPLOS DE CORREOS PARA PROBAR

## Configuración Requerida

**Antes de probar, edita `config.properties`:**

```properties
mail.pop3.host=mail.tecnoweb.org.bo
mail.pop3.user=grupoXXsa@tecnoweb.org.bo
mail.pop3.password=TU_PASSWORD

mail.smtp.host=mail.tecnoweb.org.bo
mail.smtp.user=grupoXXsa@tecnoweb.org.bo
```

---

## ✅ Prueba 1: Comando AYUDA

**Desde tu Gmail/Outlook:**
- **Para:** `grupoXXsa@tecnoweb.org.bo`
- **Asunto:** `AYUDA`
- **Cuerpo:** (vacío o cualquier cosa)

**Resultado esperado:**
Recibirás un correo con una tabla HTML mostrando todos los comandos disponibles.

---

## ✅ Prueba 2: Listar Todas las Personas

**Correo:**
- **Para:** `grupoXXsa@tecnoweb.org.bo`
- **Asunto:** `LISPER["*"]`

**Resultado esperado:**
Tabla HTML con todas las personas en la base de datos:
- Juan Pérez
- María González
- Carlos Rodríguez

---

## ✅ Prueba 3: Buscar Persona por Nombre

**Correo:**
- **Para:** `grupoXXsa@tecnoweb.org.bo`
- **Asunto:** `LISPER["Juan"]`

**Resultado esperado:**
Solo personas cuyo nombre o apellido contenga "Juan"

---

## ✅ Prueba 4: Insertar Nueva Persona

**Correo:**
- **Para:** `grupoXXsa@tecnoweb.org.bo`
- **Asunto:** `INSPER["Pedro","López"]`

**Resultado esperado:**
Mensaje de confirmación: "✅ Persona Registrada - Se registró exitosamente a Pedro López."

**Verificar:**
Luego envía `LISPER["*"]` para ver que se agregó.

---

## ✅ Prueba 5: Modificar Persona

**Correo:**
- **Para:** `grupoXXsa@tecnoweb.org.bo`
- **Asunto:** `MODPER["1","Juan Carlos","Pérez Ruiz"]`

**Resultado esperado:**
Confirmación de modificación del registro ID=1

**Verificar:**
Envía `LISPER["*"]` y verifica que Juan ahora es "Juan Carlos Pérez Ruiz"

---

## ✅ Prueba 6: Eliminar Persona

**Correo:**
- **Para:** `grupoXXsa@tecnoweb.org.bo`
- **Asunto:** `DELPER["3"]`

**Resultado esperado:**
Confirmación de eliminación del registro ID=3

**Verificar:**
Envía `LISPER["*"]` y verifica que Carlos Rodríguez ya no aparece.

---

## ❌ Pruebas de Errores

### Error 1: Comando Inválido
**Asunto:** `COMANDO_QUE_NO_EXISTE["test"]`

**Resultado:** Error indicando que el comando no existe

---

### Error 2: Parámetros Incorrectos (INSPER)
**Asunto:** `INSPER["SoloUnParametro"]`

**Resultado:** Error indicando que INSPER requiere 2 parámetros

---

### Error 3: ID No Existe (MODPER)
**Asunto:** `MODPER["999","Nombre","Apellido"]`

**Resultado:** Error indicando que no existe persona con ID=999

---

### Error 4: ID Inválido
**Asunto:** `DELPER["abc"]`

**Resultado:** Error indicando que el ID debe ser un número

---

## 🔄 Secuencia Completa de Prueba

1. **AYUDA** → Ver comandos disponibles
2. **LISPER["*"]** → Ver estado inicial (3 personas)
3. **INSPER["Ana","Martínez"]** → Insertar nueva persona
4. **LISPER["*"]** → Ver que ahora hay 4 personas
5. **LISPER["Ana"]** → Buscar solo Ana
6. **MODPER["4","Ana María","Martínez García"]** → Modificar Ana
7. **LISPER["*"]** → Verificar cambio
8. **DELPER["4"]** → Eliminar Ana
9. **LISPER["*"]** → Verificar que volvemos a tener 3 personas

---

## 💡 Tips para Testing

### Timing
- El sistema chequea correos cada 10 segundos
- Espera 10-15 segundos después de enviar antes de esperar respuesta

### Verificación Manual
```bash
# Ver base de datos en tiempo real
docker exec mail_system_db psql -U admin -d db_mail_system -c "SELECT * FROM persona;"
```

### Ver Logs del Sistema
Mientras el sistema está corriendo (`./run.sh`), verás en consola:
```
📬 1 correo(s) recibido(s)

┌─ Procesando correo ─────────────────────────
│ De: tu_correo@gmail.com
│ Asunto: LISPER["*"]
│ Comando: LISPER
│ Parámetros: 1
│ 🔍 Listando con patrón: *
│ ✓ Enviadas 3 persona(s)
└─────────────────────────────────────────────
```

---

## 📝 Template de Correo

```
Para: grupoXXsa@tecnoweb.org.bo
Asunto: [COMANDO_AQUI]
Cuerpo: (opcional)

Ejemplos de comandos:
- AYUDA
- LISPER["*"]
- LISPER["patron_busqueda"]
- INSPER["nombre","apellido"]
- MODPER["id","nombre","apellido"]
- DELPER["id"]
```

---

## 🎯 Checklist de Pruebas para Defensa

- [ ] AYUDA funciona
- [ ] LISPER["*"] muestra todos
- [ ] LISPER["patron"] filtra correctamente
- [ ] INSPER inserta correctamente
- [ ] MODPER actualiza correctamente
- [ ] DELPER elimina correctamente
- [ ] Errores de comando inválido
- [ ] Errores de parámetros incorrectos
- [ ] Errores de ID no existe
- [ ] Respuestas HTML se ven bien

---

## 🚨 Troubleshooting

### No recibo respuestas
1. Verifica que el sistema esté corriendo (`./run.sh`)
2. Revisa logs en consola
3. Verifica credenciales en `config.properties`
4. Espera al menos 15 segundos

### Error de conexión
```bash
# Reiniciar PostgreSQL
cd /home/ubuntu/proyectos/mail-system-basic
docker-compose restart
```

### Sistema no lee correos
1. Verifica `mail.pop3.host` en config.properties
2. Verifica que el puerto 110 esté accesible
3. Prueba credenciales manualmente con Thunderbird/Outlook

---

¡Listo para probar! 🚀
