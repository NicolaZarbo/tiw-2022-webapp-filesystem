package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.polimi.tiw.beans.Documento;
import it.polimi.tiw.utlli.Utili;
 
public class DaoDocumenti {

	private Connection connesione;
	
	public DaoDocumenti(Connection connesione) {
		this.connesione=connesione;
	}
	public void chiudi() throws SQLException {
		connesione.close();
	}
	public Documento getFile(int fileId) throws SQLException {
		String query = "SELECT * FROM documento WHERE idDocumento = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, fileId+"");
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Documento file=new Documento();
					file.setDataCreazione(result.getDate("dataCreazione"));
					file.setFileName(result.getString("fileName"));
					file.setSommario(Utili.getOriginalStr(result.getString("sommario")));
					file.setMime(result.getString("mime"));
					file.setSubCartellaId(result.getInt("idSubCartella"));
					file.setIdDocumento(fileId);
					return file;
				}
			}
		}	
	}
	public boolean esisteInCartella(String Name , int subCartellaId) throws SQLException {
		String query = "SELECT * FROM documento WHERE documento.idDocumento=? AND documento.idSubCartella =?";
		PreparedStatement pstatement = connesione.prepareStatement(query);
		pstatement.setString(1, Name);
		pstatement.setInt(2, subCartellaId);
		pstatement.executeUpdate();
		
		return !pstatement.executeQuery().isBeforeFirst();
	}
	public void spostaFile(int fileID, int where) throws SQLException {
		String query = "UPDATE documento SET idsubcartella =? WHERE idDocumento = ?";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(2, fileID);
			pstatement.setInt(1, where);
			pstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException();
		}	
	}
	public void creaFile(Documento f) throws SQLException {
		String query = "INSERT into  documento ( idsubcartella, fileName, dataCreazione, mime, sommario) VALUES (?,?,?,?,?) ";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, f.getSubCartellaId());
			pstatement.setString(2, f.getFileName());
			pstatement.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			pstatement.setString(4, f.getMime());
			pstatement.setString(5, f.getSommario());

			
			pstatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException();
			//e.printStackTrace();
		}	
	}
	
	
	public List<Documento> getFilesOfSubFolder(int subFolderId) throws SQLException{
				String query = "SELECT * FROM documento WHERE idsubcartella = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, subFolderId);
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					ArrayList<Documento> files=new ArrayList<Documento>();
					while(result.next()) {
						Documento file=new Documento();
						file.setDataCreazione(result.getDate("dataCreazione"));
						file.setFileName(result.getString("fileName"));
						file.setSommario(Utili.getOriginalStr(result.getString("sommario")));
						file.setMime(result.getString("mime"));
						file.setSubCartellaId(subFolderId);
						file.setIdDocumento(result.getInt("idDocumento"));
						
						files.add(file);
					}
					return files;
				}
			}
		}	
	}
	
}
