package database;

import models.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DoctorDAO - Data Access Object for Doctor operations.
 * Implements Singleton pattern and standard logging.
 */
public class DoctorDAO {
    private static final Logger LOGGER = Logger.getLogger(DoctorDAO.class.getName());
    private DatabaseConnection dbConnection;

    public DoctorDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection initialization failed in DoctorDAO", e);
        }
    }

    /**
     * Get doctor by ID
     */
    public Doctor getDoctorById(int doctorId) {
        String query = "SELECT * FROM users WHERE id = ? AND role = 'doctor'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapDoctor(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving doctor: " + doctorId, e);
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
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving doctors", e);
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
            LOGGER.log(Level.SEVERE, "Error updating doctor: " + doctor.getId(), e);
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
            LOGGER.log(Level.SEVERE, "Error deleting doctor: " + doctorId, e);
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
