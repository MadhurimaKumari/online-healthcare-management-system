-- Extended Database Schema for Billing and Reviews
-- Run these queries to add new features to your healthcare database

USE healthcare_db;

-- Billing and Invoicing Table
CREATE TABLE IF NOT EXISTS billing (
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    service_description VARCHAR(255),
    payment_status ENUM('pending', 'completed', 'cancelled') DEFAULT 'pending',
    payment_date DATETIME,
    bill_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Doctor Reviews and Ratings Table
CREATE TABLE IF NOT EXISTS doctor_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    appointment_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE SET NULL
);

-- Doctor Statistics View
CREATE VIEW IF NOT EXISTS doctor_statistics AS
SELECT 
    d.id,
    d.username,
    COUNT(DISTINCT a.id) as total_appointments,
    ROUND(AVG(CASE WHEN dr.rating IS NOT NULL THEN dr.rating ELSE 0 END), 2) as average_rating,
    COUNT(DISTINCT dr.id) as total_reviews,
    SUM(CASE WHEN b.payment_status = 'completed' THEN b.amount ELSE 0 END) as total_revenue
FROM users d
LEFT JOIN appointments a ON d.id = a.doctor_id
LEFT JOIN doctor_reviews dr ON d.id = dr.doctor_id
LEFT JOIN billing b ON a.id = b.appointment_id
WHERE d.role = 'doctor'
GROUP BY d.id, d.username;

-- Patient Billing History View
CREATE VIEW IF NOT EXISTS patient_billing_history AS
SELECT 
    p.id,
    p.username,
    COUNT(DISTINCT b.id) as total_bills,
    SUM(CASE WHEN b.payment_status = 'completed' THEN b.amount ELSE 0 END) as total_paid,
    SUM(CASE WHEN b.payment_status = 'pending' THEN b.amount ELSE 0 END) as total_pending
FROM users p
LEFT JOIN billing b ON p.id = b.patient_id
WHERE p.role = 'patient'
GROUP BY p.id, p.username;

-- Insert sample billing data
INSERT INTO billing (appointment_id, patient_id, doctor_id, amount, service_description, payment_status)
VALUES 
    (1, 3, 2, 500.00, 'General Consultation', 'completed'),
    (2, 3, 2, 300.00, 'Follow-up Consultation', 'pending');

-- Insert sample reviews
INSERT INTO doctor_reviews (doctor_id, patient_id, appointment_id, rating, review_text)
VALUES 
    (2, 3, 1, 5, 'Excellent doctor, very professional and caring'),
    (2, 3, 2, 4, 'Good consultation, very helpful advice');

-- Verification queries
SELECT 'Billing table created successfully' as status;
SELECT 'Doctor Reviews table created successfully' as status;
SELECT 'Views created successfully' as status;
