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

@WebServlet("/user_infomaction")
public class user_infomaction extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String dbUser = "root";
    final String dbPassword = "albin2364";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String user_name = "";
        String user_email = "";
        String user_password = "";
        String user_phone = "";
        String user_address = "";
        boolean isEdit = false;

        HttpSession session = request.getSession(false);

        try {
            Class.forName(driver);

            if (session != null && session.getAttribute("user") != null && session.getAttribute("password") != null) {
                String sessionUser = (String) session.getAttribute("user");
                String sessionPassword = (String) session.getAttribute("password");

                try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword);
                     PreparedStatement pst = con.prepareStatement(
                             "SELECT user_name, user_email, user_password, user_phone, user_address " +
                                     "FROM user WHERE user_name=? AND user_password=?")) {

                    pst.setString(1, sessionUser);
                    pst.setString(2, sessionPassword);

                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            user_name = rs.getString("user_name");
                            user_email = rs.getString("user_email");
                            user_password = rs.getString("user_password");
                            user_phone = rs.getString("user_phone");
                            user_address = rs.getString("user_address");
                            isEdit = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display form
        out.print("<html>");
        out.print("<head><title>User Information</title>");
        out.print("<style>");
        out.print("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:20px; }");
        out.print(".container { max-width:600px; margin:auto; background:white; padding:25px; border-radius:12px; box-shadow:0 4px 15px rgba(0,0,0,0.1); }");
        out.print("h2 { color:#007bff; margin-bottom:20px; text-align:center; }");
        out.print("label { display:block; margin-top:15px; margin-bottom:5px; font-weight:bold; }");
        out.print("input[type=text], input[type=email], input[type=password] { width:100%; padding:10px; border:1px solid #ccc; border-radius:5px; box-sizing:border-box; }");
        out.print("input[type=submit] { background:#007bff; color:white; padding:10px 20px; border:none; border-radius:5px; cursor:pointer; font-size:16px; margin-top:20px; width:100%; }");
        out.print("input[type=submit]:hover { background:#0056b3; }");
        out.print(".message { color: green; margin-bottom:15px; text-align:center; }");
        out.print("</style>");
        out.print("</head>");
        out.print("<body>");
        out.print("<div class='container'>");
        out.print("<h2>" + (isEdit ? "Edit Your Information" : "Register New User") + "</h2>");

        // Show message if any
        if (session != null && session.getAttribute("message") != null) {
            out.print("<div class='message'>" + session.getAttribute("message") + "</div>");
            session.removeAttribute("message");
        }

        out.print("<form action='user_infomaction' method='post'>");
        out.print("<label for='user_name'>Name:</label>");
        out.print("<input type='text' id='user_name' name='user_name' value='" + user_name + "' required>");
        out.print("<label for='user_email'>Email:</label>");
        out.print("<input type='email' id='user_email' name='user_email' value='" + user_email + "' required>");
        out.print("<label for='user_password'>Password:</label>");
        out.print("<input type='password' id='user_password' name='user_password' value='" + user_password + "' required>");
        out.print("<label for='user_phone'>Phone:</label>");
        out.print("<input type='text' id='user_phone' name='user_phone' value='" + user_phone + "' required>");
        out.print("<label for='user_address'>Address:</label>");
        out.print("<input type='text' id='user_address' name='user_address' value='" + user_address + "' required>");
        out.print("<input type='submit' value='" + (isEdit ? "Update" : "Register") + "'>");
        out.print("</form>");
        out.print("</div>");
        out.print("</body>");
        out.print("</html>");
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_name = request.getParameter("user_name");
        String user_email = request.getParameter("user_email");
        String user_password = request.getParameter("user_password");
        String user_phone = request.getParameter("user_phone");
        String user_address = request.getParameter("user_address");

        HttpSession session = request.getSession(true);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {

            int userId = 0;

            // Check if user exists
            try (PreparedStatement pstSelect = con.prepareStatement("SELECT user_id FROM user WHERE user_email = ?")) {
                pstSelect.setString(1, user_email);
                try (ResultSet rs = pstSelect.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                    }
                }
            }

            // Update or insert
            if (userId > 0) {
                // UPDATE
                try (PreparedStatement pstUpdate = con.prepareStatement(
                        "UPDATE user SET user_name=?, user_password=?, user_phone=?, user_address=? WHERE user_id=?")) {
                    pstUpdate.setString(1, user_name);
                    pstUpdate.setString(2, user_password);
                    pstUpdate.setString(3, user_phone);
                    pstUpdate.setString(4, user_address);
                    pstUpdate.setInt(5, userId);
                    int rows = pstUpdate.executeUpdate();
                    if (rows > 0) session.setAttribute("message", "✅ User updated successfully!");
                }
            } else {
                // INSERT
                try (PreparedStatement pstInsert = con.prepareStatement(
                        "INSERT INTO user(user_name, user_email, user_password, user_phone, user_address) VALUES (?, ?, ?, ?, ?)")) {
                    pstInsert.setString(1, user_name);
                    pstInsert.setString(2, user_email);
                    pstInsert.setString(3, user_password);
                    pstInsert.setString(4, user_phone);
                    pstInsert.setString(5, user_address);
                    int rows = pstInsert.executeUpdate();
                    if (rows > 0) session.setAttribute("message", "✅ User registered successfully!");

                    // Insert into login table
                    String user_role = "User";
                    try (PreparedStatement pstInsertlogin = con.prepareStatement(
                            "INSERT INTO login(user_name, user_password, user_role) VALUES(?,?,?)")) {
                        pstInsertlogin.setString(1, user_name);
                        pstInsertlogin.setString(2, user_password);
                        pstInsertlogin.setString(3, user_role);
                        pstInsertlogin.executeUpdate();
                    }
                }
            }

            response.sendRedirect("login.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
