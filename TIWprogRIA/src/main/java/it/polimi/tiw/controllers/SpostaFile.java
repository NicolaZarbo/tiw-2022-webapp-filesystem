package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.DAO.CartellaDao;
import it.polimi.tiw.DAO.DaoDocumento;
import it.polimi.tiw.DAO.SubCartellaDao;
import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.beans.SottoCartella;
import it.polimi.tiw.utlli.DbConnection;
import it.polimi.tiw.utlli.Utili;

/**
 * Servlet implementation class SpostaFile
 */
@WebServlet("/SpostaFile") @MultipartConfig
public class SpostaFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connessione;
	private DaoDocumento fDao;
	private SubCartellaDao subDao;

	public void init() {
		connessione = DbConnection.getConnection();
		fDao = new DaoDocumento(connessione);
		subDao = new SubCartellaDao(connessione);
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		HttpSession sessione = request.getSession();
		String subCstr = request.getParameter("subCartellaId");
		int subCartellaId = 0;
		int fileId=0;
		String fileIdStr = (request.getParameter("fileId"));
		String userName = (String) sessione.getAttribute("user");

		if (userName == null || sessione.isNew()) {
			response.sendRedirect(getServletContext().getContextPath() + "");
			return;
		}
		if (Utili.sporca(userName) || fileIdStr == null || subCstr == null) {
			Utili.errorMessage(response,400, "bad or missing input");
			return;
		}
		try {
			fileId= Integer.parseInt(fileIdStr);
			subCartellaId = Integer.parseInt(subCstr);
		} catch (NumberFormatException e) {
			Utili.errorMessage(response,400, "expected integer value");
			return;
		}

		try {
			int subC = fDao.getFile(fileId).getSubCartellaId();
			if (!subDao.isSottoCartellaDiUtente(subC, userName) || subC == subCartellaId) {
				Utili.errorMessage(response,403, "origine:" + subC + " ->  direzione:" + subCartellaId);
				return;
			}
			response.setStatus(200);
			fDao.spostaFile(fileId, subCartellaId);
		} catch (SQLException e) {
			Utili.errorMessage(response,500, "db error");
			e.printStackTrace();
			return;
		}

		sessione.invalidate();
		sessione = request.getSession();

		sessione.setAttribute("user", userName);
		response.setStatus(200);

	}

}
