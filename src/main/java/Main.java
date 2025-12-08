import database.*;
import models.*;
import java.util.*;

/**
 * Main.java - Application Entry Point with Authentication System
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static DoctorDAO doctorDAO = new DoctorDAO();
    private static PatientDAO patientDAO = new PatientDAO();
    private static MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private static DoctorScheduleDAO doctorScheduleDAO = new DoctorScheduleDAO();
    private static SystemSettingsDAO systemSettingsDAO = new SystemSettingsDAO();
    
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("\n========== ONLINE HEALTHCARE MANAGEMENT SYSTEM ==========");
        System.out.println("Version: 1.0 - Phase 3 Complete");
        System.out.println("======================================================\n");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showRoleBasedMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Exit");
        System.out.print("Choose option: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void login() {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        // Get user from database
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) {
            currentUser = user;
            System.out.println("\nLogin successful! Welcome, " + username);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void showRoleBasedMenu() {
        String role = currentUser.getRole();
        
        if ("admin".equalsIgnoreCase(role)) {
            showAdminMenu();
        } else if ("doctor".equalsIgnoreCase(role)) {
            showDoctorMenu();
        } else if ("patient".equalsIgnoreCase(role)) {
            showPatientMenu();
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n=== ADMIN DASHBOARD ===");
        System.out.println("1. Manage Users");
        System.out.println("2. View All Appointments");
        System.out.println("3. System Settings");
        System.out.println("4. View Reports");
        System.out.println("5. Logout");
        System.out.print("Choose option: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                manageUsers();
                break;
            case 2:
                viewAllAppointments();
                break;
            case 3:
                manageSettings();
                break;
            case 4:
                viewReports();
                break;
            case 5:
                currentUser = null;
                System.out.println("Logged out.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void showDoctorMenu() {
        System.out.println("\n=== DOCTOR DASHBOARD ===");
        System.out.println("1. View Appointments");
        System.out.println("2. View Medical Records");
        System.out.println("3. Manage Schedule");
        System.out.println("4. Logout");
        System.out.print("Choose option: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewDoctorAppointments();
                break;
            case 2:
                viewMedicalRecords();
                break;
            case 3:
                manageSchedule();
                break;
            case 4:
                currentUser = null;
                System.out.println("Logged out.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void showPatientMenu() {
        System.out.println("\n=== PATIENT DASHBOARD ===");
        System.out.println("1. Book Appointment");
        System.out.println("2. View Appointments");
        System.out.println("3. View Medical Records");
        System.out.println("4. Logout");
        System.out.print("Choose option: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                bookAppointment();
                break;
            case 2:
                viewPatientAppointments();
                break;
            case 3:
                viewPatientMedicalRecords();
                break;
            case 4:
                currentUser = null;
                System.out.println("Logged out.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void manageUsers() {
        System.out.println("\n=== USER MANAGEMENT ===");
        System.out.println("1. View All Users");
        System.out.println("2. Delete User");
        System.out.println("3. Back");
        System.out.print("Choose option: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                List<User> users = userDAO.getAllUsers();
                for (User u : users) {
                    System.out.println(u);
                }
                break;
            case 2:
                System.out.print("Enter User ID: ");
                int userId = getIntInput();
                userDAO.deleteUser(userId);
                System.out.println("User deleted.");
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    private static void manageSettings() {
        System.out.println("\n=== SYSTEM SETTINGS ===");
        Map<String, String> settings = systemSettingsDAO.getAllSettings();
        for (String key : settings.keySet()) {
            System.out.println(key + ": " + settings.get(key));
        }
    }

    private static void viewReports() {
        System.out.println("\n=== SYSTEM REPORTS ===");
        System.out.println("Total Appointments: " + appointmentDAO.getAllAppointments().size());
        System.out.println("Total Doctors: " + doctorDAO.getAllDoctors().size());
        System.out.println("Total Patients: " + patientDAO.getAllPatients().size());
    }

    private static void viewDoctorAppointments() {
        List<Appointment> appointments = appointmentDAO.getAppointmentsByDoctor(currentUser.getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointments.");
        } else {
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    private static void viewMedicalRecords() {
        System.out.print("Enter Patient ID: ");
        int patientId = getIntInput();
        List<MedicalRecord> records = medicalRecordDAO.getRecordsByPatient(patientId);
        for (MedicalRecord r : records) {
            System.out.println(r);
        }
    }

    private static void manageSchedule() {
        System.out.println("\n=== MANAGE SCHEDULE ===");
        List<DoctorSchedule> schedules = doctorScheduleDAO.getScheduleByDoctor(currentUser.getId());
        for (DoctorSchedule s : schedules) {
            System.out.println(s);
        }
    }

    private static void bookAppointment() {
        System.out.println("\n=== BOOK APPOINTMENT ===");
        System.out.print("Select Doctor ID: ");
        int doctorId = getIntInput();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Time (HH:MM:SS): ");
        String time = scanner.nextLine();
        
        Appointment apt = new Appointment();
        apt.setPatientId(currentUser.getId());
        apt.setDoctorId(doctorId);
        apt.setAppointmentDate(java.time.LocalDate.parse(date));
        apt.setAppointmentTime(java.time.LocalTime.parse(time));
        apt.setStatus("pending");
        
        appointmentDAO.createAppointment(apt);
        System.out.println("Appointment booked successfully!");
    }

    private static void viewPatientAppointments() {
        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatient(currentUser.getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointments.");
        } else {
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    private static void viewPatientMedicalRecords() {
        List<MedicalRecord> records = medicalRecordDAO.getRecordsByPatient(currentUser.getId());
        if (records.isEmpty()) {
            System.out.println("No medical records.");
        } else {
            for (MedicalRecord r : records) {
                System.out.println(r);
            }
        }
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
