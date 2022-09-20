package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.beans.SottoCartella;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class GestioneContenuti
 */
@WebServlet("/GestioneContenuti")
public class GestioneContenuti extends HttpServlet {//per creare cartelle, sub-cartelle, caricare documenti
	private static final long serialVersionUID = 1L;
    private Connection connessione;
    
	
	public void init() {
		connessione=DbConnection.getConnection();
	   
	 }
	public void destroy() {
		try {
			connessione.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    public GestioneContenuti() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    
		
		if(userName==null || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }
		if(Utili.sporca(userName)) {
	    	response.sendError(400, "bad input");
			return;
	    }
	    List<Cartella> cartelle;
		try {
	    	CartellaDao dao=new CartellaDao(connessione);
			cartelle=dao.getAllCartelleAndSub(userName);
			response.setStatus(200);
			response.setContentType("application/jons");
			if(cartelle==null) {
				response.getWriter().append("");
				return;
			}
			if(cartelle!=null) 
				response.getWriter().append(jsonPerRisposta(cartelle));
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(500, "db error");
			return;
		}
		
		
	}
	private String jsonPerRisposta(List<Cartella> cartelle) {
		String json="[";
		if(cartelle==null) {
			json+="]";
			return "";
		}
		for (Cartella cartella : cartelle) {
			json+="{\"cId\":\""+cartella.getCartellaId()+"\",\"subs\":  ["; 
			int i=1;
			if(cartella.getSubCartelle()!=null) {
				for (SottoCartella sub : cartella.getSubCartelle()) {
					json+="\""+sub.getSubCartellaId()+","+sub.getNome()+"\"";
					if(cartella.getSubCartelle().indexOf(sub)!=cartella.getSubCartelle().size()-1)
						json+=",";
					else json+="] }";
					i++;
				}
			}else json+="]}";
			json+="";
			if(cartelle.indexOf(cartella)!=cartelle.size()-1)
				json+=",";
			else json+="";
		}
		json+="]";
		//System.out.println(json);//debug
		return json;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
