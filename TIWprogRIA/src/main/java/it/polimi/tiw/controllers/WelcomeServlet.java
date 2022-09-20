package it.polimi.tiw.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class WelcomeServlet
 */
@WebServlet("")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine tEngine;
 
	public void init() {
    	tEngine=Utili.getTemplateEngine(getServletContext()); 

	}
	public void destroy() {
		 
	}
    public WelcomeServlet() { 
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("eccoci");
		//TemplateEngine tEngine=Utili.getTemplateEngine(getServletContext());
		String path="WEB-INF/index.html";
		WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
		tEngine.process(path, ctx,response.getWriter());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
