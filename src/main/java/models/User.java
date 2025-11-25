package models;

/**
 * Base User class - Parent class for Admin, Doctor, and Patient
 * Demonstrates OOP Inheritance principle
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String role; // admin, doctor, patient
    private String email;
    private String phone;

    // Constructors
    public User() {}

    public User(int id, String username, String passwordHash, String role, String email, String phone) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters - Demonstrates OOP Encapsulation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
