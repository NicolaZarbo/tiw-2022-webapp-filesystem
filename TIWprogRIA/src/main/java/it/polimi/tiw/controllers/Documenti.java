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
import it.polimi.tiw.DAO.DaoDocumento;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.beans.Cartella;
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
		 
		 String subCStr=request.getParameter("subCartellaId");
		 int subCartellaId=0;
		 
		 
		 
		 if(userName==null || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
			return;
		 }
		 //System.out.print(subCStr);
	     if(subCStr==null ) {//Utili.sporca(userName)  ||
	    	Utili.errorMessage(response,400, "bad or missing input");	
			return;
		 }
	     try {
	    	 //cartellaId=Integer.parseInt(cartStr);
	    	 subCartellaId=Integer.parseInt(subCStr);
	     }catch (NumberFormatException e) {
	    	Utili.errorMessage(response,400, "Expected integer value");	
			return;		
		}
	     
	     try {
	    	if(!dao.isSottoCartellaDiUtente(subCartellaId, userName)) {
	    		
	    		Utili.errorMessage(response,401, "Ownership mismatch");	
				return;
	    	}
	    	//sessione.setAttribute("cartellaId", cartellaId);//per controlli quando viene spostato un file
			sessione.setAttribute("subCartellaId", subCartellaId);
			response.setStatus(200);
	    	response.setContentType("application/json");
	    	SottoCartella subCart= dao.getCartellaAndFiles(subCartellaId);
		
	    	response.getWriter().append(jsonPerRisposta(subCart));
	    	return;
		} catch (SQLException e) {
			Utili.errorMessage(response,500,"db error");
			return;
		}
	     
		    
		    
	}
	private String jsonPerRisposta(SottoCartella sub) {
		String json="{\"subName\":\""+sub.getNome()+"\",\"files\":[";
		List<Documento> files=sub.getFiles();
		if(files==null)
			return json+"]}";
		for (Documento fi: files) {
			json+="{\"nome\":\""+fi.getFileName()+"\", \"fileId\":\""+fi.getIdDocumento()+"\"}"; 
			if(files.indexOf(fi)!=files.size()-1)
				json+=",";
		}
		json+="]}";
		//System.out.println(json);//debug
		return json;
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
