package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Patient model class extending User with patient-specific attributes.
 * Represents a patient in the healthcare system with medical history and health status.
 */
public class Patient extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Date dateOfBirth;
    private String bloodGroup;
    private String allergies;  // Comma-separated list
    private String medicalHistory;
    private String emergencyContact;
    private double height;  // in cm
    private double weight;  // in kg
    
    /**
     * Default constructor for Patient
     */
    public Patient() {
        super();
    }
    
    /**
     * Parameterized constructor for Patient
     */
    public Patient(int userId, String username, String email, String phone,
                   Date dateOfBirth, String bloodGroup, double height, double weight) {
        super(userId, username, email, phone);
        this.dateOfBirth = dateOfBirth;
        this.bloodGroup = bloodGroup;
        this.height = height;
        this.weight = weight;
    }
    
    // Getters and Setters
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public String getAllergies() {
        return allergies;
    }
    
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    /**
     * Calculates BMI (Body Mass Index)
     * @return BMI value
     */
    public double calculateBMI() {
        if (height == 0) return 0;
        return weight / ((height / 100) * (height / 100));
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        
        Patient patient = (Patient) o;
        
        if (Double.compare(patient.height, height) != 0) return false;
        if (Double.compare(patient.weight, weight) != 0) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(patient.dateOfBirth) : patient.dateOfBirth != null)
            return false;
        return bloodGroup != null ? bloodGroup.equals(patient.bloodGroup) : patient.bloodGroup == null;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (bloodGroup != null ? bloodGroup.hashCode() : 0);
        long temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
