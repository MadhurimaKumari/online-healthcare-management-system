package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import database.AppointmentDAO;
import models.Appointment;

/**
 * AppointmentServlet - Handles HTTP requests for appointment management
 * Supports GET (list/view/cancel) and POST (create) operations
 * @author Madhuri Kumar
 * @version 1.0
 */
@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        appointmentDAO = new AppointmentDAO();
        getServletContext().log("AppointmentServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
                case "VIEW":
                    viewAppointment(request, response);
                    break;
                case "FORM":
                    showBookingForm(request, response);
                    break;
                case "CANCEL":
                    cancelAppointment(request, response);
                    break;
                default:
                    listAppointments(request, response);
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
            String patientIdStr = request.getParameter("patient_id");
            String doctorIdStr = request.getParameter("doctor_id");
            String appointmentDate = request.getParameter("appointment_date");
            String appointmentTime = request.getParameter("appointment_time");
            String notes = request.getParameter("notes");

            if (patientIdStr == null || doctorIdStr == null || appointmentDate == null || appointmentTime == null) {
                request.getSession().setAttribute("error", "Missing required fields");
                response.sendRedirect(request.getContextPath() + "/appointments?action=form");
                return;
            }

            int patientId = Integer.parseInt(patientIdStr);
            int doctorId = Integer.parseInt(doctorIdStr);

            Appointment appointment = new Appointment();
            appointment.setPatientId(patientId);
            appointment.setDoctorId(doctorId);
            appointment.setAppointmentDate(appointmentDate);
            appointment.setAppointmentTime(appointmentTime);
            appointment.setStatus("pending");
            appointment.setNotes(notes != null ? notes : "");

            if (appointmentDAO.createAppointment(appointment)) {
                request.getSession().setAttribute("message", "Appointment booked successfully!");
                response.sendRedirect(request.getContextPath() + "/appointments");
            } else {
                request.getSession().setAttribute("error", "Failed to create appointment");
                response.sendRedirect(request.getContextPath() + "/appointments?action=form");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid input format");
            response.sendRedirect(request.getContextPath() + "/appointments?action=form");
        } catch (Exception e) {
            getServletContext().log("Error in doPost: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Appointment> appointments = appointmentDAO.getAllAppointments();
            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("/WEB-INF/views/appointments/list.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to list appointments", e);
        }
    }

    private void viewAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);

            if (appointment != null) {
                request.setAttribute("appointment", appointment);
                request.getRequestDispatcher("/WEB-INF/views/appointments/view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/appointments/form.jsp").forward(request, response);
    }

    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            if (appointmentDAO.updateAppointmentStatus(appointmentId, "cancelled")) {
                request.getSession().setAttribute("message", "Appointment cancelled successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to cancel appointment");
            }
            response.sendRedirect(request.getContextPath() + "/appointments");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
