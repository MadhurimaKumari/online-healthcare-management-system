package utils;

import org.mindrot.jbcrypt.BCrypt;
import java.util.logging.Logger;

/**
 * Utility class for secure password hashing using bcrypt with automatic salting.
 * Follows encapsulation with static methods and proper exception handling.
 */
public class PasswordUtil {
    private static final Logger logger = Logger.getLogger(PasswordUtil.class.getName());
    private static final int LOG_ROUNDS = 12; // Number of log rounds for bcrypt (higher = slower, more secure)
    
    /**
     * Hashes a plain-text password using bcrypt with automatic salting.
     * Each call generates a unique salt, preventing rainbow table attacks.
     * @param plainPassword The plain-text password to hash (must not be null or empty)
     * @return The hashed password string (includes salt:hash), or null if hashing fails
     * @throws RuntimeException if password hashing fails or input is invalid
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            logger.severe("Password cannot be null or empty.");
            throw new RuntimeException("Password cannot be null or empty.");
        }
        
        try {
            // BCrypt automatically generates a random salt and includes it in the output
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
            logger.info("Password hashed successfully.");
            return hashedPassword;
        } catch (Exception e) {
            logger.severe("Error hashing password: " + e.getMessage());
            throw new RuntimeException("Failed to hash password.", e);
        }
    }
    
    /**
     * Verifies a plain-text password against a bcrypt-hashed password.
     * Uses constant-time comparison to prevent timing attacks.
     * @param plainPassword The plain-text password to verify
     * @param hashedPassword The bcrypt-hashed password to verify against
     * @return true if passwords match, false otherwise
     * @throws RuntimeException if password verification fails
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            logger.warning("Plain password is null or empty.");
            return false;
        }
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            logger.warning("Hashed password is null or empty.");
            return false;
        }
        
        try {
            // BCrypt.checkpw uses constant-time comparison to prevent timing attacks
            boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
            if (matches) {
                logger.info("Password verification successful.");
            } else {
                logger.warning("Password verification failed - passwords do not match.");
            }
            return matches;
        } catch (IllegalArgumentException e) {
            // Invalid hash format (not a valid bcrypt hash)
            logger.severe("Invalid bcrypt hash format: " + e.getMessage());
            throw new RuntimeException("Invalid password hash format.", e);
        } catch (Exception e) {
            logger.severe("Error verifying password: " + e.getMessage());
            throw new RuntimeException("Failed to verify password.", e);
        }
    }
    
    /**
     * Checks if a password meets basic security requirements.
     * @param password The password to check
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isSecurePassword(String password) {
        if (password == null || password.length() < 6) {
            logger.warning("Password is too weak - minimum 6 characters required.");
            return false;
        }
        // Additional checks can be added here
        return true;
    }
    
    /**
     * Generates a random bcrypt salt for use in password hashing.
     * @return A bcrypt salt string
     */
    public static String generateSalt() {
        return BCrypt.gensalt(LOG_ROUNDS);
    }
}
