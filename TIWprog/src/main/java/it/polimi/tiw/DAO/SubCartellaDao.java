package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.beans.File;
import it.polimi.tiw.beans.SottoCartella;
import it.polimi.tiw.utlli.DbConnection;

public class SubCartellaDao {
private Connection connesione;
	
	public SubCartellaDao() {
		connesione=DbConnection.getConnection();
	}

	public SottoCartella getCartella(int cartellaId) throws SQLException {
		String query = "SELECT * FROM cartella WHERE idCartella = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, cartellaId+"");
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					SottoCartella folder=new SottoCartella();
					
					folder.setSubCartellaId(cartellaId);
					folder.setDataDiCreazione(result.getDate("dataCreazione"));
					folder.setCartellaId(result.getInt("idCartella"));
					
					return folder;
				}
			}
		}
	}
	public List<SottoCartella> getAllSottoCartelle(int cartellaPadreId) throws SQLException{
		String query = "SELECT * FROM subcartella WHERE idCartella = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, cartellaPadreId);
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					ArrayList<SottoCartella> subCartelle=new ArrayList<>();
					while(result.next()) {
					result.next();
					SottoCartella sub=new SottoCartella();
					sub.setDataDiCreazione(result.getDate("dataCreazione"));
					sub.setSubCartellaId(result.getInt("idsubCartella"));
					sub.setCartellaId(result.getInt("idCartella"));
							
					subCartelle.add(sub);
					}
					return subCartelle;
				}
			}
		}	
	}
}
