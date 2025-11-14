# Dockerfile para producción - Proyecto Email Sistema
# Grupo 17SA - TecnoWeb

FROM eclipse-temurin:11-jdk-jammy

# Instalar netcat y postgresql-client para health checks y scripts SQL
RUN apt-get update && apt-get install -y netcat-openbsd postgresql-client && rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Copiar librerías
COPY lib/*.jar /app/lib/

# Copiar código fuente
COPY src /app/src

# Compilar el proyecto
RUN javac -d /app/bin \
    -cp "/app/lib/*" \
    $(find /app/src -name "*.java")

# Copiar script SQL unificado y entrypoint
COPY db_schema.sql /app/
COPY docker-entrypoint.sh /app/

# Variable de entorno para Java
ENV CLASSPATH="/app/bin:/app/lib/*"

# Ejecutar script que genera config.properties y lanza la app
ENTRYPOINT ["/app/docker-entrypoint.sh"]
