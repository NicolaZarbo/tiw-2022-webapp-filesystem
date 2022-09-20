package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.parser.HttpParser;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import enumerazioni.HtmlPath;
import it.polimi.tiw.DAO.DaoDocumento;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.beans.Documento;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class CreaFile
 */
@WebServlet("/CreaFile")@MultipartConfig
public class CreaFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine tEngine;
    private DaoDocumento dao; 
    private Connection connessione;
    
    public void init() {
    	connessione=DbConnection.getConnection();
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	dao=new DaoDocumento(connessione);
    	
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
		/*HttpSession sessione= request.getSession();
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
		response.setStatus(200);
		response.getOutputStream().print(Utili.readFileAsString(getServletContext().getRealPath("/WEB-INF/insertTempl/fileCreazione.html")));
		response.setContentType("text/plain");
	   	return;
		*/
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    //Integer subCartellaId=(Integer)(sessione.getAttribute("subCartellaId"));
	    String fName=request.getParameter("filename");
		String mime=request.getParameter("mime");
		String submime=request.getParameter("submime");
		String somm=request.getParameter("sommario");

		String subCartellaId=request.getParameter("subCartellaId");
		int subC=0;

		
	    if(userName==null  || sessione.isNew()) {
	    	response.sendRedirect(getServletContext().getContextPath() + "");
			return;
	    }
	    if(submime==null ||mime==null ||fName==null || subCartellaId==null) {
	    	Utili.errorMessage(response, 400, "missing input");
	    	return;
	    }
	    if(somm!=null) {
	    	if(somm.length()==0) somm="no sommario";
	    	somm=Utili.cleanString(somm);
	    }else somm="no sommario";
	    if(submime.length()<1|| submime.length()>20||mime.length()<1||mime.length()>20||fName.length()==0 || fName.length()>20 ) {
	    	Utili.errorMessage(response,400, "errore input, troppo lungo o troppo corto");	
	    	return;
	    }
	    
		if(Utili.sporca(userName) ||Utili.sporca(fName.replace("_", "").replaceFirst(".", "")) 
				|| Utili.sporca(mime) || Utili.sporca(submime.replace("+","").replace("-",""))) 
		{
			Utili.errorMessage(response,400, "errore, input contiene caratteri o sequenze proibite");	
			return;
	    }
		try {
			subC=Integer.parseInt(subCartellaId);
		}catch (NumberFormatException e) {
			Utili.errorMessage(response,403, "expected integere value for cartella id");	

		}
		Documento file=new Documento();
		file.setFileName(fName);
		file.setMime(mime+"/"+submime);
		file.setSubCartellaId(subC);
		file.setSommario(somm);

		try {
			response.setStatus(200);
			dao.creaFile(file);
		}catch (SQLException e) {
			Utili.errorMessage(response,500,"db error");	
	    	return;		
	    }
		//response.sendRedirect(getServletContext().getContextPath() + "/GestioneContenuti");
		return;
	}

}
