package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import database.DoctorDAO;
import database.DoctorScheduleDAO;
import models.User;
import models.DoctorSchedule;

/**
 * DoctorServlet - Handles HTTP requests for doctor management
 * Supports GET (list/view/schedule) and POST (create/schedule update)
 * @author Madhuri Kumar
 * @version 1.0
 */
@WebServlet("/doctors")
public class DoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DoctorDAO doctorDAO;
    private DoctorScheduleDAO scheduleDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        doctorDAO = new DoctorDAO();
        scheduleDAO = new DoctorScheduleDAO();
        getServletContext().log("DoctorServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
                case "VIEW":
                    viewDoctor(request, response);
                    break;
                case "SCHEDULE":
                    viewSchedule(request, response);
                    break;
                default:
                    listDoctors(request, response);
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
            if (action != null && action.equals("schedule")) {
                updateSchedule(request, response);
            } else {
                createDoctor(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Error in doPost: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listDoctors(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> doctors = doctorDAO.getAllDoctors();
            request.setAttribute("doctors", doctors);
            request.setAttribute("pageTitle", "All Doctors");
            request.getRequestDispatcher("/WEB-INF/views/doctors/list.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to list doctors", e);
        }
    }

    private void viewDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int doctorId = Integer.parseInt(request.getParameter("id"));
            User doctor = doctorDAO.getDoctorById(doctorId);

            if (doctor != null) {
                List<DoctorSchedule> schedules = scheduleDAO.getScheduleByDoctorId(doctorId);
                request.setAttribute("doctor", doctor);
                request.setAttribute("schedules", schedules);
                request.setAttribute("pageTitle", "Doctor Profile");
                request.getRequestDispatcher("/WEB-INF/views/doctors/view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void viewSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int doctorId = Integer.parseInt(request.getParameter("id"));
            User doctor = doctorDAO.getDoctorById(doctorId);
            List<DoctorSchedule> schedules = scheduleDAO.getScheduleByDoctorId(doctorId);

            request.setAttribute("doctor", doctor);
            request.setAttribute("schedules", schedules);
            request.setAttribute("pageTitle", "Doctor Schedule");
            request.getRequestDispatcher("/WEB-INF/views/doctors/schedule.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void createDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            if (username == null || password == null || email == null) {
                request.getSession().setAttribute("error", "Missing required fields");
                response.sendRedirect(request.getContextPath() + "/doctors?action=form");
                return;
            }

            User doctor = new User();
            doctor.setUsername(username);
            doctor.setPasswordHash(password);
            doctor.setEmail(email);
            doctor.setPhone(phone);
            doctor.setRole("doctor");

            if (doctorDAO.createDoctor(doctor)) {
                request.getSession().setAttribute("message", "Doctor created successfully!");
                response.sendRedirect(request.getContextPath() + "/doctors");
            } else {
                request.getSession().setAttribute("error", "Failed to create doctor");
                response.sendRedirect(request.getContextPath() + "/doctors");
            }
        } catch (Exception e) {
            throw new ServletException("Error creating doctor", e);
        }
    }

    private void updateSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int doctorId = Integer.parseInt(request.getParameter("doctor_id"));
            String dayOfWeek = request.getParameter("day_of_week");
            String startTime = request.getParameter("start_time");
            String endTime = request.getParameter("end_time");

            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setDoctorId(doctorId);
            schedule.setDayOfWeek(dayOfWeek);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);

            if (scheduleDAO.createSchedule(schedule)) {
                request.getSession().setAttribute("message", "Schedule updated successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to update schedule");
            }
            response.sendRedirect(request.getContextPath() + "/doctors?action=schedule&id=" + doctorId);
        } catch (Exception e) {
            throw new ServletException("Error updating schedule", e);
        }
    }
}
