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

@WebServlet("/login")
public class login extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/guardian_care";
    final String dbUser = "root";
    final String dbPassword = "albin2364";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginUser = request.getParameter("user_name");
        String loginPassword = request.getParameter("user_password");

        try {
            Class.forName(driver);
            try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {

                // Step 1: Validate login in login table
                try (PreparedStatement pst = con.prepareStatement(
                        "SELECT user_name, user_role FROM login WHERE user_name=? AND user_password=?")) {

                    pst.setString(1, loginUser);
                    pst.setString(2, loginPassword);

                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            String userName = rs.getString("user_name");
                            String userRole = rs.getString("user_role");

                            // Step 2: Fetch user_id from user table
                            int userId = 0; // default
                            try (PreparedStatement pstUser = con.prepareStatement(
                                    "SELECT user_id FROM user WHERE user_name=?")) { // assuming user_name is unique
                                pstUser.setString(1, userName);
                                try (ResultSet rsUser = pstUser.executeQuery()) {
                                    if (rsUser.next()) {
                                        userId = rsUser.getInt("user_id");
                                    }
                                }
                            }

                            // Step 3: Create session and store attributes
                            HttpSession session = request.getSession();
                            session.setAttribute("user", userName);
                            session.setAttribute("userId", userId); // store user table ID
                            session.setAttribute("role", userRole);

                            System.out.println("User: " + userName + ", User ID: " + userId);

                            // Step 4: Redirect based on role
                            if ("Admin".equalsIgnoreCase(userRole)) {
                                response.sendRedirect("admindashboard");
                            } else if ("User".equalsIgnoreCase(userRole)) {
                                response.sendRedirect("userdashboard");
                            } else {
                                response.sendRedirect("noUser");
                            }
                        } else {
                            // Invalid login
                            response.sendRedirect("login.html?error=invalid");
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            response.sendRedirect("login.html?error=server");
        }
    }
}
