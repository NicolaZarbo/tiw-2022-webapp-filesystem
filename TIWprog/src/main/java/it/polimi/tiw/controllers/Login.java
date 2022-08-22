package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.mysql.cj.util.StringUtils;

import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
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
		if(Utili.sporca(password) || Utili.sporca(user)) {
			response.sendError(400, "bad input");	
			return;
		}
		try {
			if(!dao.login(user, password)) {
				String path="WEB-INF/Login.html";
				WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
				ctx.setVariable("errato", true);
				tEngine.process(path, ctx,response.getWriter());
				return;
			}
			request.getSession().setAttribute("user", user);
			String path = getServletContext().getContextPath() + "/HomePage";
			response.sendRedirect(path);
			
			return;
		} catch (IOException|SQLException e) {
			response.sendError(500, e.getMessage());
			return;
		}
		
		
		
	}

}
