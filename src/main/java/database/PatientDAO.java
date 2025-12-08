package database;

import models.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private DatabaseConnection dbConnection;

    public PatientDAO() {
        this.dbConnection = new DatabaseConnection();
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
            System.err.println("Error retrieving patient: " + e.getMessage());
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'patient'";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
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
            System.err.println("Error updating patient: " + e.getMessage());
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
            System.err.println("Error deleting patient: " + e.getMessage());
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
