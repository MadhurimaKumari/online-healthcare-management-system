package database;

import models.SystemSettings;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SystemSettingsDAO {
    private DatabaseConnection dbConnection;

    public SystemSettingsDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public boolean saveSetting(String key, String value) {
        String query = "INSERT INTO system_settings (setting_key, setting_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE setting_value = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, value);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving setting: " + e.getMessage());
            return false;
        }
    }

    public String getSetting(String key) {
        String query = "SELECT setting_value FROM system_settings WHERE setting_key = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("setting_value");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving setting: " + e.getMessage());
        }
        return null;
    }

    public Map<String, String> getAllSettings() {
        Map<String, String> settings = new HashMap<>();
        String query = "SELECT * FROM system_settings";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                settings.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving settings: " + e.getMessage());
        }
        return settings;
    }

    public boolean deleteSetting(String key) {
        String query = "DELETE FROM system_settings WHERE setting_key = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, key);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting setting: " + e.getMessage());
            return false;
        }
    }
}
