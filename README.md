# Online Healthcare Management System

## Project Overview

The Online Healthcare Management System is a comprehensive Java application designed to manage healthcare operations efficiently. Built with Core Java, JDBC, MySQL, Servlets and JavaFX, it provides both console-based and modern GUI interfaces for managing appointments, medical records, billing, and user administration.

### Key Features:
- ✅ **Dual Interface**: Console (CLI) and Modern GUI (JavaFX)
- ✅ **User Management**: Register, authenticate, update users (Admin, Doctor, Patient)
- ✅ **Appointment Management**: Book, view, update, and cancel appointments
- ✅ **Medical Records**: Doctors can add/view patient medical history
- ✅ **Doctor Schedules**: Manage and view doctor availability
- ✅ **Billing System**: Create invoices and track payments
- ✅ **Reviews & Ratings**: Patients can rate and review doctors (1-5 stars)
- ✅ **System Settings**: Admin-configurable system parameters
- ✅ **Security**: Input validation, SQL injection prevention, bcrypt password hashing
- ✅ **Role-Based Access**: Different dashboards for Admin, Doctor, and Patient

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| Frontend | JavaFX GUI + Console UI |
| Backend | Core Java |
| Database | MySQL (5.7+) |
| Connection | JDBC |
| Security | bcrypt (jBCrypt) |
| Build | Maven Ready |

---

## Project Structure

```
online-healthcare-management-system/
├── database/
│   ├── schema.sql                 # Original database tables
│   └── schema_extended.sql        # Billing & Reviews tables
│
├── src/main/java/
│   ├── database/                  # DAO Classes (7 files)
│   ├── models/                    # Model Classes (11 files)
│   ├── utils/                    # Utilities
|   ├── controller/                # Servlets
│   ├── Main.java                  # Console Application
│   └── HealthcareGUIApp.java      # JavaFX GUI Application
│
├── lib/                        # Dependencies
├── README.md                   # This file
├── SETUP_GUIDE.md              # Detailed setup guide
└── ERROR_FIXES.md              # Common issues and solutions
```

---

## System Requirements

- **Java**: JDK 11 or higher
- **MySQL**: Version 5.7+
- **JavaFX**: 21.0.1 (for GUI)
- **RAM**: Minimum 2GB
- **Storage**: 1GB free disk space

---

## Quick Start

### Console Application
```bash
# Compile
javac -cp "lib/*" -d bin src/main/java/**/*.java

# Run
java -cp "bin:lib/*" Main
```

### GUI Application
```bash
# Compile
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml \
  -cp "lib/*" -d bin src/main/java/**/*.java

# Run
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml \
  -cp "bin:lib/*" HealthcareGUIApp
```

### Default Credentials
- **Admin**: admin / password
- **Doctor**: doctor1 / password
- **Patient**: patient1 / password

---

## Features

### Admin Dashboard
- User management (CRUD)
- View all appointments
- System settings
- Generate reports

### Doctor Dashboard
- View appointments
- Manage medical records
- Schedule management

### Patient Dashboard
- Book appointments
- View medical history
- Check doctor ratings

---

## Database Schema

**7 Tables:**
1. users
2. appointments
3. medical_records
4. doctor_schedules
5. system_settings
6. billing
7. doctor_reviews

**2 Views:**
- doctor_statistics
- patient_billing_history

---

## Setup Instructions

1. **Install dependencies**: MySQL, Java JDK 11+
2. **Clone repository**: `git clone <url>`
3. **Setup database**: Run schema.sql & schema_extended.sql
4. **Configure**: Update database.properties
5. **Compile**: Use commands above
6. **Run**: Choose console or GUI version

For detailed instructions, see [SETUP_GUIDE.md](SETUP_GUIDE.md)

---

## Troubleshooting

- **JDBC Driver error**: Add mysql-connector to classpath
- **Connection refused**: Check MySQL is running
- **Table not found**: Run SQL schema files
- **JavaFX error**: Ensure --module-path is correct

See [ERROR_FIXES.md](ERROR_FIXES.md) for more solutions.

---

## Architecture

**Design Patterns:**
- DAO Pattern for data access
- MVC for GUI separation
- Singleton for connections

**Security:**
- bcrypt password hashing
- SQL injection prevention
- Input validation
- Role-based access control
  
**Web Layer – Java Servlet Integration**
- AppointmentServlet
- PatientServlet
- DoctorServlet
- UserServlet
---

## Future Enhancements

- Email notifications
- Payment gateway integration
- Mobile app
- Advanced analytics
- Telemedicine features

---

## License

MIT License - Open Source

---

## Author

**Madhurima Kumari and Team**

---
