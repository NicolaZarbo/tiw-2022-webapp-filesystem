package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.standard.processor.StandardSubstituteByTagProcessor;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.DaoDocumenti;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.beans.Documento;
import it.polimi.tiw.beans.SottoCartella;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;


@WebServlet("/Documenti")
public class Documenti extends HttpServlet {//stampa file di sottocartella
	private static final long serialVersionUID = 1L;
	private TemplateEngine tEngine;
    private SubCartellaDao dao;
    private Connection connessione;

    public void init() {
    	tEngine=Utili.getTemplateEngine(getServletContext());
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
    public Documenti() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 HttpSession sessione=request.getSession();
		 String userName=(String)sessione.getAttribute("user");
		 
		 //String cartStr=request.getParameter("cartellaId");
		 String subCStr=request.getParameter("subCartellaId");
		 String subN=request.getParameter("nSubCartella");
		 //int cartellaId=0;
		 int subCartellaId=0;
		 int subNumber=0;
		 
		 
		 
		 if(userName==null || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
			return;
		 }
	     if(Utili.sporca(userName)  ||subCStr==null ||subN==null) {
	    	response.sendError(400, "bad or missing input");	
			return;
		 }
	     try {
	    	// cartellaId=Integer.parseInt(cartStr);
	    	 subCartellaId=Integer.parseInt(subCStr);
	    	 subNumber=Integer.parseInt(subN);
	     }catch (NumberFormatException e) {
	    	response.sendError(400, "Expected integer value");	
			return;		
		}
	     
	     try {
	    	if(!dao.isSottoCartellaDiUtente(subCartellaId, userName)) {
	    		response.sendError(401, "Ownership mismatch");	
				return;
	    	}
	    	//sessione.setAttribute("cartellaId", cartellaId);//per controlli quando viene spostato un file
			sessione.setAttribute("subCartellaId", subCartellaId);
			sessione.setAttribute("nSubCartella", subNumber);
			
	    	List<Documento> files= dao.getCartellaAndFiles(subCartellaId).getFiles();
	    	
			WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
			Utili.gestioneCronologia(sessione, ctx, request,"/Documenti?" +request.getQueryString());
			ctx.setVariable("files", files);
			ctx.setVariable("nSubCartella",subNumber);
			tEngine.process(HtmlPath.DOCUMENTI.getUrl(), ctx,response.getWriter());
	    	return;
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(500,e.getMessage());
			return;
		}
	     
		    
		    
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
