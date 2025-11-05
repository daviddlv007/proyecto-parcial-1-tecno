#!/bin/bash
# Comandos útiles para el proyecto

echo "════════════════════════════════════════════════════════════"
echo "   COMANDOS ÚTILES - Sistema de Gestión vía E-Mail"
echo "════════════════════════════════════════════════════════════"
echo ""

# Función para mostrar menú
mostrar_menu() {
    echo "Selecciona una opción:"
    echo ""
    echo "1. 🚀 Ejecutar sistema"
    echo "2. 🔨 Recompilar código"
    echo "3. 📊 Ver base de datos"
    echo "4. ➕ Insertar persona de prueba"
    echo "5. 🗑️  Limpiar base de datos"
    echo "6. 🔄 Reiniciar PostgreSQL"
    echo "7. 📝 Ver logs de PostgreSQL"
    echo "8. ✅ Test de conexión"
    echo "9. 🛑 Detener todo"
    echo "0. ❌ Salir"
    echo ""
    read -p "Opción: " opcion

    case $opcion in
        1)
            echo "🚀 Ejecutando sistema..."
            ./run.sh
            ;;
        2)
            echo "🔨 Recompilando..."
            ./compile.sh
            ;;
        3)
            echo "📊 Contenido de la tabla persona:"
            docker exec mail_system_db psql -U admin -d db_mail_system -c "SELECT * FROM persona ORDER BY id;"
            ;;
        4)
            read -p "Nombre: " nombre
            read -p "Apellido: " apellido
            docker exec mail_system_db psql -U admin -d db_mail_system -c "INSERT INTO persona (nombre, apellido) VALUES ('$nombre', '$apellido');"
            echo "✅ Persona insertada"
            ;;
        5)
            read -p "⚠️  ¿Estás seguro? Esto borrará todas las personas (s/n): " confirmacion
            if [ "$confirmacion" = "s" ]; then
                docker exec mail_system_db psql -U admin -d db_mail_system -c "TRUNCATE TABLE persona RESTART IDENTITY CASCADE;"
                echo "✅ Tabla limpiada"
            fi
            ;;
        6)
            echo "🔄 Reiniciando PostgreSQL..."
            docker-compose down
            docker-compose up -d
            echo "✅ PostgreSQL reiniciado"
            ;;
        7)
            echo "📝 Logs de PostgreSQL (Ctrl+C para salir):"
            docker logs -f mail_system_db
            ;;
        8)
            echo "✅ Ejecutando test..."
            java -cp "bin:lib/*" TestConexion
            ;;
        9)
            echo "🛑 Deteniendo servicios..."
            docker-compose down
            echo "✅ Servicios detenidos"
            ;;
        0)
            echo "👋 ¡Hasta luego!"
            exit 0
            ;;
        *)
            echo "❌ Opción inválida"
            ;;
    esac

    echo ""
    read -p "Presiona Enter para continuar..."
    clear
    mostrar_menu
}

# Verificar que estamos en el directorio correcto
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ Error: Ejecuta este script desde /home/ubuntu/proyectos/mail-system-basic/"
    exit 1
fi

clear
mostrar_menu
