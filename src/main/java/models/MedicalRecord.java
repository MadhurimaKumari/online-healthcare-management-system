package com.healthcare.models;

import java.io.Serializable;
import java.time.LocalDate;

public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int recordId;
    private int patientId;
    private int doctorId;
    private LocalDate recordDate;
    private String diagnosis;
    private String prescription;
    private String treatment;
    private String notes;
    
    public MedicalRecord() {}
    
    public MedicalRecord(int patientId, int doctorId, LocalDate recordDate, 
                         String diagnosis, String prescription, String treatment) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.recordDate = recordDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.treatment = treatment;
    }
    
    public MedicalRecord(int recordId, int patientId, int doctorId, LocalDate recordDate,
                         String diagnosis, String prescription, String treatment, String notes) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.recordDate = recordDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.treatment = treatment;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordId=" + recordId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", recordDate=" + recordDate +
                ", diagnosis='" + diagnosis + '\'' +
                ", prescription='" + prescription + '\'' +
                ", treatment='" + treatment + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
