package com.guardian_care;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/service")
public class service extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            // Safely parse the ID
            String idParam = request.getParameter("id");
            int id = 0; // default 0 for new insert
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    id = Integer.parseInt(idParam);
                } catch (NumberFormatException e) {
                    id = 0; // fallback to insert if invalid id
                }
            }

            // Get form parameters
            String service_name = request.getParameter("service_name");
            String service_desinaction = request.getParameter("service_desinaction");
            String service_rate = request.getParameter("service_rate");

            if (id > 0) { // UPDATE existing service
                pst = con.prepareStatement(
                        "UPDATE service SET service_name = ?, service_desinaction = ?, service_rate = ? WHERE service_id = ?");
                pst.setString(1, service_name);
                pst.setString(2, service_desinaction);
                pst.setString(3, service_rate);
                pst.setInt(4, id);
                pst.executeUpdate();
                response.sendRedirect("view"); // redirect to view page
            } else { // INSERT new service
                pst = con.prepareStatement(
                        "INSERT INTO service(service_name, service_desinaction, service_rate) VALUES (?, ?, ?)");
                pst.setString(1, service_name);
                pst.setString(2, service_desinaction);
                pst.setString(3, service_rate);
                int rows = pst.executeUpdate();

                HttpSession session = request.getSession(true);
                if (rows > 0) {
                    session.setAttribute("message", "✅ Service added successfully!");
                } else {
                    session.setAttribute("message", "❌ Failed to add service!");
                }
                response.sendRedirect("addservice"); // redirect back to addservice page
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
