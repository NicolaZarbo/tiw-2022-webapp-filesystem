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
import it.polimi.tiw.DAO.DaoDocumenti;
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
    private DaoDocumenti dao; 
    private Connection connessione;
    
    public void init() {
    	connessione=DbConnection.getConnection();
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	dao=new DaoDocumenti(connessione);
    	
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
	    String subNSUBStr=request.getParameter("nSubCartella");
	    Integer subCartellaId=null; 
	    int nSubC=0;

	    if(userName==null ||  sessione.isNew()) { 
	    	response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }if(Utili.sporca(userName) || subCStr==null||subNSUBStr==null) {
	    	response.sendError(400, "wrong data sent");	
			return;
	    }
	    try {
	    	subCartellaId=Integer.parseInt(subCStr);
	    	nSubC=Integer.parseInt(subNSUBStr);
	    }catch (NumberFormatException e) {
	    	response.sendError(400, "Unexpected value, expected integer value for sub-folder");	
			return;
	    }
		
	    sessione.setAttribute("subCartellaId",subCartellaId);
	    sessione.setAttribute("nSubCartella",nSubC);

	    WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
		Utili.gestioneCronologia(sessione, ctx, request,"/GestioneContenuti");

		tEngine.process(HtmlPath.FILE_CREATION.getUrl(), ctx,response.getWriter());
    	return;
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    Integer subCartellaId=(Integer)(sessione.getAttribute("subCartellaId"));
	    Integer nSubCartella=(Integer)(sessione.getAttribute("nSubCartella"));

	    String fName=request.getParameter("filename");
		String mime=request.getParameter("mime");
		String submime=request.getParameter("submime");
		String somm=request.getParameter("sommario");


		
	    if(userName==null ||nSubCartella==null || subCartellaId==null || sessione.isNew()) {
	    	response.sendRedirect(getServletContext().getContextPath() + "");
			return;
	    }
	    if(somm!=null) {
	    	if(somm.length()==0) somm="no sommario";
	    	somm=Utili.cleanString(somm);
	    }else somm="no sommario";
	   // System.out.println(somm);
	    if(submime==null ||mime==null ||fName==null) {
			response.sendRedirect(getServletContext().getContextPath() + "/CreaFile?subCartella="+subCartellaId);
			return;
	    }
	    if(submime.length()<1|| submime.length()>20||mime.length()<1||mime.length()>20||fName.length()==0 || fName.length()>20 ) {
	    	response.sendRedirect(getServletContext().getContextPath() + "/CreaFile?subCartella="+subCartellaId);
			return;
	    }
	    
		if(Utili.sporca(userName) ||Utili.sporca(fName.replace("_", "").replaceFirst("\\.", "")) 
				|| Utili.sporca(mime) || Utili.sporca(submime.replace("+","").replace("-",""))) 
		{
	    	response.sendError(400, "bad input");	
			return;
	    }
		Documento file=new Documento();
		file.setFileName(fName);
		file.setMime(mime+"/"+submime);
		file.setSubCartellaId(subCartellaId);
		file.setSommario(somm);

		
		try {
			dao.creaFile(file);
		} catch (SQLException e) {
			response.sendError(500, "db error");	
			return;
		}
		response.sendRedirect(getServletContext().getContextPath() + "/Documenti?subCartellaId="+subCartellaId+"&nSubCartella="+nSubCartella);
		return;
	}

}
