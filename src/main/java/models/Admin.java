package models;

/**
 * Admin model - Extends User with administrative privileges
 */
public class Admin extends User {
    private boolean canManageUsers;
    private boolean canManageSettings;
    private boolean canViewReports;
    private String department;

    public Admin() {
        super();
        this.canManageUsers = true;
        this.canManageSettings = true;
        this.canViewReports = true;
    }

    public Admin(int id, String username, String passwordHash, String email, String phone) {
        super(id, username, passwordHash, "admin", email, phone);
        this.canManageUsers = true;
        this.canManageSettings = true;
        this.canViewReports = true;
    }

    public boolean isCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }

    public boolean isCanManageSettings() {
        return canManageSettings;
    }

    public void setCanManageSettings(boolean canManageSettings) {
        this.canManageSettings = canManageSettings;
    }

    public boolean isCanViewReports() {
        return canViewReports;
    }

    public void setCanViewReports(boolean canViewReports) {
        this.canViewReports = canViewReports;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", department='" + department + '\'' +
                ", canManageUsers=" + canManageUsers +
                ", canManageSettings=" + canManageSettings +
                ", canViewReports=" + canViewReports +
                '}';
    }
}
