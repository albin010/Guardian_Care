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

@WebServlet("/user_update")
public class user_update extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String dbUser = "root";
    final String dbPassword = "albin2364";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect("login.html?error=session");
            return;
        }

        String user_name = "", user_email = "", user_password = "", user_phone = "", user_address = "";

        try {
            Class.forName(driver);
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword);
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM user WHERE user_id=?")) {

                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        user_name = rs.getString("user_name");
                        user_email = rs.getString("user_email");
                        user_password = rs.getString("user_password");
                        user_phone = rs.getString("user_phone");
                        user_address = rs.getString("user_address");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print HTML form with styling
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Update User</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f0f4f8; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; }");
        out.println("form { background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); width: 400px; max-width: 90%; }");
        out.println("h2 { text-align: center; color: #333333; margin-bottom: 20px; }");
        out.println("input[type=text], input[type=email], input[type=password] { width: 100%; padding: 10px; margin: 8px 0 15px 0; border: 1px solid #ccc; border-radius: 5px; }");
        out.println("button { width: 100%; padding: 12px; background-color: #007BFF; border: none; border-radius: 5px; color: white; font-size: 16px; cursor: pointer; }");
        out.println("button:hover { background-color: #0056b3; }");
        out.println("@media (max-width: 500px) { form { width: 90%; padding: 20px; } }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<form method='post' action='user_update'>");
        out.println("<h2>Update User Information</h2>");
        out.println("Name:<br><input type='text' name='user_name' value='" + user_name + "'/><br/>");
        out.println("Email:<br><input type='email' name='user_email' value='" + user_email + "'/><br/>");
        out.println("Password:<br><input type='password' name='user_password' value='" + user_password + "'/><br/>");
        out.println("Phone:<br><input type='text' name='user_phone' value='" + user_phone + "'/><br/>");
        out.println("Address:<br><input type='text' name='user_address' value='" + user_address + "'/><br/>");
        out.println("<button type='submit'>Update</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect("login.html?error=session");
            return;
        }

        String user_name = request.getParameter("user_name");
        String user_email = request.getParameter("user_email");
        String user_password = request.getParameter("user_password");
        String user_phone = request.getParameter("user_phone");
        String user_address = request.getParameter("user_address");

        try {
            Class.forName(driver);
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {

                // Update user table
                try (PreparedStatement pstUpdate = con.prepareStatement(
                        "UPDATE user SET user_name=?, user_email=?, user_password=?, user_phone=?, user_address=? WHERE user_id=?")) {

                    pstUpdate.setString(1, user_name);
                    pstUpdate.setString(2, user_email);
                    pstUpdate.setString(3, user_password);
                    pstUpdate.setString(4, user_phone);
                    pstUpdate.setString(5, user_address);
                    pstUpdate.setInt(6, userId);

                    int rows = pstUpdate.executeUpdate();

                    if (rows > 0) {
                        // Optional: Update login table (if login table has user_name column)
                        try (PreparedStatement pstLogin = con.prepareStatement(
                                "UPDATE login SET user_name=?, user_password=? WHERE user_name=?")) {

                            pstLogin.setString(1, user_name);
                            pstLogin.setString(2, user_password);
                            pstLogin.setString(3, (String) session.getAttribute("user"));
                            pstLogin.executeUpdate();
                        }

                        // Update session with new user name
                        session.setAttribute("user", user_name);
                        session.setAttribute("message", "✅ User updated successfully!");
                    }
                }
            }

            response.sendRedirect("user_update");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Error updating user.</h3>");
        }
    }
}
