# Java Servlets for Online Healthcare Management System

This guide provides complete servlet implementations for your hospital management system. These servlets handle HTTP requests for appointments, patients, doctors, and user authentication.

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

## Installation Instructions

### Step 1: Create Directory Structure

```bash
# Navigate to your project
cd src/main/java/

# If controller folder doesn't exist
mkdir -p controller
```

### Step 2: Create Servlet Files

Create the following Java files in `src/main/java/controller/`:

#### A. AppointmentServlet.java
#### B. PatientServlet.java  
#### C. DoctorServlet.java
#### D. UserServlet.java

### Step 3: Paste the Code

Copy the servlet code from sections above into respective files.

### Step 4: Update web.xml (if using servlet mapping)

If not using `@WebServlet` annotations, add to `src/main/webapp/WEB-INF/web.xml`:

```xml
<servlet>
    <servlet-name>AppointmentServlet</servlet-name>
    <servlet-class>controller.AppointmentServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>AppointmentServlet</servlet-name>
    <url-pattern>/appointments</url-pattern>
</servlet-mapping>
```

### Step 5: Compile and Deploy

```bash
javac -cp ".:/path/to/mysql-connector-java.jar" src/main/java/controller/*.java
```

### Step 6: Restart Application Server

Restart Tomcat or your servlet container.

---

## Error Handling

All servlets include:
- `try-catch` blocks for exception handling
- Input validation for form data
- Database error logging
- User-friendly error messages via session attributes

Access error messages in JSP:
```jsp
<% String error = (String)session.getAttribute("error"); %>
<% if(error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
<% } %>
```

---

## Database Tables Required

Ensure these tables exist in your `healthcare_db`:

```sql
-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin', 'doctor', 'patient') NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status ENUM('pending', 'confirmed', 'completed', 'cancelled'),
    notes TEXT,
    FOREIGN KEY (patient_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);
```

---

## Usage Examples

### List Appointments
```
GET http://localhost:8080/healthcare/appointments
```

### Create Appointment
```
POST http://localhost:8080/healthcare/appointments
Content-Type: application/x-www-form-urlencoded

patient_id=1&doctor_id=2&appointment_date=2024-12-28&appointment_time=10:00:00&notes=Check-up
```

### View Appointment
```
GET http://localhost:8080/healthcare/appointments?action=view&id=1
```

### Cancel Appointment
```
GET http://localhost:8080/healthcare/appointments?action=cancel&id=1
```

---

## Best Practices

1. **Input Validation:** Always validate user input before processing
2. **SQL Injection Prevention:** Use PreparedStatements (already done in DAO classes)
3. **Error Logging:** Log errors for debugging
4. **Session Management:** Use HttpSession for user state
5. **Request/Response:** Always set appropriate HTTP headers
6. **Redirection Pattern:** Use POST-Redirect-GET pattern

---

## Common Issues & Solutions

### Issue: "Servlet class not found"
- Ensure package name matches: `package controller;`
- Check classpath includes compiled classes

### Issue: "DAO class not found"
- Verify DAO classes exist in `src/main/java/database/`
- Check import statements match your project structure

### Issue: "Database connection failed"
- Verify MySQL server is running
- Check database credentials in DatabaseConnection.java
- Confirm database and tables exist

---

## Next Steps

1. Create JSP views in `src/main/webapp/WEB-INF/views/`
2. Implement error handling pages (404, 500, etc.)
3. Add authentication filters
4. Implement logging framework
5. Add unit tests for servlets

---

## References

- [Servlet API Documentation](https://javaee.github.io/servlet-spec/)
- [HTTP Methods Guide](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)
- [RESTful Design Principles](https://restfulapi.net/)

---

**Author:** Madhuri Kumar  
**Last Updated:** December 27, 2024  
**Project:** Online Healthcare Management System
