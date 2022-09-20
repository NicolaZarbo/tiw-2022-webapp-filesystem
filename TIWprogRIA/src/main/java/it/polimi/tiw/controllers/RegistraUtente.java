package it.polimi.tiw.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class RegistraUtente
 */
@WebServlet("/registrazione")@MultipartConfig
public class RegistraUtente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private TemplateEngine tEngine;
    private UDao dao; 
    
    public void init() {
    //	tEngine=Utili.getTemplateEngine(getServletContext());
    	dao=new UDao(DbConnection.getConnection());
    	
    }
    public void destroy() {
		try {
			dao.chiudi();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistraUtente() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setStatus(200);
			response.getOutputStream().print(Utili.readFileAsString(getServletContext().getRealPath("/WEB-INF/insertTempl/Registrazione.html")));
			response.setContentType("text/plain");
		}catch (FileNotFoundException e) {
			Utili.errorMessage(response,404, "can't find page");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user=request.getParameter("username");
		String password=request.getParameter("password");
		String mail=request.getParameter("mail");

		if(user==null||password==null|| mail==null) {
			Utili.errorMessage(response,400, "missing inputt");	
			return;
		}
		
		if(user.length()<2||user.length()>44||password.length()<8 ||password.length()>12 ) {
			Utili.errorMessage(response,400, "wrong input length");	
			return;
		}
		
			
		if(Utili.sporca(password) || Utili.sporca(user)|| !Utili.isMail(mail)) {
			Utili.errorMessage(response,400, "bad input");	
			return;
		}
		try {
			if(dao.checkExists(user)) {
				Utili.errorMessage(response,406, "already exists");	

				return;
			}
			dao.registerNewUser(user, password,mail);
			
			request.getSession().setAttribute("user", user);
			return;
		}catch ( SQLException | IOException w) {
			w.printStackTrace();//TODO gestione errore
			Utili.errorMessage(response,500,"db error");
			return;
		}
			
	}

}
