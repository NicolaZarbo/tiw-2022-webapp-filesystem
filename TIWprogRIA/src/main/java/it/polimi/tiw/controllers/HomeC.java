package it.polimi.tiw.controllers;

import java.io.IOException;
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
 * Servlet implementation class DirectoryControl
 */
@WebServlet("/HomePage")
public class HomeC extends HttpServlet {//stampa tutte cartelle e sub-cartelle
	private static final long serialVersionUID = 1L;
    private CartellaDao dao;
    public HomeC() {
        super();
    }
    public void init() {
    	dao=new CartellaDao(DbConnection.getConnection());
    	
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    
	   // String fileIdStr=request.getParameter("fileId");
	    
	    if(userName==null || sessione.isNew()) {
	    	response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }
	    if(Utili.sporca(userName) ) {
	    	Utili.errorMessage(response,400, "bad or missing input");	
			return;
	    }
	    	
	    List<Cartella> cartelle;	    
	    try {
			cartelle=dao.getAllCartelleAndSub(userName);
		} catch (SQLException e) {
			e.printStackTrace();//TODO gestione errore
			Utili.errorMessage(response,500, "db error");
			return;
		}
	    response.setContentType("application/json");
		response.getWriter().append(jsonPerRisposta(cartelle));
	
	    
	}
	private String jsonPerRisposta(List<Cartella> cartelle) {
		String json="[";
		if(cartelle==null)
			return json+"]";
		for (Cartella cartella : cartelle) {
			json+="{\"cId\":\""+cartella.getCartellaId()+"\",\"subs\":["; 
			int i=1;
			if(cartella.getSubCartelle()!=null) {
				for (SottoCartella sub : cartella.getSubCartelle()) {
					json+="{\"id\":"+sub.getSubCartellaId()+",\"nome\":\""+sub.getNome()+"\"}";
					if(cartella.getSubCartelle().indexOf(sub)!=cartella.getSubCartelle().size()-1)
						json+=",";
					else json+="]";
					i++;
				}
			}
			else json+="]";
			json+="";
			if(cartelle.indexOf(cartella)!=cartelle.size()-1)
				json+="},";
			else json+="}";
		}
		json+="]";
	//	System.out.println(json);//debug
		return json;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void destroy() {
		try {
			dao.chiudi();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
