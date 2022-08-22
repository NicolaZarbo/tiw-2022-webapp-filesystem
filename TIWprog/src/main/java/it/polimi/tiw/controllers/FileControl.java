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
import it.polimi.tiw.DAO.FileDao;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.beans.File;
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
    private FileDao fDao; 
    private SubCartellaDao subDao;
    
    
    public void init() {
    	connessione= DbConnection.getConnection();
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	fDao=new FileDao(connessione);
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
		Integer cartellaId=(Integer)sessione.getAttribute("cartellaId");
		Integer nSubCartella=(Integer)sessione.getAttribute("nSubCartella");
		
		String fileIDstr=request.getParameter("fileId");
		int fileId=0;
		if(userName==null ||subCartellaId==null ||cartellaId==null || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }
	    if(Utili.sporca(userName) || fileIDstr==null) {
	    	response.sendError(400, "bad or missing input");	
			return;
	    }
		try {
			fileId=Integer.parseInt(fileIDstr);
		}catch (NumberFormatException e) {
			response.sendError(400, "Expected integer value");	
			return;
		}
		try {
			
			File file=fDao.getFile(fileId);
			if(file==null) {
				response.sendError(404, "file not found");
				return;
			}
			if(!subDao.isSottoCartellaDiUtente(file.getSubCartellaId(), userName)) {
				response.sendError(404, "Nessun file (ID:"+fileId+") legato all'utente");
				return;
			}
			WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
			Utili.gestioneCronologia(sessione, ctx, request,"/AccediDocumento?" +request.getQueryString());

			ctx.setVariable("subCartellaId", subCartellaId);
			ctx.setVariable("cartellaId",cartellaId);
			ctx.setVariable("nSubCartella",nSubCartella);
			ctx.setVariable("file", file);
			tEngine.process(HtmlPath.DOCUMENTO.getUrl(), ctx,response.getWriter());
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(500, "db error");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
