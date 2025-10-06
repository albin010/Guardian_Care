package com.guardian_care;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/location")
public class location extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.print("<!DOCTYPE html>");
            out.print("<html lang='en'>");
            out.print("<head>");
            out.print("<meta charset='UTF-8'>");
            out.print("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.print("<title>Add Location</title>");

            // CSS Styling
            out.print("<style>");
            out.print("body { font-family: Arial, sans-serif; background:#f5f5f5; padding:30px; }");
            out.print("h1 { color:#005fa3; margin-bottom:10px; }");
            out.print("h5 { color:#0077cc; margin-bottom:20px; }");
            out.print("form { background:white; padding:25px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); max-width:500px; }");
            out.print("input[type=text] { width:100%; padding:10px; margin:10px 0; border:1px solid #ccc; border-radius:6px; font-size:16px; }");
            out.print("input[type=submit] { background:#005fa3; color:white; padding:10px 20px; border:none; border-radius:6px; cursor:pointer; font-size:16px; margin-top:10px; }");
            out.print("input[type=submit]:hover { background:#0077cc; }");
            out.print("h3 { color:green; margin-top:20px; }");
            out.print("</style>");

            out.print("</head>");
            out.print("<body>");

            out.print("<h1>Welcome to Location Page</h1>");
            out.print("<h5>Where You Can Add Business Locations</h5>");

            out.print("<form method='post' action='addlocation'>");
            out.print("<input type='text' name='state' placeholder='State' required/><br>");
            out.print("<input type='text' name='district' placeholder='District' required/><br>");
            out.print("<input type='text' name='city' placeholder='City' required/><br>");
            out.print("<input type='text' name='pincode' placeholder='Pincode' required/><br>");
            out.print("<input type='submit' name='submit' value='Add Location'>");
            out.print("</form>");

            // Show session message if any
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("message") != null) {
                String msg = session.getAttribute("message").toString();
                session.removeAttribute("message"); // clear after showing
                out.println("<h3>" + msg + "</h3>");
            }

            out.print("</body>");
            out.print("</html>");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
