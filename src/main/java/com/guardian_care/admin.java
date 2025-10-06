package com.guardian_care;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admindashboard")
public class admin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Admin Dashboard</title>");
        out.println("<style>");
        // General body
        out.println("body { margin:0; font-family: Arial, sans-serif; display:flex; height:100vh; }");
        
        // Sidebar
        out.println(".sidebar { width:220px; background:#005fa3; color:white; padding:20px; display:flex; flex-direction:column; }");
        out.println(".sidebar h2 { margin-bottom:20px; font-size:20px; }");
        out.println(".sidebar a { color:white; text-decoration:none; margin:10px 0; padding:8px; display:block; border-radius:5px; }");
        out.println(".sidebar a:hover { background:#0077cc; }");
        
        // Main content
        out.println(".main { flex:1; display:flex; flex-direction:column; }");
        
        // Topbar
        out.println(".topbar { background:#0077cc; color:white; padding:15px; display:flex; justify-content:space-between; align-items:center; }");
        out.println(".logout-btn { background:#ff4d4d; border:none; color:white; padding:8px 14px; border-radius:5px; cursor:pointer; }");
        out.println(".logout-btn:hover { background:#e60000; }");
        
        // Iframe
        out.println("iframe { flex:1; width:100%; border:none; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        // Sidebar
        out.println("<div class='sidebar'>");
        out.println("<h2>Guardian Care</h2>");
        out.println("<a href='addservice' target='contentFrame'>Service</a>");
        out.println("<a href='employee' target='contentFrame'>Employee</a>");
        out.println("<a href='location' target='contentFrame'>Add Location</a>");
        out.println("<a href='conformbooking' target='contentFrame'>View Booking</a>");
        out.println("<a href='view' target='contentFrame'>View</a>");
        out.println("</div>");

        // Main content area
        out.println("<div class='main'>");

        // Topbar
        out.println("<div class='topbar'>");
        out.println("<span>Welcome, " + (username != null ? username : "Admin") + "</span>");
        out.println("<form action='index.html' method='get' style='margin:0;'>");
        out.println("<button type='submit' class='logout-btn'>Logout</button>");
        out.println("</form>");
        out.println("</div>");

        // Iframe with dashboard cards
        String iframeContent = 
            "<div style=&quot;padding:30px; font-family:Arial,sans-serif; background:#f5f5f5; height:100%;&quot;>" +
            "<h2 style=&quot;color:#0077cc; margin-bottom:20px;&quot;>Admin Dashboard Overview</h2>" +
            "<div style=&quot;display:flex; flex-wrap:wrap; gap:20px;&quot;>" +

            "<div style=&quot;background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); flex:1 1 250px;&quot;>" +
            "<h3 style=&quot;color:#005fa3;&quot;>Welcome " + (username != null ? username : "Admin") + "!</h3>" +
            "<p>Use the sidebar to manage all sections efficiently.</p></div>" +

            "<div style=&quot;background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); flex:1 1 250px;&quot;>" +
            "<h3 style=&quot;color:#005fa3;&quot;>Service</h3>" +
            "<p>Click <b>Service</b> in the sidebar to manage available services.</p></div>" +

            "<div style=&quot;background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); flex:1 1 250px;&quot;>" +
            "<h3 style=&quot;color:#005fa3;&quot;>Employee</h3>" +
            "<p>Click <b>Employee</b> to view and manage employees.</p></div>" +

            "<div style=&quot;background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); flex:1 1 250px;&quot;>" +
            "<h3 style=&quot;color:#005fa3;&quot;>Add Location</h3>" +
            "<p>Click <b>Add Location</b> to manage service locations.</p></div>" +

            "<div style=&quot;background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); flex:1 1 250px;&quot;>" +
            "<h3 style=&quot;color:#005fa3;&quot;>View Booking</h3>" +
            "<p>Click <b>View Booking</b> to monitor all booking requests.</p></div>" +

            "<div style=&quot;background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); flex:1 1 250px;&quot;>" +
            "<h3 style=&quot;color:#005fa3;&quot;>Other Data</h3>" +
            "<p>Click <b>View</b> for other management data.</p></div>" +

            "</div></div>";

        out.println("<iframe name='contentFrame' srcdoc=\"" + iframeContent + "\"></iframe>");

        out.println("</div>"); // end main
        out.println("</body>");
        out.println("</html>");
    }
}
