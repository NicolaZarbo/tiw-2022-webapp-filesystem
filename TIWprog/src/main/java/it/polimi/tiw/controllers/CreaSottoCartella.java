package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.CartellaDao;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.beans.Utente;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class CreaSottoCartella
 */
@WebServlet("/CreaSottoCartella")
public class CreaSottoCartella extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private SubCartellaDao dao; 
    private Connection connessione;
    
    public void init() {
    	connessione=DbConnection.getConnection();
    	dao=new SubCartellaDao(connessione);
    	
    }
    public void destroy() {
		try {
			dao.chiudi();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}     
  
    public CreaSottoCartella() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    String cartStr=request.getParameter("cartellaId");
	    Integer cartellaId=null;
		
	    
		if(userName==null  || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
			return;
	    }
		if(Utili.sporca(userName)|| cartStr==null) {
	    	response.sendError(400, "bad or missing input");	
			return;
	    }
		try {
			cartellaId=Integer.parseInt(cartStr);
		}catch (NumberFormatException e) {
			response.sendError(400, "Unexpected value, expected integer value for folder");	
			return;
		}
		
		
		try {
			UDao utDAO=new UDao(connessione);
			Utente utente=utDAO.getUtenteFromUserName(userName);
			CartellaDao cDAO=new CartellaDao(connessione);
			Cartella folder=cDAO.getCartella(cartellaId);
			
			if(folder.getUtenteId()!=utente.getId()) {
				response.sendError(401, "can't create a folder for another user");	
				return;
			}
		}catch (SQLException e) {
			response.sendError(500, "db error");	
			return;
		}
		try {
			dao.creaSottoCartella(cartellaId.intValue());
			response.sendRedirect(getServletContext().getContextPath() + "/GestioneContenuti");
		} catch (SQLException e) {
			response.sendError(500, "db error");	
			return;
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
