package models;

import java.io.Serializable;

/**
 * Doctor model class extending User with doctor-specific attributes.
 * Represents a doctor in the healthcare system with specialization and experience.
 * Implements Serializable for potential database operations and caching.
 */
public class Doctor extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String specialization;  // e.g., "Cardiology", "Orthopedics"
    private int yearsOfExperience;
    private String licenseNumber;
    private boolean isAvailable;
    private double consultationFee;
    
    /**
     * Default constructor for Doctor
     */
    public Doctor() {
        super();
        this.isAvailable = true;
    }
    
    /**
     * Parameterized constructor for Doctor
     * @param userId User ID
     * @param username Username
     * @param email Email address
     * @param phone Phone number
     * @param specialization Doctor's specialization
     * @param yearsOfExperience Years of experience
     * @param licenseNumber Medical license number
     * @param consultationFee Consultation fee
     */
    public Doctor(int userId, String username, String email, String phone,
                  String specialization, int yearsOfExperience, 
                  String licenseNumber, double consultationFee) {
        super(userId, username, email, phone);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.licenseNumber = licenseNumber;
        this.consultationFee = consultationFee;
        this.isAvailable = true;
    }
    
    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    /**
     * Override toString() for better logging and debugging
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", isAvailable=" + isAvailable +
                ", consultationFee=" + consultationFee +
                '}';
    }
    
    /**
     * Override equals() for proper object comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        
        Doctor doctor = (Doctor) o;
        
        if (yearsOfExperience != doctor.yearsOfExperience) return false;
        if (isAvailable != doctor.isAvailable) return false;
        if (Double.compare(doctor.consultationFee, consultationFee) != 0) return false;
        if (specialization != null ? !specialization.equals(doctor.specialization) : doctor.specialization != null)
            return false;
        return licenseNumber != null ? licenseNumber.equals(doctor.licenseNumber) : doctor.licenseNumber == null;
    }
    
    /**
     * Override hashCode() for use in hash-based collections
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (specialization != null ? specialization.hashCode() : 0);
        result = 31 * result + yearsOfExperience;
        result = 31 * result + (licenseNumber != null ? licenseNumber.hashCode() : 0);
        result = 31 * result + (isAvailable ? 1 : 0);
        long temp = Double.doubleToLongBits(consultationFee);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
