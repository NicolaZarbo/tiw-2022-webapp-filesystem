package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.DAO.CartellaDao;
import it.polimi.tiw.DAO.UDao;
import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;



@WebServlet("/CreaCartella")
public class CreaCartella extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CartellaDao dao; 
    
    public void init() {
    	dao=new CartellaDao(DbConnection.getConnection());
    	
    }
    public void destroy() {
		try {
			dao.chiudi();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}   
   
    public CreaCartella() {
        super();
    }



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione= request.getSession();
	    String userName=(String)sessione.getAttribute("user");

		if(userName==null || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath()+"");
	    	return;
	    }
		if(Utili.sporca(userName)) {
			Utili.errorMessage(response,400, "bad input");	
			return;
	    }
		try {
			response.setStatus(200);
			dao.creaCartella(userName);
		} catch (SQLException e) {
			Utili.errorMessage(response,500, "db error");	
			return;
		}
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
