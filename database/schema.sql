-- Create database
CREATE DATABASE IF NOT EXISTS healthcare_db;
USE healthcare_db;

-- Users table (base for admin, doctor, patient)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin', 'doctor', 'patient') NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status ENUM('pending', 'confirmed', 'completed', 'cancelled') DEFAULT 'pending',
    notes TEXT,
    FOREIGN KEY (patient_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Patient medical records
CREATE TABLE IF NOT EXISTS medical_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    record_date DATE NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    FOREIGN KEY (patient_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Doctor schedules
CREATE TABLE IF NOT EXISTS doctor_schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- System settings
CREATE TABLE IF NOT EXISTS system_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT NOT NULL
);


INSERT INTO users (username, password_hash, role, email, phone) VALUES
('admin', '$2a$12$R9h/lZssNLoY789nzS70Xunx7XnFv.zXU6l/k5yKzI0k/I1Gk6Z6e', 'admin', 'admin@hospital.com', '123-456-7890'),
('doctor1', '$2a$12$R9h/lZssNLoY789nzS70Xunx7XnFv.zXU6l/k5yKzI0k/I1Gk6Z6e', 'doctor', 'doctor1@hospital.com', '123-456-7891'),
('patient1', '$2a$12$R9h/lZssNLoY789nzS70Xunx7XnFv.zXU6l/k5yKzI0k/I1Gk6Z6e', 'patient', 'patient1@hospital.com', '123-456-7892');

INSERT INTO doctor_schedules (doctor_id, day_of_week, start_time, end_time) VALUES
(2, 'Monday', '09:00:00', '17:00:00'),
(2, 'Tuesday', '09:00:00', '17:00:00');

INSERT INTO system_settings (setting_key, setting_value) VALUES
('max_appointments_per_day', '10'),
('hospital_name', 'City Hospital');
