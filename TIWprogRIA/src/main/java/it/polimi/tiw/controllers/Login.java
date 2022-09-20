package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.mysql.cj.util.StringUtils;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")  @MultipartConfig
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine tEngine;
    private UDao dao; 
    
    public void init() {
    	tEngine=Utili.getTemplateEngine(getServletContext());
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
    
    public Login() {
        super();
    }
 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user=(request.getParameter("username"));
		String password=(request.getParameter("password"));
		String mail=(request.getParameter("mail"));

		
		if(password==null||user==null ||mail==null) {
			response.getWriter().append("missing input");
			response.setStatus(400);			
		return;
		}
		if(Utili.sporca(password) || Utili.sporca(user) || !Utili.isMail(mail)) {
			response.getWriter().append("errore input");
			response.setStatus(400);			
			return;
		}
		try {
			if(!dao.login(user, password,mail)) {//TODO aggiungere mail
				Utili.errorMessage(response,406,"credenziali errate");
				return;
			}
			HttpSession ss= request.getSession();
			if(!ss.isNew()) {
				ss.invalidate();
				ss=request.getSession();
			}
			ss.setAttribute("user", user);			
			response.getWriter().append("ok");
			response.setStatus(200);
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			return;
		} catch (IOException|SQLException e) {
			Utili.errorMessage(response,500, e.getMessage());
			return;
		}
		
		
		
	}

}
