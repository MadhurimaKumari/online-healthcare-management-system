package database;

import models.MedicalRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MedicalRecordDAO - Data Access Object for Medical Records
 */
public class MedicalRecordDAO {
    private DatabaseConnection dbConnection;

    public MedicalRecordDAO() {
        this.dbConnection = new DatabaseConnection();
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
            System.err.println("Error creating medical record: " + e.getMessage());
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
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapMedicalRecord(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving medical record: " + e.getMessage());
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
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(mapMedicalRecord(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient records: " + e.getMessage());
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
            System.err.println("Error updating medical record: " + e.getMessage());
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
            System.err.println("Error deleting medical record: " + e.getMessage());
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
