package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Utente;

public class DaoUtenti {
private Connection connesione;
	
	public DaoUtenti(Connection conn) {
		connesione=conn;
	}
	public void chiudi() throws SQLException {
		connesione.close();
	}
	
	public Utente getUtenteFromID(int idUtente) throws SQLException {
		String query = "SELECT * FROM utente WHERE idutente = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, idUtente);
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Utente utente=new Utente();
					utente.setId(idUtente);
					utente.setNickName(result.getString("userName"));
					return utente;
				}
			}
		}	
	}
	public Utente getUtenteFromUserName(String userN) throws SQLException {
		String query = "SELECT * FROM utente WHERE userName = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, userN);
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Utente utente=new Utente();
					utente.setId(result.getInt("idutente"));
					utente.setNickName(result.getString("userName"));
					return utente;
				}
			}
		}	
	}
	
	public boolean checkExists(String userN) throws SQLException {
		Utente xx=this.getUtenteFromUserName(userN);
		return xx!=null;
	}
	public boolean login(String userN, String password) throws SQLException {
		String query = "SELECT * FROM utente WHERE userName = ? AND password = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, userN);
			pstatement.setString(2, password);

		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return false;
				else {
					return true;
				}
			}
		}	
		
	}
	//controllo di presenza userN a priori
	public void registerNewUser(String userN, String password) throws SQLException {
		String query = "INSERT into  utente ( userName,password ) VALUES (?,?) ";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, userN );
			pstatement.setString(2, password );

			
			pstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException(e.getMessage());
		}	
	}
	
}
