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

@WebServlet("")
public class RedirectLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public RedirectLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine tEngine=Utili.getTemplateEngine(getServletContext());
		//String path="WEB-INF/Login.html";
		WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
		tEngine.process(HtmlPath.LOGIN.getUrl(), ctx,response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
