package database;

import models.SystemSettings;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SystemSettingsDAO - Data Access Object for System Settings.
 * Implements Singleton pattern and proper exception handling.
 */
public class SystemSettingsDAO {
    private static final Logger LOGGER = Logger.getLogger(SystemSettingsDAO.class.getName());
    private DatabaseConnection dbConnection;

    public SystemSettingsDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection initialization failed in SystemSettingsDAO", e);
        }
    }

    /**
     * Get all settings as a Map
     */
    public Map<String, String> getAllSettings() {
        Map<String, String> settings = new HashMap<>();
        String query = "SELECT setting_key, setting_value FROM system_settings";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                settings.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all system settings", e);
        }
        return settings;
    }

    /**
     * Update a setting value
     */
    public boolean updateSetting(String key, String value) {
        String query = "UPDATE system_settings SET setting_value = ? WHERE setting_key = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, value);
            stmt.setString(2, key);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating system setting: " + key, e);
            return false;
        }
    }
}
