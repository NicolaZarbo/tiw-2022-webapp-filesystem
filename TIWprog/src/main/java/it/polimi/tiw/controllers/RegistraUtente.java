package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.DaoUtenti;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class RegistraUtente
 */
@WebServlet("/registrazione")
public class RegistraUtente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine tEngine;
    private DaoUtenti dao; 
    
    public void init() {
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	dao=new DaoUtenti(DbConnection.getConnection());
    	
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
		WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
		tEngine.process(HtmlPath.REGISTRAZIONE.getUrl(), ctx,response.getWriter());
		return;
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user=request.getParameter("username");
		String password=request.getParameter("password");
		if(user==null||password==null) {
			response.sendError(400, "missing input");	
			return;
		}
		if(user.length()<2||user.length()>44||password.length()<8 ||password.length()>12) {
			response.sendError(400, "wrong input lenght");	
			return;
		}
			
		if(Utili.sporca(password) || Utili.sporca(user)) {
			response.sendError(400, "bad input");	
			return;
		}
		try {
			if(dao.checkExists(user)) {
				WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
				ctx.setVariable("nondisponibile", true);
				tEngine.process(HtmlPath.REGISTRAZIONE.getUrl(), ctx,response.getWriter());
				return;
			}
			dao.registerNewUser(user, password);
			
			request.getSession().setAttribute("user", user);
			String path = getServletContext().getContextPath() + "/HomePage";
			response.sendRedirect(path);
			return;
		}catch ( SQLException | IOException w) {
			w.printStackTrace();//TODO gestione errore
			response.sendError(500);
			return;
		}
			
	}

}
