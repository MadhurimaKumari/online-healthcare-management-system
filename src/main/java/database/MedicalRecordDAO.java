package database;

import models.MedicalRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MedicalRecordDAO - Data Access Object for Medical Records.
 * Implements Singleton pattern and proper exception handling.
 */
public class MedicalRecordDAO {
    private static final Logger LOGGER = Logger.getLogger(MedicalRecordDAO.class.getName());
    private DatabaseConnection dbConnection;

    public MedicalRecordDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection initialization failed in MedicalRecordDAO", e);
        }
    }

    /**
     * Create a new medical record
     */
    public boolean createMedicalRecord(MedicalRecord record) {
        String query = "INSERT INTO medical_records (patient_id, doctor_id, record_date, diagnosis, treatment, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getDoctorId());
            stmt.setDate(3, Date.valueOf(record.getRecordDate()));
            stmt.setString(4, record.getDiagnosis());
            stmt.setString(5, record.getTreatment());
            stmt.setString(6, record.getNotes());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating medical record", e);
            return false;
        }
    }

    /**
     * Get medical record by ID
     */
    public MedicalRecord getMedicalRecordById(int id) {
        String query = "SELECT * FROM medical_records WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMedicalRecord(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving medical record: " + id, e);
        }
        return null;
    }

    /**
     * Get all medical records for a patient
     */
    public List<MedicalRecord> getRecordsByPatient(int patientId) {
        List<MedicalRecord> records = new ArrayList<>();
        String query = "SELECT * FROM medical_records WHERE patient_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapMedicalRecord(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving patient records: " + patientId, e);
        }
        return records;
    }

    /**
     * Update medical record
     */
    public boolean updateMedicalRecord(MedicalRecord record) {
        String query = "UPDATE medical_records SET diagnosis = ?, treatment = ?, notes = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, record.getDiagnosis());
            stmt.setString(2, record.getTreatment());
            stmt.setString(3, record.getNotes());
            stmt.setInt(4, record.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating medical record: " + record.getId(), e);
            return false;
        }
    }

    /**
     * Delete medical record
     */
    public boolean deleteMedicalRecord(int recordId) {
        String query = "DELETE FROM medical_records WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recordId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting medical record: " + recordId, e);
            return false;
        }
    }

    /**
     * Map ResultSet to MedicalRecord object
     */
    private MedicalRecord mapMedicalRecord(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setId(rs.getInt("id"));
        record.setPatientId(rs.getInt("patient_id"));
        record.setDoctorId(rs.getInt("doctor_id"));
        record.setRecordDate(rs.getDate("record_date").toLocalDate());
        record.setDiagnosis(rs.getString("diagnosis"));
        record.setTreatment(rs.getString("treatment"));
        record.setNotes(rs.getString("notes"));
        return record;
    }
}
