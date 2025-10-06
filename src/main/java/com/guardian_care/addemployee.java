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

@WebServlet("/addemployee")
public class addemployee extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String user = "root";
    final String password = "albin2364";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");
        int id = 0;
        String emp_name = "", emp_email = "", emp_password = "", emp_phone = "", emp_address = "";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            if (idParam != null && !idParam.isEmpty()) {
                id = Integer.parseInt(idParam);

                Class.forName(driver);
                con = DriverManager.getConnection(url, user, password);
                pst = con.prepareStatement(
                        "SELECT emp_name, emp_email, emp_password, emp_phone, emp_address FROM employee WHERE emp_id=?");
                pst.setInt(1, id);
                rs = pst.executeQuery();

                if (rs.next()) {
                    emp_name = rs.getString("emp_name");
                    emp_email = rs.getString("emp_email");
                    emp_password = rs.getString("emp_password");
                    emp_phone = rs.getString("emp_phone");
                    emp_address = rs.getString("emp_address");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (pst != null) pst.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }

        // Display form
        out.println("<html><head><title>" + (id > 0 ? "Edit Employee" : "Add Employee") + "</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f5f5f5; padding: 30px; }");
        out.println(".container { max-width: 500px; margin: auto; background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }");
        out.println("h2 { text-align: center; color: #007bff; }");
        out.println("label { display: block; margin-top: 15px; font-weight: bold; }");
        out.println("input[type=text], input[type=email], input[type=password] { width: 100%; padding: 10px; margin-top: 5px; border-radius: 5px; border: 1px solid #ccc; box-sizing: border-box; }");
        out.println("input[type=submit] { background: #007bff; color: white; padding: 12px; margin-top: 20px; border: none; width: 100%; border-radius: 5px; cursor: pointer; font-size: 16px; }");
        out.println("input[type=submit]:hover { background: #0056b3; }");
        out.println(".message { text-align: center; margin-bottom: 15px; }");
        out.println("</style>");
        out.println("</head><body>");

        // Display message if exists
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("message") != null) {
            out.println("<div class='message'>" + session.getAttribute("message") + "</div>");
            session.removeAttribute("message");
        }

        out.println("<div class='container'>");
        out.println("<h2>" + (id > 0 ? "Edit Employee" : "Add Employee") + "</h2>");
        out.println("<form method='post' action='addemployee'>");
        out.println("<input type='hidden' name='id' value='" + id + "'/>");
        out.println("<label>Name:</label><input type='text' name='emp_name' value='" + emp_name + "' required/>");
        out.println("<label>Email:</label><input type='email' name='emp_email' value='" + emp_email + "' required/>");
        out.println("<label>Password:</label><input type='password' name='emp_password' value='" + emp_password + "' required/>");
        out.println("<label>Phone:</label><input type='text' name='emp_phone' value='" + emp_phone + "' required/>");
        out.println("<label>Address:</label><input type='text' name='emp_address' value='" + emp_address + "' required/>");
        out.println("<input type='submit' value='" + (id > 0 ? "Update" : "Save") + " Employee'/>");
        out.println("</form>");
        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection con = null;
        PreparedStatement pst = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

            String emp_name = request.getParameter("emp_name");
            String emp_email = request.getParameter("emp_email");
            String emp_password = request.getParameter("emp_password");
            String emp_phone = request.getParameter("emp_phone");
            String emp_address = request.getParameter("emp_address");

            // Safe parsing
            String idParam = request.getParameter("id");
            int id = 0;
            if (idParam != null && !idParam.isEmpty()) {
                id = Integer.parseInt(idParam);
            }

            HttpSession session = request.getSession(true);

            if (id > 0) {
                // Update employee
                pst = con.prepareStatement(
                        "UPDATE employee SET emp_name=?, emp_email=?, emp_password=?, emp_phone=?, emp_address=? WHERE emp_id=?");
                pst.setString(1, emp_name);
                pst.setString(2, emp_email);
                pst.setString(3, emp_password);
                pst.setString(4, emp_phone);
                pst.setString(5, emp_address);
                pst.setInt(6, id);
                pst.executeUpdate();

                session.setAttribute("message", "✅ Employee updated successfully!");
                response.sendRedirect("view");
            } else {
                // Insert new employee
                pst = con.prepareStatement(
                        "INSERT INTO employee(emp_name, emp_email, emp_password, emp_phone, emp_address) VALUES (?,?,?,?,?)");
                pst.setString(1, emp_name);
                pst.setString(2, emp_email);
                pst.setString(3, emp_password);
                pst.setString(4, emp_phone);
                pst.setString(5, emp_address);
                int row = pst.executeUpdate();

                if (row > 0) {
                    session.setAttribute("message", "✅ Employee added successfully!");
                } else {
                    session.setAttribute("message", "❌ Failed to add employee!");
                }
                response.sendRedirect("employee");
            }

            response.sendRedirect("employee"); // go back to the form page

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }
}
