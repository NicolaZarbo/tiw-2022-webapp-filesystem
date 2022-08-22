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
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class DirectoryControl
 */
@WebServlet("/HomePage")
public class HomeC extends HttpServlet {//stampa tutte cartelle e sub-cartelle
	private static final long serialVersionUID = 1L;
    private TemplateEngine tEngine;
    private CartellaDao dao;
    public HomeC() {
        super();
    }
    public void init() {
    	tEngine=Utili.getTemplateEngine(getServletContext());
    	dao=new CartellaDao(DbConnection.getConnection());
    	
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");
	    
	    String fileIdStr=request.getParameter("fileId");
	    
	    if(userName==null || sessione.isNew()) {
	    	response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }
	    if(Utili.sporca(userName) ) {
	    	response.sendError(400, "bad or missing input");	
			return;
	    }
	    	
	    List<Cartella> cartelle;	    
	    try {
			cartelle=dao.getAllCartelleAndSub(userName);
		} catch (SQLException e) {
			e.printStackTrace();//TODO gestione errore
			response.sendError(500, "db error");
			return;
		}
	    
		WebContext ctx = new WebContext(request, response, getServletContext(),request.getLocale());
		
		if(request.getParameter("spostamento")!=null && fileIdStr!=null) {
			if(!Utili.sporca(fileIdStr)) {
				ctx.setVariable("spostamento", true);
				int fileID=0;
				try {
					fileID=Integer.parseInt(fileIdStr);
				}catch (NumberFormatException e) {
					response.sendError(403, "expected integer value for fileID");
					return;
				}
				sessione.setAttribute("fileId",fileID );
			}
		}
		
		Utili.gestioneCronologia(sessione, ctx, request,"/HomePage");
		ctx.setVariable("cartelle", cartelle);
		tEngine.process(HtmlPath.HOME.getUrl(), ctx,response.getWriter());

	    
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
