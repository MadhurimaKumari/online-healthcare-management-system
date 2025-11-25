package com.healthcare.models;

import java.io.Serializable;
import java.time.LocalDate;

public class DoctorSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int scheduleId;
    private int doctorId;
    private LocalDate scheduleDate;
    private String startTime; // HH:mm format
    private String endTime;   // HH:mm format
    private boolean isAvailable;
    private int maxPatients;
    private int currentPatients;
    
    public DoctorSchedule() {}
    
    public DoctorSchedule(int doctorId, LocalDate scheduleDate, String startTime, 
                          String endTime, boolean isAvailable, int maxPatients) {
        this.doctorId = doctorId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.maxPatients = maxPatients;
        this.currentPatients = 0;
    }
    
    public DoctorSchedule(int scheduleId, int doctorId, LocalDate scheduleDate,
                          String startTime, String endTime, boolean isAvailable,
                          int maxPatients, int currentPatients) {
        this.scheduleId = scheduleId;
        this.doctorId = doctorId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.maxPatients = maxPatients;
        this.currentPatients = currentPatients;
    }
    
    // Getters and Setters
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public LocalDate getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(LocalDate scheduleDate) { this.scheduleDate = scheduleDate; }
    
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public int getMaxPatients() { return maxPatients; }
    public void setMaxPatients(int maxPatients) { this.maxPatients = maxPatients; }
    
    public int getCurrentPatients() { return currentPatients; }
    public void setCurrentPatients(int currentPatients) { this.currentPatients = currentPatients; }
    
    public boolean hasSlots() {
        return currentPatients < maxPatients && isAvailable;
    }
    
    @Override
    public String toString() {
        return "DoctorSchedule{" +
                "scheduleId=" + scheduleId +
                ", doctorId=" + doctorId +
                ", scheduleDate=" + scheduleDate +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isAvailable=" + isAvailable +
                ", maxPatients=" + maxPatients +
                ", currentPatients=" + currentPatients +
                '}';
    }
}
