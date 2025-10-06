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

@WebServlet("/addservice")
public class addservice extends HttpServlet {
    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            String id = request.getParameter("id");

            String serviceName = "";
            String serviceDesc = "";
            String serviceRate = "";

            if (id != null && !id.isEmpty()) {  // check null and empty
                try {
                    int Id = Integer.parseInt(id);
                    pst = con.prepareStatement("SELECT service_name, service_desinaction, service_rate FROM service WHERE service_id=?");
                    pst.setInt(1, Id);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        serviceName = rs.getString("service_name");
                        serviceDesc = rs.getString("service_desinaction");
                        serviceRate = rs.getString("service_rate");
                    }
                } catch (NumberFormatException e) {
                    out.println("<h3>Invalid service ID</h3>");
                }
            }

            out.print("<!DOCTYPE html>");
            out.print("<html lang='en'>");
            out.print("<head>");
            out.print("<meta charset='UTF-8'>");
            out.print("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.print("<title>Add Service</title>");

            // Styling
            out.print("<style>");
            out.print("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:30px; }");
            out.print("h1 { color:#005fa3; margin-bottom:10px; }");
            out.print("h5 { color:#0077cc; margin-bottom:30px; }");
            out.print("form { background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); max-width:500px; }");
            out.print("input[type=text], input[type=number] { width:100%; padding:10px; margin:10px 0; border:1px solid #ccc; border-radius:6px; font-size:16px; }");
            out.print("input[type=submit] { background:#005fa3; color:white; padding:10px 20px; border:none; border-radius:6px; cursor:pointer; font-size:16px; margin-top:10px; }");
            out.print("input[type=submit]:hover { background:#0077cc; }");
            out.print("h3 { color:green; margin-top:20px; }");
            out.print("</style>");

            out.print("</head>");
            out.print("<body>");

            out.print("<h1>Welcome to Service Page</h1>");
            out.print("<h5>Where You Can Add or Edit Service</h5>");
            out.print("<form method='post' action='service'>");

            if (id != null && !id.isEmpty()) {
                out.print("<input type='hidden' name='id' value='" + id + "'/>");
            }

            out.print("<input type='text' name='service_name' value='" + serviceName + "' placeholder='Enter Service Name' required/><br>");
            out.print("<input type='text' name='service_desinaction' value='" + serviceDesc + "' placeholder='Enter Service Description' required/><br>");
            out.print("<input type='text' name='service_rate' value='" + serviceRate + "' placeholder='Enter Service Rate' required/><br>");
            out.print("<input type='submit' name='submit' value='Save'>");
            out.print("</form>");

            // Show session message if any
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("message") != null) {
                String msg = session.getAttribute("message").toString();
                session.removeAttribute("message");
                out.println("<h3>" + msg + "</h3>");
            }

            out.print("</body>");
            out.print("</html>");

        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
