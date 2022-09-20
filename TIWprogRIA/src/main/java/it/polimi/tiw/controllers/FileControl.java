package it.polimi.tiw.controllers;

import java.io.IOException;
import java.io.PrintWriter;
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
import it.polimi.tiw.DAO.DaoDocumento;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.beans.Documento;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class FileControl
 */
@WebServlet("/AccediDocumento")
public class FileControl extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	private TemplateEngine tEngine; 
	private Connection connessione;
    private DaoDocumento fDao; 
    private SubCartellaDao subDao;
    
    
    public void init() {
    	connessione= DbConnection.getConnection();
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	fDao=new DaoDocumento(connessione);
    	subDao=new SubCartellaDao(connessione);
    	 
    }
    public void destroy() {
		try {
			connessione.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}   
     
    public FileControl() {
        super();
    } 

 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		HttpSession sessione=request.getSession();
		String userName=(String)sessione.getAttribute("user");
		Integer subCartellaId=(Integer)sessione.getAttribute("subCartellaId");
		
		String fileIDstr=request.getParameter("fileId");
		int fileId=0;
		if(userName==null ||subCartellaId==null  || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }
	    if(Utili.sporca(userName) || fileIDstr==null) {
	    	Utili.errorMessage(response,400, "bad or missing input");	
			return;
	    }
		try {
			fileId=Integer.parseInt(fileIDstr);
		}catch (NumberFormatException e) {
			Utili.errorMessage(response,400, "Expected integer value");	
			return;
		}
		try {
			Documento file=fDao.getFile(fileId);
			if(file==null) {
				Utili.errorMessage(response,404, "file not found");
				return;
			}
			if(!subDao.isSottoCartellaDiUtente(file.getSubCartellaId(), userName)) {
				Utili.errorMessage(response,404, "Nessun file (ID:"+fileId+") legato all'utente");
				return;
			}
			response.setStatus(200);
			response.setContentType("application/json");
			response.getWriter().append(jsonFILE(file));
			
		} catch (SQLException e) {
			e.printStackTrace();
			Utili.errorMessage(response,500, "db error");
		}
		
	}
	private String jsonFILE(Documento fi) {
		return "{\"nome\":\""+fi.getFileName()+"\",\"sum\":\""+Utili.getOriginalStr(fi.getSommario())+"\",\"mime\":\""+fi.getMime()+"\",\"data\":\""+fi.getDataCreazione()+"\"}";
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
