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

import org.apache.tomcat.util.http.parser.HttpParser;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.FileDao;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.beans.File;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class CreaFile
 */
@WebServlet("/CreaFile")
public class CreaFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine tEngine;
    private FileDao dao; 
    private Connection connessione;
    
    public void init() {
    	connessione=DbConnection.getConnection();
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	dao=new FileDao(connessione);
    	
    }
    public void destroy() {
		try {
			dao.chiudi();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}      
     
    public CreaFile() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    String subCStr=request.getParameter("subCartella");
	    Integer subCartellaId=null;
	    

	    if(userName==null ||  sessione.isNew()) { 
	    	response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }if(Utili.sporca(userName) || subCStr==null) {
	    	response.sendError(400, "wrong data sent");	
			return;
	    }
	    try {
	    	subCartellaId=Integer.parseInt(subCStr);
	    }catch (NumberFormatException e) {
	    	response.sendError(400, "Unexpected value, expected integer value for sub-folder");	
			return;
	    }
		
	    sessione.setAttribute("subCartellaId",subCartellaId);
	    WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
		Utili.gestioneCronologia(sessione, ctx, request,"/GestioneContenuti");

		tEngine.process(HtmlPath.FILE_CREATION.getUrl(), ctx,response.getWriter());
    	return;
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    Integer subCartellaId=(Integer)(sessione.getAttribute("subCartellaId"));
	    String fName=request.getParameter("filename");
		String mime=request.getParameter("mime");
		String submime=request.getParameter("submime");

		
	    if(userName==null || subCartellaId==null || sessione.isNew()  ) {
	    	response.sendRedirect(getServletContext().getContextPath() + "");
			return;
	    }
	    if(submime==null ||mime==null ||fName==null) {
			response.sendRedirect(getServletContext().getContextPath() + "/CreaFile?subCartella="+subCartellaId);
			return;
	    }
	    if(submime.length()==0||mime.length()==0||fName.length()==0) {
	    	response.sendRedirect(getServletContext().getContextPath() + "/CreaFile?subCartella="+subCartellaId);
			return;
	    }
	    
		if(Utili.sporca(userName) ||Utili.sporca(fName.replaceAll("_", "").replace(".", "")) 
				|| Utili.sporca(mime) || Utili.sporca(submime.replaceAll("\\+","").replaceAll("-",""))) 
		{
	    	response.sendError(400, "bad input");	
			return;
	    }
		File file=new File();
		file.setFileName(fName);
		file.setMime(mime+"/"+submime);
		file.setSubCartellaId(subCartellaId);
		
		dao.creaFile(file);
		response.sendRedirect(getServletContext().getContextPath() + "/GestioneContenuti");
		return;
	}

}
