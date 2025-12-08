package database;

import models.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DoctorDAO - Data Access Object for Doctor operations
 */
public class DoctorDAO {
    private DatabaseConnection dbConnection;

    public DoctorDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    /**
     * Get doctor by ID
     */
    public Doctor getDoctorById(int doctorId) {
        String query = "SELECT * FROM users WHERE id = ? AND role = 'doctor'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapDoctor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all doctors
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'doctor'";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctors: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Update doctor details
     */
    public boolean updateDoctor(Doctor doctor) {
        String query = "UPDATE users SET email = ?, phone = ? WHERE id = ? AND role = 'doctor'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doctor.getEmail());
            stmt.setString(2, doctor.getPhone());
            stmt.setInt(3, doctor.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete doctor
     */
    public boolean deleteDoctor(int doctorId) {
        String query = "DELETE FROM users WHERE id = ? AND role = 'doctor'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Map ResultSet to Doctor object
     */
    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setUsername(rs.getString("username"));
        doctor.setPasswordHash(rs.getString("password_hash"));
        doctor.setRole(rs.getString("role"));
        doctor.setEmail(rs.getString("email"));
        doctor.setPhone(rs.getString("phone"));
        return doctor;
    }
}
