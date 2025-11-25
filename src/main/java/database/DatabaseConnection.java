package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe Singleton class for database connection management
 * Implements double-checked locking pattern for thread safety
 * Prevents resource leaks by managing JDBC resources properly
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static volatile DatabaseConnection instance;
    private Connection connection;
    
    // Database configuration (ideally load from properties file)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/healthcare_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your_password_here"; // Change this to actual password
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() throws SQLException {
        try {
            // Load MySQL driver
            Class.forName(DB_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            LOGGER.info("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new SQLException("Failed to load database driver", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw e;
        }
    }
    
    /**
     * Thread-safe method to get singleton instance using double-checked locking
     * @return Singleton DatabaseConnection instance
     * @throws SQLException if connection fails
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get database connection with null check and validation
     * @return Database Connection object
     * @throws SQLException if connection is null or closed
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            LOGGER.warning("Connection is null or closed, attempting to reconnect...");
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    LOGGER.info("Reconnected to database");
                }
            }
        }
        return connection;
    }
    
    /**
     * Close database connection safely
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", e);
        }
    }
    
    /**
     * Clean up resources when application shuts down
     */
    public void shutdown() {
        closeConnection();
        instance = null;
    }
}
