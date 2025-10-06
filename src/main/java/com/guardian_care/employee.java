package com.guardian_care;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/employee")
public class employee extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String username = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("user");

            pst = con.prepareStatement("select area_pincode from area");
            rs = pst.executeQuery();

            out.print("<!DOCTYPE html>");
            out.print("<html lang='en'>");
            out.print("<head>");
            out.print("<meta charset='UTF-8'>");
            out.print("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.print("<title>Add Employee</title>");

            // Styling
            out.print("<style>");
            out.print("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:30px; }");
            out.print("h1 { color:#005fa3; margin-bottom:10px; }");
            out.print("form { background:white; padding:25px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); max-width:500px; }");
            out.print("input[type=text], input[type=password], select { width:100%; padding:10px; margin:10px 0; border:1px solid #ccc; border-radius:6px; font-size:16px; }");
            out.print("input[type=submit] { background:#005fa3; color:white; padding:10px 20px; border:none; border-radius:6px; cursor:pointer; font-size:16px; margin-top:10px; }");
            out.print("input[type=submit]:hover { background:#0077cc; }");
            out.print("h3 { color:green; margin-top:20px; }");
            out.print("</style>");

            out.print("</head>");
            out.print("<body>");

            out.print("<h1>Welcome to Add Employee, " + (username != null ? username : "Admin") + "</h1>");
            out.print("<form method='post' action='addemployee'>");

            out.print("<input type='text' name='emp_name' placeholder='Enter Name' required><br>");
            out.print("<input type='text' name='emp_email' placeholder='Enter Email' required><br>");
            out.print("<input type='password' name='emp_password' placeholder='Enter Password' required><br>");
            out.print("<input type='text' name='emp_phone' placeholder='Enter Phone' required><br>");
            out.print("<input type='text' name='emp_address' placeholder='Enter Address' required><br>");

            // Dropdown menu for area pincode
            out.print("<select name='pincode' required>");
            out.print("<option value=''>Select Pincode</option>");
            while (rs.next()) {
                String pincode = rs.getString("area_pincode");
                out.print("<option value='" + pincode + "'>" + pincode + "</option>");
            }
            out.print("</select><br>");

            out.print("<input type='submit' value='Submit'>");
            out.print("</form>");

            // Show session message if any
            if (session != null && session.getAttribute("message") != null) {
                String msg = session.getAttribute("message").toString();
                session.removeAttribute("message");
                out.println("<h3>" + msg + "</h3>");
            }

            out.print("</body>");
            out.print("</html>");

        } catch (IOException | SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}
