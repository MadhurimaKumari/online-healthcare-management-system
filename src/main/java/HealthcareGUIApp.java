import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import database.*;
import models.*;
import utils.PasswordUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HealthcareGUIApp - Main JavaFX Application for Online Healthcare Management
 * Provides modern GUI interface for login and role-based dashboards.
 * Corrected to use secure password verification and Singleton DAOs.
 */
public class HealthcareGUIApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(HealthcareGUIApp.class.getName());
    private Stage primaryStage;
    private UserDAO userDAO;
    private AppointmentDAO appointmentDAO;
    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;
    private MedicalRecordDAO medicalRecordDAO;
    private User currentUser;

    @Override
    public void init() {
        // Initialize DAOs safely using updated constructors
        userDAO = new UserDAO();
        appointmentDAO = new AppointmentDAO();
        doctorDAO = new DoctorDAO();
        patientDAO = new PatientDAO();
        medicalRecordDAO = new MedicalRecordDAO();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Online Healthcare Management System");
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        showLoginWindow();
        primaryStage.show();
    }

    private void showLoginWindow() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f0f0f0;");
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Healthcare Management System");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white;");
        loginBox.setPrefWidth(350);

        Label loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(35);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(35);

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10; -fx-cursor: hand;");

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Username and password required!");
                return;
            }

            try {
                User user = userDAO.getUserByUsername(username);
                // Corrected: Use PasswordUtil.verifyPassword for secure login
                if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                    currentUser = user;
                    showDashboard();
                } else {
                    errorLabel.setText("Invalid credentials!");
                    passwordField.clear();
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Login error", ex);
                errorLabel.setText("Database connection error.");
            }
        });

        loginBox.getChildren().addAll(loginLabel, usernameField, passwordField, loginButton, errorLabel);
        root.getChildren().addAll(titleLabel, new Separator(), loginBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    private void showDashboard() {
        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            showAdminDashboard();
        } else if ("doctor".equalsIgnoreCase(currentUser.getRole())) {
            showDoctorDashboard();
        } else if ("patient".equalsIgnoreCase(currentUser.getRole())) {
            showPatientDashboard();
        }
    }

    private void showAdminDashboard() {
        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu dashboardMenu = new Menu("Dashboard");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> showLoginWindow());
        dashboardMenu.getItems().add(logoutItem);
        menuBar.getMenus().add(dashboardMenu);

        VBox sidebar = createSidebar(new String[]{"View Users", "Manage Appointments", "System Settings", "View Reports"});

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setStyle("-fx-background-color: #ecf0f1;");

        Label welcomeLabel = new Label("Welcome Admin: " + currentUser.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TableView<User> usersTable = new TableView<>();
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        usersTable.getColumns().addAll(idCol, usernameCol, roleCol);

        List<User> users = userDAO.getAllUsers();
        usersTable.getItems().addAll(users);

        centerContent.getChildren().addAll(welcomeLabel, new Separator(), new Label("All Users:"), usersTable);

        root.setTop(menuBar);
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    private void showDoctorDashboard() {
        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu dashboardMenu = new Menu("Dashboard");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> showLoginWindow());
        dashboardMenu.getItems().add(logoutItem);
        menuBar.getMenus().add(dashboardMenu);

        VBox sidebar = createSidebar(new String[]{"View Appointments", "View Medical Records", "Manage Schedule"});

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setStyle("-fx-background-color: #ecf0f1;");

        Label welcomeLabel = new Label("Welcome Doctor: " + currentUser.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TableView<Appointment> appointmentsTable = new TableView<>();
        TableColumn<Appointment, Integer> aptIdCol = new TableColumn<>("ID");
        TableColumn<Appointment, Integer> patientCol = new TableColumn<>("Patient ID");
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        appointmentsTable.getColumns().addAll(aptIdCol, patientCol, statusCol);

        List<Appointment> appointments = appointmentDAO.getAppointmentsByDoctor(currentUser.getId());
        appointmentsTable.getItems().addAll(appointments);

        centerContent.getChildren().addAll(welcomeLabel, new Separator(), new Label("Your Appointments:"), appointmentsTable);

        root.setTop(menuBar);
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    private void showPatientDashboard() {
        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu dashboardMenu = new Menu("Dashboard");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> showLoginWindow());
        dashboardMenu.getItems().add(logoutItem);
        menuBar.getMenus().add(dashboardMenu);

        VBox sidebar = createSidebar(new String[]{"Book Appointment", "View Appointments", "Medical History"});

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setStyle("-fx-background-color: #ecf0f1;");

        Label welcomeLabel = new Label("Welcome Patient: " + currentUser.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TableView<Appointment> appointmentsTable = new TableView<>();
        TableColumn<Appointment, Integer> aptIdCol = new TableColumn<>("ID");
        TableColumn<Appointment, Integer> doctorCol = new TableColumn<>("Doctor ID");
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        appointmentsTable.getColumns().addAll(aptIdCol, doctorCol, statusCol);

        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatient(currentUser.getId());
        appointmentsTable.getItems().addAll(appointments);

        centerContent.getChildren().addAll(welcomeLabel, new Separator(), new Label("Your Appointments:"), appointmentsTable);

        root.setTop(menuBar);
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    private VBox createSidebar(String[] items) {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #34495e;");
        sidebar.setPrefWidth(200);

        for (String item : items) {
            Button btn = new Button(item);
            btn.setPrefWidth(180);
            btn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #2c3e50; -fx-text-fill: white; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #16a085; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 10; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-size: 12; -fx-padding: 10; -fx-cursor: hand;"));
            sidebar.getChildren().add(btn);
        }
        return sidebar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
