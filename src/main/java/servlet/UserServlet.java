package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import database.UserDAO;
import models.User;
import utils.PasswordUtil;

/**
 * UserServlet - Handles authentication and user management
 * Supports login, logout, register, and profile operations
 * @author Madhuri Kumar
 * @version 1.0
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        getServletContext().log("UserServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "LOGIN";

        try {
            switch (action.toUpperCase()) {
                case "REGISTER":
                    showRegisterForm(request, response);
                    break;
                case "LOGOUT":
                    handleLogout(request, response);
                    break;
                case "PROFILE":
                    showProfile(request, response);
                    break;
                default:
                    showLoginForm(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Error in doGet: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action != null && action.equals("register")) {
                handleRegister(request, response);
            } else {
                handleLogin(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Error in doPost: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Login");
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Register");
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Username and password are required");
                response.sendRedirect(request.getContextPath() + "/user?action=login");
                return;
            }

            User user = userDAO.getUserByUsername(username);

            if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("username", user.getUsername());
                
                getServletContext().log("User logged in: " + username);
                
                // Redirect based on role
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else if ("doctor".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/doctor/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/patient/dashboard");
                }
            } else {
                request.getSession().setAttribute("error", "Invalid username or password");
                response.sendRedirect(request.getContextPath() + "/user?action=login");
                getServletContext().log("Failed login attempt for username: " + username);
            }
        } catch (Exception e) {
            throw new ServletException("Error during login", e);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");

            if (username == null || password == null || email == null || role == null) {
                request.getSession().setAttribute("error", "Missing required fields");
                response.sendRedirect(request.getContextPath() + "/user?action=register");
                return;
            }

            // Check if user already exists
            if (userDAO.getUserByUsername(username) != null) {
                request.getSession().setAttribute("error", "Username already exists");
                response.sendRedirect(request.getContextPath() + "/user?action=register");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(PasswordUtil.hashPassword(password));
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);

            if (userDAO.createUser(user)) {
                request.getSession().setAttribute("message", "Registration successful! Please log in.");
                response.sendRedirect(request.getContextPath() + "/user?action=login");
                getServletContext().log("New user registered: " + username);
            } else {
                request.getSession().setAttribute("error", "Registration failed. Please try again.");
                response.sendRedirect(request.getContextPath() + "/user?action=register");
            }
        } catch (Exception e) {
            throw new ServletException("Error during registration", e);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                getServletContext().log("User logged out: " + username);
                session.invalidate();
            }
            request.getSession().setAttribute("message", "You have been logged out successfully");
            response.sendRedirect(request.getContextPath() + "/user?action=login");
        } catch (Exception e) {
            throw new ServletException("Error during logout", e);
        }
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/user?action=login");
                return;
            }

            User user = (User) session.getAttribute("user");
            user = userDAO.getUserById(user.getId()); // Refresh user data
            
            request.setAttribute("user", user);
            request.setAttribute("pageTitle", "User Profile");
            request.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error retrieving profile", e);
        }
    }
}

