package database;

import models.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PatientDAO - Data Access Object for Patient operations.
 * Implements proper OOP with singleton pattern for database connection.
 */
public class PatientDAO {
    private static final Logger LOGGER = Logger.getLogger(PatientDAO.class.getName());
    private DatabaseConnection dbConnection;
    
    public PatientDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection initialization failed in PatientDAO", e);
        }
    }
    
    public Patient getPatientById(int patientId) {
        String query = "SELECT * FROM users WHERE id = ? AND role = 'patient'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapPatient(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving patient by ID: " + patientId, e);
        }
        return null;
    }
    
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'patient'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all patients", e);
        }
        return patients;
    }
    
    public boolean updatePatient(Patient patient) {
        String query = "UPDATE users SET email = ?, phone = ? WHERE id = ? AND role = 'patient'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patient.getEmail());
            stmt.setString(2, patient.getPhone());
            stmt.setInt(3, patient.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating patient with ID: " + patient.getId(), e);
            return false;
        }
    }
    
    public boolean deletePatient(int patientId) {
        String query = "DELETE FROM users WHERE id = ? AND role = 'patient'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting patient with ID: " + patientId, e);
            return false;
        }
    }
    
    private Patient mapPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setUsername(rs.getString("username"));
        patient.setPasswordHash(rs.getString("password_hash"));
        patient.setRole(rs.getString("role"));
        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        return patient;
    }
}
