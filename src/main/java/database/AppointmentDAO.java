package database;

import models.Appointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentDAO - Data Access Object for Appointments
 * Handles all database operations for appointments
 */
public class AppointmentDAO {
    private DatabaseConnection dbConnection;

    public AppointmentDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    /**
     * Create a new appointment
     */
    public boolean createAppointment(Appointment appointment) {
        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.setString(6, appointment.getNotes());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get appointment by ID
     */
    public Appointment getAppointmentById(int id) {
        String query = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapAppointment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointment: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all appointments for a patient
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE patient_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient appointments: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Get all appointments for a doctor
     */
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE doctor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor appointments: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Update appointment status
     */
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String query = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancel appointment
     */
    public boolean cancelAppointment(int appointmentId) {
        return updateAppointmentStatus(appointmentId, "cancelled");
    }

    /**
     * Delete appointment
     */
    public boolean deleteAppointment(int appointmentId) {
        String query = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all appointments
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all appointments: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Map ResultSet to Appointment object
     */
    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        appointment.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        appointment.setStatus(rs.getString("status"));
        appointment.setNotes(rs.getString("notes"));
        return appointment;
    }
}
