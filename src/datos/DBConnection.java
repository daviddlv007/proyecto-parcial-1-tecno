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
