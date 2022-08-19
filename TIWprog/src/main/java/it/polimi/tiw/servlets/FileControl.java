package it.polimi.tiw.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FileControl
 */
@WebServlet("/Documento")
public class FileControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int userId=0;//get from session???? also check against session if Ids are ok!
		HttpSession se=request.getSession();
		 int fileId=Integer.parseInt(request.getPathInfo());
		 se.setAttribute("fileId",fileId );//ci butto user id -> cartellaId-> sub-cartllaId->fileId così posso tornare al punto superiore
		//ottieni da session info su cartella
		String fileName;
		String fileDate;
		String ownerName;
		//chiamata sql per informazioni su file
		PrintWriter out=response.getWriter();
		out.print("file name: "+ fileName+ " \nfileId : "+fileId+" \nCreation Date : "+fileDate+"\nOwner : "+ownerName);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
