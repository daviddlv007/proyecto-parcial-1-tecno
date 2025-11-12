FROM eclipse-temurin:11-jdk-alpine

WORKDIR /app

# Copiar dependencias
COPY lib/ /app/lib/

# Copiar código fuente
COPY src/ /app/src/

# Compilar aplicación
RUN javac -d /app/bin -cp "/app/lib/*" \
    /app/src/datos/*.java \
    /app/src/conexion/*.java \
    /app/src/negocio/*.java \
    /app/src/servicio/*.java \
    /app/src/Main.java \
    /app/src/SimuladorComandos.java

# Copiar configuración
COPY config.properties /app/

# Comando por defecto
CMD ["java", "-cp", "/app/bin:/app/lib/*", "Main"]
