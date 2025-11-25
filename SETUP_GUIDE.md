# Online Healthcare Management System - Setup Guide

## Project Overview

A comprehensive Online Healthcare Management System built with **Core Java** and **OOP principles**. This system enables patients to book appointments, doctors to manage schedules and patient records, and administrators to oversee all operations.

## Technology Stack

- **Language**: Core Java (Java 8+)
- **Database**: MySQL
- **Connectivity**: JDBC
- **Architecture**: OOP-based with MVC pattern
- **UI**: Console-based (Can be upgraded to Swing/JavaFX)

## Project Structure

```
online-healthcare-management-system/
├── database/
│   └── schema.sql                 # Database schema and sample data
├── src/
│   └── main/
│       └── java/
│           ├── models/            # Domain models
│           │   ├── User.java           # Base user class (Inheritance)
│           │   ├── Doctor.java         # Extends User
│           │   ├── Patient.java        # Extends User
│           │   ├── Admin.java          # Extends User
│           │   ├── Appointment.java    # Appointment model
│           │   └── MedicalRecord.java  # Medical record model
│           │
│           ├── database/          # Data access layer (DAOs)
│           │   ├── DatabaseConnection.java  # Singleton pattern
│           │   ├── UserDAO.java            # User CRUD operations
│           │   ├── AppointmentDAO.java     # Appointment CRUD
│           │   ├── DoctorDAO.java          # Doctor operations
│           │   └── MedicalRecordDAO.java   # Medical record operations
│           │
│           ├── services/          # Business logic layer
│           │   ├── UserService.java        # User management
│           │   ├── AppointmentService.java # Appointment management
│           │   ├── DoctorService.java      # Doctor operations
│           │   └── AuthenticationService.java # Login/Auth
│           │
│           ├── ui/                # User interface layer
│           │   ├── AdminDashboard.java     # Admin console UI
│           │   ├── DoctorDashboard.java    # Doctor console UI
│           │   ├── PatientDashboard.java   # Patient console UI
│           │   └── Main.java               # Entry point
│           │
│           └── utils/             # Utility classes
│               ├── PasswordUtil.java       # Password hashing
│               └── ValidationUtil.java     # Input validation
│
├── resources/
│   └── database.properties        # Database configuration
│
├── SETUP_GUIDE.md                # This file
├── README.md                      # Project description
└── .gitignore                     # Git ignore file
```

## Database Setup

### Prerequisites
- MySQL Server installed and running
- MySQL CLI or MySQL Workbench

### Steps to Setup Database

1. Open MySQL command line or workbench
2. Run the SQL script:
   ```sql
   source database/schema.sql;
   ```

3. Verify tables:
   ```sql
   USE healthcare_db;
   SHOW TABLES;
   ```

### Default Credentials (After Setup)
- **Admin**: username=`admin`, password=`admin` (SHA-256 hashed)
- **Doctor**: username=`doctor1`, password=`admin`
- **Patient**: username=`patient1`, password=`admin`

## OOP Principles Implementation

### 1. Inheritance
- `User` is the base class
- `Doctor`, `Patient`, and `Admin` extend `User`
- Each subclass adds specific attributes and methods

### 2. Encapsulation
- Private fields with public getters/setters
- Data hiding and controlled access
- All model classes follow this pattern

### 3. Polymorphism
- Service classes use overridden methods for role-specific logic
- Different dashboards implement similar interfaces differently
- Abstract methods for common operations

### 4. Abstraction
- DAO classes abstract database operations
- Services abstract business logic
- Users interact only with high-level interfaces

## Key Classes and Their Responsibilities

### Models (src/main/java/models/)
- **User.java**: Base class with common user properties
- **Doctor.java**: Doctor-specific attributes (specialization, experience)
- **Patient.java**: Patient-specific attributes (medical history)
- **Admin.java**: Admin-specific operations
- **Appointment.java**: Appointment details and status
- **MedicalRecord.java**: Patient medical history

### Database Layer (src/main/java/database/)
- **DatabaseConnection.java**: Singleton pattern for DB connection
- **UserDAO.java**: CRUD operations for users
- **AppointmentDAO.java**: Appointment management in DB
- **DoctorDAO.java**: Doctor schedules and records
- **MedicalRecordDAO.java**: Medical record operations

### Service Layer (src/main/java/services/)
- **UserService.java**: User management logic
- **AppointmentService.java**: Appointment booking and management
- **DoctorService.java**: Doctor-specific operations
- **AuthenticationService.java**: Login and authentication

### UI Layer (src/main/java/ui/)
- **Main.java**: Entry point with login
- **AdminDashboard.java**: Admin interface and operations
- **DoctorDashboard.java**: Doctor interface and operations
- **PatientDashboard.java**: Patient interface and operations

## Features by User Type

### Patient Features
- Register and manage profile
- Book appointments with doctors
- View appointment history
- View and manage medical records
- Cancel appointments

### Doctor Features
- Manage appointment schedule
- View patient information
- Update patient medical records
- Confirm/reschedule appointments
- View upcoming appointments

### Admin Features
- User management (CRUD operations)
- Manage all appointments
- Configure system settings
- View system analytics
- Manage doctor schedules

## JDBC Connectivity

### Database Configuration
- Host: localhost
- Port: 3306
- Database: healthcare_db
- Driver: com.mysql.cj.jdbc.Driver

### Connection Management
- Singleton pattern ensures single connection instance
- Connection pooling can be implemented for production
- All queries use PreparedStatement for SQL injection prevention

## Running the Application

### Prerequisites
- Java 8+ installed
- MySQL database configured
- Database schema created (using schema.sql)

### Compilation
```bash
javac -d bin src/main/java/**/*.java
```

### Execution
```bash
java -cp bin ui.Main
```

## Implementation Roadmap

1. **Phase 1**: Core Models (User, Doctor, Patient, Admin) ✓
2. **Phase 2**: Database Layer (DatabaseConnection, DAOs)
3. **Phase 3**: Service Layer (Business Logic)
4. **Phase 4**: UI Layer (Dashboards and Main)
5. **Phase 5**: Testing and Deployment

## Security Considerations

- Passwords are hashed using SHA-256
- PreparedStatements prevent SQL injection
- Role-based access control implemented
- Input validation on all user inputs
- Session management for authenticated users

## Testing

Test each component:
- Unit tests for model classes
- Integration tests for DAOs
- Functional tests for services
- UI tests for dashboards

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check connection credentials in database.properties
- Ensure healthcare_db database exists

### Compilation Errors
- Verify Java version (8+)
- Check all class imports are correct
- Ensure MySQL JDBC driver is in classpath

## Future Enhancements

- Web-based UI using Spring Boot + Thymeleaf
- REST APIs for mobile app integration
- Advanced analytics and reporting
- Email notifications for appointments
- SMS alerts integration
- Video consultation capability
- Electronic health records (EHR) system

## References

- Java OOP Concepts: https://docs.oracle.com/javase/tutorial/java/concepts/
- JDBC Documentation: https://docs.oracle.com/javase/tutorial/jdbc/
- MySQL Documentation: https://dev.mysql.com/doc/
- Design Patterns: https://www.geeksforgeeks.org/design-patterns-in-java/

## License

This project is open source and available for educational purposes.

## Contact & Support

For questions or support, please refer to the project documentation or create an issue in the repository.
