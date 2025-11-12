package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Gestiona la conexión a PostgreSQL
 */
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private Properties config;

    private DBConnection() throws SQLException, IOException {
        loadConfig();
        connect();
    }

    private void loadConfig() throws IOException {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            config.load(fis);
        }
        
        // Cargar variables de entorno si están disponibles
        String dbHost = System.getenv("DB_HOST");
        if (dbHost != null) config.setProperty("db.host", dbHost);
        
        String dbPort = System.getenv("DB_PORT");
        if (dbPort != null) config.setProperty("db.port", dbPort);
        
        String dbName = System.getenv("DB_NAME");
        if (dbName != null) config.setProperty("db.name", dbName);
        
        String dbUser = System.getenv("DB_USER");
        if (dbUser != null) config.setProperty("db.user", dbUser);
        
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword != null) config.setProperty("db.password", dbPassword);
    }

    private void connect() throws SQLException {
        String host = config.getProperty("db.host");
        String port = config.getProperty("db.port");
        String dbName = config.getProperty("db.name");
        String user = config.getProperty("db.user");
        String password = config.getProperty("db.password");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
        
        System.out.println("Conectando a base de datos: " + url);
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("✓ Conexión exitosa a PostgreSQL");
    }

    public static DBConnection getInstance() throws SQLException, IOException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión a DB cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
