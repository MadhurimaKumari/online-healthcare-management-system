package models;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Billing Model - Represents invoice/billing records
 */
public class Billing {
    private int id;
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private BigDecimal amount;
    private String serviceDescription;
    private String paymentStatus; // pending, completed, cancelled
    private LocalDateTime paymentDate;
    private LocalDateTime billDate;
    private String notes;

    public Billing() {}

    public Billing(int appointmentId, int patientId, int doctorId, BigDecimal amount, String serviceDescription) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.amount = amount;
        this.serviceDescription = serviceDescription;
        this.paymentStatus = "pending";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public LocalDateTime getBillDate() { return billDate; }
    public void setBillDate(LocalDateTime billDate) { this.billDate = billDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Billing{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", amount=" + amount +
                ", serviceDescription='" + serviceDescription + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", billDate=" + billDate +
                '}';
    }
}
