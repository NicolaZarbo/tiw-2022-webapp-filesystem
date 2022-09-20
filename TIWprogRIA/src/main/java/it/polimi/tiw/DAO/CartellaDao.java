package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import it.polimi.tiw.beans.Cartella;
import it.polimi.tiw.beans.SottoCartella;


public class CartellaDao {

	private Connection connesione;
	
	public CartellaDao(Connection connesione) {
		this.connesione=connesione;
	} 
	public void chiudi() throws SQLException {
		connesione.close();
	}
	public Cartella getCartella(int cartellaId) throws SQLException {
		String query = "SELECT * FROM cartella WHERE idCartella = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, cartellaId);
			SubCartellaDao sottoDao=new SubCartellaDao(connesione);

			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Cartella folder=new Cartella();
					
					folder.setUtenteId(result.getInt("idutente"));
					folder.setDataDiCreazione(result.getDate("dataCreazione"));
					folder.setCartellaId(result.getInt("idCartella"));
					List<SottoCartella> subs=sottoDao.getAllSottoCartelle(folder.getCartellaId());
					if(subs!=null)
						folder.setSubCartelle(subs);		
				
					return folder;
				}
			}
		}
	}

	
	public void creaCartella(String User) throws SQLException{
		UDao ut=new UDao(connesione);
		int id=ut.getUtenteFromUserName(User).getId();
	
		String query = "INSERT into  cartella ( idutente,  dataCreazione) VALUES (?,?) ";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setDate(2, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			pstatement.setInt(1, id);
			pstatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	
	}


	
	public List<Cartella> getAllCartelleAndSub(int userID) throws SQLException{
		String query = "SELECT * FROM cartella WHERE idutente = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, userID);
			SubCartellaDao sottoDao=new SubCartellaDao(connesione);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					ArrayList<Cartella> cartelle=new ArrayList<>();
					while(result.next()) {
						Cartella folder=new Cartella();
						folder.setDataDiCreazione(result.getDate("dataCreazione"));
						folder.setUtenteId(result.getInt("idutente"));
						folder.setCartellaId(result.getInt("idCartella"));
						List<SottoCartella> subs=sottoDao.getAllSottoCartelle(folder.getCartellaId());
						if(subs!=null)
							folder.setSubCartelle(subs);		
						cartelle.add(folder);
					}
					return cartelle;
				}
			}
		}	
	}
	public List<Cartella> getAllCartelleAndSub(String userName) throws SQLException{
		String query = "SELECT idcartella, dataCreazione,cartella.idutente FROM cartella INNER JOIN utente WHERE cartella.idutente=utente.idutente  AND utente.userName = ? ";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, userName);
			SubCartellaDao sottoDao=new SubCartellaDao(connesione);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					ArrayList<Cartella> cartelle=new ArrayList<>();
					while(result.next()) {
						Cartella folder=new Cartella();
						folder.setDataDiCreazione(result.getDate("dataCreazione"));
						folder.setUtenteId(result.getInt("idutente"));
						folder.setCartellaId(result.getInt("idCartella"));
						List<SottoCartella> subs=sottoDao.getAllSottoCartelle(folder.getCartellaId());
						if(subs!=null)
							folder.setSubCartelle(subs);		
						cartelle.add(folder);
					}
					return cartelle;
				}
			}
		}	
	}
}