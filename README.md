Online Healthcare Management System
Project Overview
The Online Healthcare Management System is a console-based Java application designed to manage healthcare operations efficiently. It supports user roles (Admin, Doctor, Patient) with features like user management, appointment booking, medical records, and system settings. Built using Core Java with OOP principles, JDBC for database connectivity, and secure password hashing with bcrypt.

Features
User Management: Register, authenticate, update, and delete users (Admin, Doctor, Patient).
Appointment Management: Book, view, update, and cancel appointments.
Medical Records: Doctors can add/view patient records.
Doctor Schedules: Manage and view doctor availability.
System Settings: Admins can configure system-wide settings.
Security: Input validation, SQL injection prevention, and bcrypt password hashing.


Requirements

Software Prerequisites
Java: JDK 8 or higher (e.g., OpenJDK 11+ recommended).
MySQL: Version 5.7 or higher (with MySQL Server running locally or remotely).
IDE: IntelliJ IDEA, Eclipse, or any Java-compatible IDE (optional, but recommended for development).
Build Tool: None required (pure Java), but Maven/Gradle can be used for dependency management.


Hardware Prerequisites
Minimum 2GB RAM, 1GB free disk space.
Stable internet connection for remote DB access (if not local).


Dependencies
MySQL Connector/J: Download from MySQL Downloads (e.g., mysql-connector-java-8.0.33.jar).
jBCrypt: Download from jBCrypt GitHub (e.g., jbcrypt-0.4.jar).
Place JARs in the project root or add to classpath.


Setup Instructions
1. Clone or Download the Project
Download the project files and place them in a directory (e.g., HealthcareManagementSystem).
Ensure the project structure matches:


HealthcareManagementSystem/
├── src/main/java/
│   ├── models/ (User.java, Doctor.java, etc.)
│   ├── database/ (DatabaseConnection.java, UserDAO.java, etc.)
│   ├── services/ (UserService.java, etc.)
│   ├── ui/ (AdminDashboard.java, Main.java, etc.)
│   └── resources/ (database.properties)
├── lib/ (Place JAR dependencies here)
└── README.md




2. Set Up the Database
Install and start MySQL Server.
Create a database named healthcare_db.
Run the SQL script to create tables and insert sample data (from the initial schema provided):
sql


-- Run this in MySQL Workbench or command line
CREATE DATABASE healthcare_db;
USE healthcare_db;
-- [Paste the full schema from earlier messages]
Ensure the roles table has data: INSERT INTO roles (roleId, roleName) VALUES (1, 'admin'), (2, 'doctor'), (3, 'patient');.




3. Configure Database Connection
Edit src/main/resources/database.properties:

db.url=jdbc:mysql://localhost:3306/healthcare_db?useSSL=false&serverTimezone=UTC&autoReconnect=true
db.username=your_mysql_username
db.password=your_mysql_password
Replace placeholders with your MySQL credentials.



4. Add Dependencies to Classpath
Place mysql-connector-java-8.0.33.jar and jbcrypt-0.4.jar in the lib/ directory.
When compiling/running, include them in the classpath (see commands below).


6. Compile the Project
Open a terminal in the project root.
Compile all Java files:

javac -cp "lib/*" -d bin src/main/java/**/*.java
This compiles to a bin/ directory (create it if needed).


6. Run the Project
Run the main class (assuming Main.java in ui/):

java -cp "bin:lib/*" ui.Main

Follow the console prompts to log in (e.g., default admin: username admin, password password – update in DB if needed).
Use role-based menus: Admin for management, Doctor for records/schedules, Patient for bookings.


Usage
Login: Enter username and password at startup.
Admin Dashboard: Manage users, appointments, settings.
Doctor Dashboard: View/update schedules, add/view medical records.
Patient Dashboard: Book/view appointments, view medical history.
Navigation: Use numbered menus in the console. Type 'exit' to quit.



Troubleshooting
Compilation Errors: Ensure JDK is installed (java -version). Check for missing imports or JARs in classpath.
Database Connection Issues: Verify MySQL is running, credentials are correct, and DB exists. Check firewall for port 3306.
Runtime Errors: Check logs (console output) for exceptions. Ensure tables are created and populated.
Password Issues: Passwords are hashed; use authenticateUser for login. Reset via DB if needed.
Common Fixes: Restart MySQL, update JAR versions, or run as administrator.


Contributing
Fork the repository, make changes, and submit a pull request.
Follow Java coding standards and add unit tests (e.g., JUnit).


Authors
Developed as part of a Core Java project. Contact for support.
