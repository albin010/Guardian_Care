package com.guardian_care;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bookserviceadd")
public class bookserviceadd extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Set encoding and content type
        response.setContentType("text/html;charset=UTF-8");

        try {
            Class.forName(driver);

            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO booking_info(user_id, service_id, departure_date, return_date, bk_anyrequirement, payment_status, book_status) VALUES (?,?,?,?,?,?,?)"
                 )) {

                // Get parameters from form
                int userId = Integer.parseInt(request.getParameter("user_id"));
                int serviceId = Integer.parseInt(request.getParameter("service"));
                String startDate = request.getParameter("date");
                String endDate = request.getParameter("enddate");
                String info = request.getParameter("info");
                String payment = request.getParameter("payment");
                String status = "pending";

                // Set values
                pst.setInt(1, userId);
                pst.setInt(2, serviceId);
                pst.setString(3, startDate); // use setDate if DB column is DATE
                pst.setString(4, endDate);
                pst.setString(5, info);
                pst.setString(6, payment);
                pst.setString(7, status);

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    response.getWriter().println("<h3>Booked! Waiting for the Admin Conformaction. Try Again.</h3><a href='bookservice'>Back</a>");
                    
                } else {
                    response.getWriter().println("<h3>Booking Failed. Try Again.</h3><a href='bookservice'>Back</a>");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().println("<h3>Error: " + ex.getMessage() + "</h3>");
        }
    }
}
