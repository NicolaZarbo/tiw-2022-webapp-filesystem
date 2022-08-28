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

import it.polimi.tiw.DAO.DaoDocumenti;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class SpostaFile
 */
@WebServlet("/SpostaFile")
public class SpostaFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connessione;
    private DaoDocumenti fDao; 
    private SubCartellaDao subDao;
    
    public void init() {
    	connessione= DbConnection.getConnection();
    	fDao=new DaoDocumenti(connessione);
    	subDao=new SubCartellaDao(connessione);
    }
    public void destroy() {
		try {
			connessione.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}   
    
    public SpostaFile() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 HttpSession sessione= request.getSession();
		 String subCstr=request.getParameter("subCartellaId");
		 int subCartellaId=0;
		 Integer fileId=(Integer)(sessione.getAttribute("fileId"));
		 String userName=(String)sessione.getAttribute("user");
		 
		 if(userName==null || sessione.isNew()) {
		   	response.sendRedirect(getServletContext().getContextPath()+"");
		   	return;
		 }
		 if(Utili.sporca(userName) || fileId==null || subCstr==null) {
		   	response.sendError(400, "bad or missing input");	
			return;
		} 
		 try {
			 subCartellaId=Integer.parseInt(subCstr);
		 }catch (NumberFormatException e) {
			 response.sendError(400, "expected integer value");	
				return;		
		}
		     
		try {
			int subC=fDao.getFile(fileId).getSubCartellaId();
			if(!subDao.isSottoCartellaDiUtente(subC, userName) || subC==subCartellaId) {
				response.sendError(403, "origine:"+subC+" ->  direzione:"+subCartellaId);	
				return;
			} 
			fDao.spostaFile(fileId, subCartellaId);
		} catch (SQLException e) {
			response.sendError(500,"db error");	
			e.printStackTrace();
			return;
		}
		
		sessione.invalidate();
		sessione=request.getSession();
		
		sessione.setAttribute("user",userName);

		response.sendRedirect(getServletContext().getContextPath()+"/HomePage");

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
