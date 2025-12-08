package database;

import models.DoctorSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorScheduleDAO {
    private DatabaseConnection dbConnection;

    public DoctorScheduleDAO() {
        this.dbConnection = new DatabaseConnection();
    }

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
            System.err.println("Error creating schedule: " + e.getMessage());
            return false;
        }
    }

    public List<DoctorSchedule> getScheduleByDoctor(int doctorId) {
        List<DoctorSchedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM doctor_schedules WHERE doctor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                schedules.add(mapSchedule(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving schedules: " + e.getMessage());
        }
        return schedules;
    }

    public boolean updateSchedule(DoctorSchedule schedule) {
        String query = "UPDATE doctor_schedules SET start_time = ?, end_time = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTime(1, Time.valueOf(schedule.getStartTime()));
            stmt.setTime(2, Time.valueOf(schedule.getEndTime()));
            stmt.setInt(3, schedule.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating schedule: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSchedule(int scheduleId) {
        String query = "DELETE FROM doctor_schedules WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, scheduleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting schedule: " + e.getMessage());
            return false;
        }
    }

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
