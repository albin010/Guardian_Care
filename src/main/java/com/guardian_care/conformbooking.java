package com.guardian_care;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/conformbooking")
public class conformbooking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // DB Connection details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);

            // Fetch all pending bookings
            String query = "SELECT book_id, departure_date, return_date, bk_anyrequirement, payment_status, book_status FROM booking_info WHERE book_status = 'pending'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Fetch all employees for dropdown
            String empQuery = "SELECT emp_id, emp_name FROM employee";
            PreparedStatement empPst = con.prepareStatement(empQuery);
            ResultSet empRs = empPst.executeQuery();

            // Store employees in memory
            StringBuilder empOptions = new StringBuilder();
            while (empRs.next()) {
                empOptions.append("<option value='").append(empRs.getInt("emp_id")).append("'>")
                        .append(empRs.getString("emp_name")).append("</option>");
            }

            // HTML & CSS
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Confirm Bookings</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:30px; }");
            out.println("h2 { color:#005fa3; margin-bottom:20px; }");
            out.println("table { width:100%; border-collapse:collapse; background:white; box-shadow:0 4px 12px rgba(0,0,0,0.1); border-radius:12px; overflow:hidden; margin-bottom:40px; }");
            out.println("th, td { padding:12px 15px; text-align:left; border-bottom:1px solid #ddd; }");
            out.println("th { background:#005fa3; color:white; }");
            out.println("tr:hover { background:#f1f1f1; }");
            out.println("select { padding:6px; border-radius:6px; border:1px solid #ccc; font-size:14px; }");
            out.println("input[type=submit] { background:#005fa3; color:white; padding:6px 12px; border:none; border-radius:6px; cursor:pointer; font-size:14px; }");
            out.println("input[type=submit]:hover { background:#0077cc; }");
            out.println("</style></head><body>");

            // ----------------- Pending Bookings -----------------
            out.println("<h2>Pending Bookings</h2>");
            out.println("<table>");
            out.println("<tr><th>S.No</th><th>Departure Date</th><th>Return Date</th><th>Requirement</th><th>Payment Status</th><th>Assign Employee</th><th>Action</th></tr>");

            int i = 1;
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + rs.getDate("departure_date") + "</td>");
                out.println("<td>" + rs.getDate("return_date") + "</td>");
                out.println("<td>" + rs.getString("bk_anyrequirement") + "</td>");
                out.println("<td>" + rs.getString("payment_status") + "</td>");

                // Employee Dropdown + Confirm Button inside same form
                out.println("<td>");
                out.println("<form method='post' action='conformbooking'>");
                out.println("<input type='hidden' name='book_id' value='" + bookId + "'/>");
                out.println("<select name='emp_id'>" + empOptions.toString() + "</select>");
                out.println("</td>");
                out.println("<td><input type='submit' value='Confirm'/></td>");
                out.println("</form>");
                out.println("</tr>");
                i++;
            }

            out.println("</table>");

            // ----------------- All Booking History -----------------
            String historyQuery = "SELECT book_id, user_id, service_id, departure_date, return_date, bk_anyrequirement, payment_status, book_status, emp_id FROM booking_info";
            PreparedStatement historyPst = con.prepareStatement(historyQuery);
            ResultSet historyRs = historyPst.executeQuery();

            out.println("<h2>All Booking History</h2>");
            out.println("<table>");
            out.println("<tr><th>SINO</th><th>User id</th><th>Departure</th><th>Return</th><th>Requirement</th><th>Payment</th><th>Status</th><th>Employee</th></tr>");
            int s=0;
            while (historyRs.next()) {
                out.println("<tr>");
                out.println("<td>" + s+++ "</td>");
                out.println("<td>" + historyRs.getInt("book_id") + "</td>");
                out.println("<td>" + historyRs.getDate("departure_date") + "</td>");
                out.println("<td>" + historyRs.getDate("return_date") + "</td>");
                out.println("<td>" + historyRs.getString("bk_anyrequirement") + "</td>");
                out.println("<td>" + historyRs.getString("payment_status") + "</td>");
                out.println("<td>" + historyRs.getString("book_status") + "</td>");
                out.println("<td>" + historyRs.getInt("emp_id") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }

    // Handle Confirm action
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookId = Integer.parseInt(request.getParameter("book_id"));
        int empId = Integer.parseInt(request.getParameter("emp_id"));

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);

            // Update booking status
            String updateQuery = "UPDATE booking_info SET book_status='activated', emp_id=? WHERE book_id=?";
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setInt(1, empId);
            pst.setInt(2, bookId);
            pst.executeUpdate();

            con.close();

            // Refresh page after update
            response.sendRedirect("conformbooking");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
}
