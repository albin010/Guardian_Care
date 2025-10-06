package com.guardian_care;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/userdashboard")
public class userdashboard extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);  
        if (session == null || session.getAttribute("user") == null) {
            // No session → redirect to login/home
            response.sendRedirect("index.html");
            return;
        }

        String userName = (String) session.getAttribute("user");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head>");
        out.println("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>User Dashboard</title>");

        // CSS Styling
        out.println("<style>");
        out.println("body { margin:0; font-family: Arial, sans-serif; display:flex; height:100vh; }");
        // Sidebar
        out.println(".sidebar { width:220px; background:#007bff; color:white; padding:20px; display:flex; flex-direction:column; }");
        out.println(".sidebar h2 { margin-bottom:20px; font-size:20px; }");
        out.println(".sidebar a { color:white; text-decoration:none; margin:10px 0; padding:10px; display:block; border-radius:5px; }");
        out.println(".sidebar a:hover { background:#0056b3; }");
        // Main content
        out.println(".main { flex:1; display:flex; flex-direction:column; }");
        // Topbar
        out.println(".topbar { background:#0056b3; color:white; padding:15px; display:flex; justify-content:space-between; align-items:center; }");
        out.println(".logout-btn { background:#ff4d4d; border:none; color:white; padding:8px 14px; border-radius:5px; cursor:pointer; }");
        out.println(".logout-btn:hover { background:#e60000; }");
        // iframe
        out.println("iframe { flex:1; width:100%; border:none; }");
        out.println("</style>");
        out.println("</head><body>");

        // Sidebar
        out.println("<div class='sidebar'>");
        out.println("<h2>Guardian Care</h2>");
        out.println("<a href='bookservice' target='contentFrame'>Book Service</a>");
        out.println("<a href='user_update' target='contentFrame'>Update Account</a>");
        out.println("<a href='contactinfo.html' target='contentFrame'>Contact Info</a>");
        out.println("<a href='servicestatus' target='contentFrame'>Service Status</a>");
        out.println("</div>");

        // Main content
        out.println("<div class='main'>");
        // Topbar
        out.println("<div class='topbar'>");
        out.println("<span>Welcome, " + userName + "</span>");
        out.println("<form action='logout' method='get' style='margin:0;'>");
        out.println("<button type='submit' class='logout-btn'>Logout</button>");
        out.println("</form>");
        out.println("</div>");

        // Iframe with default welcome message
        out.println("<iframe name='contentFrame' srcdoc='");
        out.println("<div style=\"padding:30px; font-family:Arial,sans-serif; background:#f5f5f5; height:100%;\">");
        out.println("<h2 style=\"color:#007bff; margin-bottom:20px;\">Welcome, " + userName + "!</h2>");
        out.println("<p>Welcome to Guardian Care User Dashboard. Here you can book services, view your account details, check service status, and contact us for support.</p>");
        out.println("<ul>");
        out.println("<li>📌 Book Service: Schedule a new service request.</li>");
        out.println("<li>👤 View Account: Check and update your profile information.</li>");
        out.println("<li>📞 Contact Info: Get in touch with support.</li>");
        out.println("<li>📋 Service Status: Track your booked services.</li>");
        out.println("</ul>");
        out.println("</div>'></iframe>");

        out.println("</div>"); // end main
        out.println("</body></html>");
    }
}
