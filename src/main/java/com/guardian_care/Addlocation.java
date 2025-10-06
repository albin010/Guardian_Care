package com.guardian_care;

import java.io.IOException;
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
@WebServlet("/addlocation")
public class Addlocation	extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/guardian_care";
	final String user = "root";
	final String password = "albin2364";
	
	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
			String state=request.getParameter("state");
			String district=request.getParameter("district");
			String city=request.getParameter("city");
			String pincode=request.getParameter("pincode");
			System.out.println("location:"+state+district+city+pincode);
			
			pst=con.prepareStatement("insert into area(area_state,area_district,area_city,area_pincode) values(?,?,?,?)");
			pst.setString(1, state);
			pst.setString(2,district);
			pst.setString(3, city);
			pst.setString(4, pincode);
			int rs=pst.executeUpdate();
			
			HttpSession session = request.getSession(true);
			if(rs>=0) {
				session.setAttribute("message", "✅ Service added successfully!");
			}else {
	            session.setAttribute("message", "❌ Failed to add service!");
			}
	        response.sendRedirect("location");

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
