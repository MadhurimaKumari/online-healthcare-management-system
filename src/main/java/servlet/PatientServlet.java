package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import database.PatientDAO;
import models.User;

/**
 * PatientServlet - Handles HTTP requests for patient management
 * Supports GET (list/view) and POST (register/update) operations
 * @author Madhuri Kumar
 * @version 1.0
 */
@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PatientDAO patientDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        patientDAO = new PatientDAO();
        getServletContext().log("PatientServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
                case "VIEW":
                    viewPatient(request, response);
                    break;
                case "FORM":
                    showRegistrationForm(request, response);
                    break;
                default:
                    listPatients(request, response);
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
            if (action == null || action.equals("register")) {
                registerPatient(request, response);
            } else if (action.equals("update")) {
                updatePatient(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Error in doPost: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listPatients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> patients = patientDAO.getAllPatients();
            request.setAttribute("patients", patients);
            request.setAttribute("pageTitle", "All Patients");
            request.getRequestDispatcher("/WEB-INF/views/patients/list.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to list patients", e);
        }
    }

    private void viewPatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            int patientId = Integer.parseInt(idParam);
            User patient = patientDAO.getPatientById(patientId);

            if (patient != null) {
                request.setAttribute("patient", patient);
                request.setAttribute("pageTitle", "Patient Details");
                request.getRequestDispatcher("/WEB-INF/views/patients/view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Register Patient");
        request.getRequestDispatcher("/WEB-INF/views/patients/form.jsp").forward(request, response);
    }

    private void registerPatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            if (username == null || password == null || email == null) {
                request.getSession().setAttribute("error", "Missing required fields");
                response.sendRedirect(request.getContextPath() + "/patients?action=form");
                return;
            }

            User patient = new User();
            patient.setUsername(username);
            patient.setPasswordHash(password);
            patient.setEmail(email);
            patient.setPhone(phone);
            patient.setRole("patient");

            if (patientDAO.createPatient(patient)) {
                request.getSession().setAttribute("message", "Patient registered successfully!");
                response.sendRedirect(request.getContextPath() + "/patients");
            } else {
                request.getSession().setAttribute("error", "Failed to register patient");
                response.sendRedirect(request.getContextPath() + "/patients?action=form");
            }
        } catch (Exception e) {
            throw new ServletException("Error registering patient", e);
        }
    }

    private void updatePatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int patientId = Integer.parseInt(request.getParameter("id"));
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            if (patientDAO.updatePatient(patientId, email, phone)) {
                request.getSession().setAttribute("message", "Patient updated successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to update patient");
            }
            response.sendRedirect(request.getContextPath() + "/patients?action=view&id=" + patientId);
        } catch (Exception e) {
            throw new ServletException("Error updating patient", e);
        }
    }
}
