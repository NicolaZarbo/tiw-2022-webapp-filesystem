package it.polimi.tiw.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utlli.DbConnection;

/**
 * Servlet implementation class DirectoryControl
 */
@WebServlet("/HomePage")
public class DirectoryControl extends HttpServlet {//stampa tutte cartelle e sub-cartelle
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DirectoryControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
	    Connection con= DbConnection.getConnection();
	    HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("userName");
	    
	/*	final String DB_URL = "jdbc:mysql://localhost:3306/tiwp?serverTimezone=UTC";//to test connection
		final String USER = "root";
		final String PASS = "root";
		String result = "Connection worked";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (Exception e) {
			result = "Connection failed";
			e.printStackTrace();
			}
			response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println(result);
			out.close();
			*/
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
