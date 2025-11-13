# Dockerfile para producción - Proyecto Email Sistema
# Grupo 17SA - TecnoWeb

FROM eclipse-temurin:11-jdk-jammy

# Instalar netcat para health checks
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Copiar librerías
COPY lib/*.jar /app/lib/

# Copiar archivos compilados (se compilan en build stage)
COPY src /app/src

# Copiar configuración de producción
COPY config.properties /app/

# Compilar el proyecto
RUN javac -d /app/bin \
    -cp "/app/lib/*" \
    $(find /app/src -name "*.java")

# Variable de entorno para Java
ENV CLASSPATH="/app/bin:/app/lib/*"

# Comando de inicio
CMD ["java", "-cp", "/app/bin:/app/lib/*", "Main"]
