package it.polimi.tiw.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.utlli.Utili;

@WebServlet("/redirectLogin")
public class RedirectLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public RedirectLogin() {
        super();
        // TODO Auto-generated constructor stub
    } 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setStatus(200);
			response.getOutputStream().print(Utili.readFileAsString(getServletContext().getRealPath("/WEB-INF/insertTempl/Login.txt")));
			response.setContentType("text/plain");
		}catch (FileNotFoundException e) {
			Utili.errorMessage(response,404, getServletContext().getRealPath("/WEB-INF/insertTempl/Login.txt"));
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
