package utils;

import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * Utility class for input validation with comprehensive checks for user data.
 * Prevents invalid data from reaching the database layer.
 * Follows encapsulation and single responsibility principles.
 */
public class ValidationUtil {
    private static final Logger logger = Logger.getLogger(ValidationUtil.class.getName());
    
    // Validation patterns
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9\\-\\+\\s()]{10,20}$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 128;
    
    /**
     * Validates username format and length.
     * @param username The username to validate (alphanumeric and underscore, 3-20 chars)
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            logger.warning("Username is null or empty.");
            return false;
        }
        boolean isValid = USERNAME_PATTERN.matcher(username).matches();
        if (!isValid) {
            logger.warning("Invalid username format: " + username);
        }
        return isValid;
    }
    
    /**
     * Validates email format.
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.warning("Email is null or empty.");
            return false;
        }
        boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        if (!isValid) {
            logger.warning("Invalid email format: " + email);
        }
        return isValid;
    }
    
    /**
     * Validates phone number format.
     * @param phone The phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            logger.warning("Phone is null or empty.");
            return false;
        }
        boolean isValid = PHONE_PATTERN.matcher(phone).matches();
        if (!isValid) {
            logger.warning("Invalid phone format: " + phone);
        }
        return isValid;
    }
    
    /**
     * Validates password strength (length and non-empty).
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            logger.warning("Password is null or empty.");
            return false;
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            logger.warning("Password length must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH);
            return false;
        }
        return true;
    }
    
    /**
     * Validates a complete user object for registration.
     * @param username The username to validate
     * @param email The email to validate
     * @param password The password to validate
     * @param phone The phone to validate (optional, can be null)
     * @return true if all required fields are valid, false otherwise
     */
    public static boolean isValidUser(String username, String email, String password, String phone) {
        if (!isValidUsername(username)) {
            logger.warning("Validation failed: Invalid username.");
            return false;
        }
        if (!isValidEmail(email)) {
            logger.warning("Validation failed: Invalid email.");
            return false;
        }
        if (!isValidPassword(password)) {
            logger.warning("Validation failed: Invalid password.");
            return false;
        }
        // Phone is optional but if provided, must be valid
        if (phone != null && !phone.isEmpty() && !isValidPhone(phone)) {
            logger.warning("Validation failed: Invalid phone.");
            return false;
        }
        logger.info("User validation successful for username: " + username);
        return true;
    }
    
    /**
     * Validates if a number is positive (for IDs, counts, etc.).
     * @param id The integer ID to validate
     * @return true if positive, false otherwise
     */
    public static boolean isValidId(int id) {
        if (id <= 0) {
            logger.warning("Invalid ID: " + id + ". Must be positive.");
            return false;
        }
        return true;
    }
    
    /**
     * Sanitizes input by removing potentially harmful characters (for display purposes).
     * Note: This is NOT a replacement for prepared statements for SQL safety.
     * @param input The input string to sanitize
     * @return The sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove common SQL injection characters (for display only)
        return input.replaceAll("[;'\\"\\-\\-]", "").trim();
    }
}
