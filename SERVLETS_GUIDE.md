# Java Servlets for Online Healthcare Management System


## Table of Contents
1. [AppointmentServlet](#appointmentservlet)
2. [PatientServlet](#patientservlet)
3. [DoctorServlet](#doctorservlet)
4. [UserServlet](#userservlet)
5. [Installation Instructions](#installation-instructions)

---

## AppointmentServlet

**Location:** `src/main/java/controller/AppointmentServlet.java`

**Purpose:** Manages appointment operations (list, create, view, cancel)

**URL Mapping:** `/appointments`

### Features:
- **GET /appointments** - List all appointments
- **GET /appointments?action=view&id=X** - View specific appointment
- **GET /appointments?action=form** - Show appointment booking form
- **GET /appointments?action=cancel&id=X** - Cancel appointment
- **POST /appointments** - Create new appointment

### Parameters (POST):
- `patient_id` (int) - Patient ID
- `doctor_id` (int) - Doctor ID
- `appointment_date` (String) - Date (YYYY-MM-DD)
- `appointment_time` (String) - Time (HH:MM:SS)
- `notes` (String) - Additional notes (optional)

### Dependencies:
- AppointmentDAO
- Appointment model
- DatabaseConnection

---

## PatientServlet

**Location:** `src/main/java/controller/PatientServlet.java`

**Purpose:** Manages patient information (CRUD operations)

**URL Mapping:** `/patients`

### Features:
- **GET /patients** - List all patients
- **GET /patients?action=view&id=X** - View patient details
- **GET /patients?action=form** - Show registration form
- **POST /patients** - Create/register new patient
- **POST /patients?action=update** - Update patient info
- **GET /patients?action=delete&id=X** - Delete patient

### Parameters (POST):
- `username` (String) - Patient username
- `password` (String) - Patient password
- `email` (String) - Email address
- `phone` (String) - Contact number
- (Additional fields as per your Patient model)

---

## DoctorServlet

**Location:** `src/main/java/controller/DoctorServlet.java`

**Purpose:** Manages doctor information and schedules

**URL Mapping:** `/doctors`

### Features:
- **GET /doctors** - List all doctors
- **GET /doctors?action=view&id=X** - View doctor profile
- **GET /doctors?action=schedule&id=X** - View doctor schedule
- **POST /doctors** - Create doctor account
- **POST /doctors?action=schedule** - Set doctor schedule

### Dependencies:
- DoctorDAO
- DoctorScheduleDAO
- Doctor model

---

## UserServlet

**Location:** `src/main/java/controller/UserServlet.java`

**Purpose:** Handles user authentication and login/logout

**URL Mapping:** `/user` or `/auth`

### Features:
- **GET /user?action=login** - Show login form
- **POST /user?action=login** - Process login
- **GET /user?action=logout** - Logout user
- **GET /user?action=profile** - View user profile
- **POST /user?action=register** - Register new user

### Parameters (POST - Login):
- `username` (String) - Username
- `password` (String) - Password

---


