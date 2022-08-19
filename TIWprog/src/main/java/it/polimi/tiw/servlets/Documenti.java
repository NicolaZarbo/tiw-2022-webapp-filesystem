package it.polimi.tiw.servlets;+

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utlli.Utili;

/**
 * Servlet implementation class Documenti
 */
@WebServlet("/Documenti")
public class Documenti extends HttpServlet {//stampa file di sottocartella
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Documenti() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		
		 HttpSession se=request.getSession();
		 int fileId=Integer.parseInt(request.getPathInfo());
		 se.setAttribute("fileId",fileId );//ci butto user id -> cartellaId-> sub-cartllaId->fileId così posso tornare al punto superiore
		//ottieni da session info su cartella
		 Connection con= Utili.
		 String fileName;
		 String owner;
		 //prendi info e buttale in sti valori
		 //capisci come creare pseudo html, per link e bottoni lmaooo
		 //prepara pattern per sql con fileId come parametro, ottieni 
		int idCartella=0;
		//stampa html con lista file e pulsanti di accedi file e sposta
		out.println("fileName: "+fileName+" \nOwner: "+owner+"");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
