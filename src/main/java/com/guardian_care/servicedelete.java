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

@WebServlet("/servicedelete")
public class servicedelete extends HttpServlet {

	/**
	 * 
	 */
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
            
            String id=request.getParameter("id");
            System.out.println("id: "+id);
            pst = con.prepareStatement("delete FROM service where service_id=?");
            pst.setInt(1, Integer.parseInt(id));
            pst.executeUpdate();
            response.sendRedirect("view");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
