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
import javax.servlet.http.HttpSession;

@WebServlet("/servicestatus")
public class servicestatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DB details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            
            HttpSession session = request.getSession(false);
            Integer id = (Integer) session.getAttribute("userId");

            // Query for activated bookings only
            String sql = "SELECT departure_date, return_date, bk_anyrequirement, payment_status, book_status "
                       + "FROM booking_info WHERE book_status='activated' AND user_id=?";

            ps = con.prepareStatement(sql);
            ps.setInt(1, id);   // ✅ set parameter here
            rs = ps.executeQuery();


            // Add some CSS for table styling
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 80%; margin: 20px auto; }");
            out.println("th, td { border: 1px solid #333; padding: 8px; text-align: center; }");
            out.println("th { background-color: #007BFF; color: white; }");
            out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
            out.println("h2 { text-align: center; color: #007BFF; }");
            out.println("</style>");

            // Print Activated table
            out.println("<h2>Activated Bookings</h2>");
            out.println("<table>");
            out.println("<tr><th>Departure Date</th><th>Return Date</th><th>Requirement</th><th>Payment Status</th><th>Book Status</th></tr>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                out.println("<tr>");
                out.println("<td>" + rs.getString("departure_date") + "</td>");
                out.println("<td>" + rs.getString("return_date") + "</td>");
                out.println("<td>" + rs.getString("bk_anyrequirement") + "</td>");
                out.println("<td>" + rs.getString("payment_status") + "</td>");
                out.println("<td>" + rs.getString("book_status") + "</td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='5'>No Activated Bookings Found</td></tr>");
            }

            out.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
