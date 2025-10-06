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

@WebServlet("/view")
public class view extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html");
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Service & Employee List</title>");
            
            // CSS Styling
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:30px; }");
            out.println("h1, h2 { color:#005fa3; margin-bottom:15px; }");
            out.println("table { width:100%; border-collapse:collapse; background:white; box-shadow:0 4px 12px rgba(0,0,0,0.1); border-radius:12px; overflow:hidden; margin-bottom:30px; }");
            out.println("th, td { padding:12px 15px; text-align:left; border-bottom:1px solid #ddd; }");
            out.println("th { background:#005fa3; color:white; }");
            out.println("tr:hover { background:#f1f1f1; }");
            out.println("a { color:#0077cc; text-decoration:none; font-weight:bold; }");
            out.println("a:hover { text-decoration:underline; }");
            out.println("</style>");
            
            out.println("</head><body>");
            out.println("<h1>Guardian Care Dashboard</h1>");

            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            // ----------------- SERVICE TABLE -----------------
            pst = con.prepareStatement("SELECT service_id, service_name, service_desinaction, service_rate FROM service");
            rs = pst.executeQuery();

            out.println("<h2>Service Details</h2>");
            out.print("<table>");
            out.print("<tr><th>SINo</th><th>Service Name</th><th>Description</th><th>Price</th><th>UPDATE</th><th>DELETE</th></tr>");

            int serviceNo = 1;
            while (rs.next()) {
                int service_id = rs.getInt("service_id");
                out.print("<tr>");
                out.print("<td>" + serviceNo + "</td>");
                out.print("<td>" + rs.getString("service_name") + "</td>");
                out.print("<td>" + rs.getString("service_desinaction") + "</td>");
                out.print("<td>" + rs.getString("service_rate") + "</td>");
                out.println("<td><a href='addservice?id=" + service_id + "'>Edit</a></td>");
                out.println("<td><a href='servicedelete?id=" + service_id + "'>Delete</a></td>");
                out.print("</tr>");
                serviceNo++;
            }
            out.print("</table>");

            // ----------------- EMPLOYEE TABLE -----------------
            pst = con.prepareStatement("SELECT emp_id, emp_name, emp_email, emp_phone, emp_address FROM employee");
            rs = pst.executeQuery();

            out.println("<h2>Employee Details</h2>");
            out.print("<table>");
            out.print("<tr><th>SINo</th><th>Name</th><th>Email</th><th>Phone</th><th>Address</th><th>UPDATE</th><th>DELETE</th></tr>");

            int empNo = 1;
            while (rs.next()) {
                int emp_id = rs.getInt("emp_id");
                out.print("<tr>");
                out.print("<td>" + empNo + "</td>");
                out.print("<td>" + rs.getString("emp_name") + "</td>");
                out.print("<td>" + rs.getString("emp_email") + "</td>");
                out.print("<td>" + rs.getString("emp_phone") + "</td>");
                out.print("<td>" + rs.getString("emp_address") + "</td>");
                out.println("<td><a href='addemployee?id=" + emp_id + "'>Edit</a></td>");
                out.println("<td><a href='employeedelete?id=" + emp_id + "'>Delete</a></td>");
                out.print("</tr>");
                empNo++;
            }
            out.print("</table>");

            out.println("</body></html>");

        } catch (Exception ex) {
            ex.printStackTrace(out);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
