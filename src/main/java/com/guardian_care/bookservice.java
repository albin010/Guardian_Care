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

@WebServlet("/bookservice")
public class bookservice extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession(false);
            int id = (int) session.getAttribute("userId");  // user id from session

            out.println("<html><head><title>Booking Page</title>");

            // CSS styling
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:20px; }");
            out.println("h2 { color:#007bff; margin-bottom:20px; }");
            out.println("form { background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); max-width:600px; margin:auto; }");
            out.println("label { display:block; margin-top:15px; margin-bottom:5px; font-weight:bold; }");
            out.println("input[type=text], select { width:100%; padding:10px; margin-bottom:15px; border:1px solid #ccc; border-radius:5px; box-sizing:border-box; }");
            out.println("input[type=radio] { margin-right:5px; }");
            out.println("input[type=submit] { background:#007bff; color:white; padding:10px 20px; border:none; border-radius:5px; cursor:pointer; font-size:16px; }");
            out.println("input[type=submit]:hover { background:#0056b3; }");
            out.println("</style>");

            out.println("</head><body>");

            out.println("<h2>Book a Service</h2>");
            out.println("<form method='post' action='bookserviceadd'>");
            out.println("<input type='hidden' name='user_id' value='" + id + "'/>");

            // Service dropdown
            out.println("<label for='service'>Select Service:</label>");
            out.println("<select name='service' id='service'>");
            pst = con.prepareStatement("SELECT service_id, service_name FROM service");
            rs = pst.executeQuery();
            while (rs.next()) {
                int service_id = rs.getInt("service_id");
                String service_name = rs.getString("service_name");
                out.println("<option value='" + service_id + "'>" + service_name + "</option>");
            }
            out.println("</select>");

            // Start date
            out.println("<label for='date'>Start Date:</label>");
            out.println("<input type='text' name='date' id='date' placeholder='YYYY-MM-DD'/>");

            // End date
            out.println("<label for='enddate'>End Date:</label>");
            out.println("<input type='text' name='enddate' id='enddate' placeholder='YYYY-MM-DD'/>");

            // Additional info
            out.println("<label for='info'>Additional Information:</label>");
            out.println("<input type='text' name='info' id='info' placeholder='Any special requirements?'/>");

            // Payment type
            out.println("<label>Payment Method:</label>");
            out.println("<input type='radio' name='payment' value='online'/> Online ");
            out.println("<input type='radio' name='payment' value='offline'/> Offline");

            // Submit button
            out.println("<input type='submit' value='Book Service'/>");

            out.println("</form>");
            out.println("</body></html>");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
