package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.beans.Documento;
import it.polimi.tiw.beans.SottoCartella;

public class SubCartellaDao {
private Connection connesione;
	
	public SubCartellaDao(Connection connesione) {
		this.connesione=connesione;
	}
	
	public SottoCartella getCartellaAndFiles(int subCartellaId) throws SQLException {
		String query = "SELECT * FROM subcartella WHERE idsubCartella = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, subCartellaId);
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					DaoDocumenti fDao=new DaoDocumenti(connesione);
					List<Documento> files=fDao.getFilesOfSubFolder(subCartellaId);
					result.next();
					SottoCartella folder=new SottoCartella();
					folder.setSubCartellaId(subCartellaId);
					folder.setDataDiCreazione(result.getDate("dataCreazione"));
					folder.setCartellaId(result.getInt("idCartella"));
					if(files!=null)
						folder.setFiles(files);
					return folder;
				}
			}
		}
	}
	
	
	public boolean isSottoCartellaDiUtente(int sottoCartellaId, String user) throws SQLException {
		String query = "SELECT * FROM (subcartella INNER JOIN cartella ON "
				+ "subcartella.idCartella=cartella.idcartella)  INNER JOIN	"
				+ "utente ON utente.idutente=cartella.idutente  WHERE "
				+ "subcartella.idsubCartella= ?  AND utente.userName= ? ";
		PreparedStatement pstatement = connesione.prepareStatement(query);
		pstatement.setInt(1, sottoCartellaId);
		pstatement.setString(2,user);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) 
			return false;
		else return true;
	}
	
	public SottoCartella getSottoCartella(int cartellaId) throws SQLException {
		String query = "SELECT * FROM subcartella WHERE idCartella = ?";
		
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
	
	public void creaSottoCartella(int cartellaId) throws SQLException {
		String query = "INSERT into  subcartella ( idCartella,  dataCreazione) VALUES (?,?) ";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setDate(2, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			pstatement.setInt(1, cartellaId);
			pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e.getLocalizedMessage());
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

	public void chiudi() throws SQLException {
		connesione.close();
		
	}
}
