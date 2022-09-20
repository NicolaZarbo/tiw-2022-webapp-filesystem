package it.polimi.tiw.controllers;

import java.io.FileNotFoundException;
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

@WebServlet("/VaiRegistrazione")
public class VaiRegistrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public VaiRegistrazione() {
        super();
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
		// TODO Auto-generated method stub
		doGet(request, response);
	} 

}
