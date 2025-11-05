#!/bin/bash
# Script de compilación

echo "🔨 Compilando proyecto..."

# Crear directorio de salida
mkdir -p bin

# Compilar con todas las dependencias
javac -d bin -cp "lib/*" \
    src/datos/*.java \
    src/conexion/*.java \
    src/negocio/*.java \
    src/servicio/*.java \
    src/Main.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
else
    echo "❌ Error de compilación"
    exit 1
fi
