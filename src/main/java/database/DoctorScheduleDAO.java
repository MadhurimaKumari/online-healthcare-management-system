package database;

import models.DoctorSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DoctorScheduleDAO - Data Access Object for Doctor Schedules.
 * Implements Singleton pattern and proper exception handling.
 */
public class DoctorScheduleDAO {
    private static final Logger LOGGER = Logger.getLogger(DoctorScheduleDAO.class.getName());
    private DatabaseConnection dbConnection;

    public DoctorScheduleDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection initialization failed in DoctorScheduleDAO", e);
        }
    }

    /**
     * Create a new schedule entry
     */
    public boolean createSchedule(DoctorSchedule schedule) {
        String query = "INSERT INTO doctor_schedules (doctor_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, schedule.getDoctorId());
            stmt.setString(2, schedule.getDayOfWeek());
            stmt.setTime(3, Time.valueOf(schedule.getStartTime()));
            stmt.setTime(4, Time.valueOf(schedule.getEndTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating doctor schedule", e);
            return false;
        }
    }

    /**
     * Get all schedules for a doctor
     */
    public List<DoctorSchedule> getScheduleByDoctor(int doctorId) {
        List<DoctorSchedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM doctor_schedules WHERE doctor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapSchedule(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving doctor schedules: " + doctorId, e);
        }
        return schedules;
    }

    /**
     * Delete a schedule entry
     */
    public boolean deleteSchedule(int scheduleId) {
        String query = "DELETE FROM doctor_schedules WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, scheduleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting schedule: " + scheduleId, e);
            return false;
        }
    }

    /**
     * Map ResultSet to DoctorSchedule object
     */
    private DoctorSchedule mapSchedule(ResultSet rs) throws SQLException {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(rs.getInt("id"));
        schedule.setDoctorId(rs.getInt("doctor_id"));
        schedule.setDayOfWeek(rs.getString("day_of_week"));
        schedule.setStartTime(rs.getTime("start_time").toLocalTime());
        schedule.setEndTime(rs.getTime("end_time").toLocalTime());
        return schedule;
    }
}
