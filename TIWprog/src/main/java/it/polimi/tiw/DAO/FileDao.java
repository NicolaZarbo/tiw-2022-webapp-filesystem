package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.polimi.tiw.beans.File;
 
public class FileDao {

	private Connection connesione;
	
	public FileDao(Connection connesione) {
		this.connesione=connesione;
	}
	public void chiudi() throws SQLException {
		connesione.close();
	}
	public File getFile(int fileId) throws SQLException {
		String query = "SELECT * FROM file WHERE idfile = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setString(1, fileId+"");
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					File file=new File();
					file.setDataCreazione(result.getDate("dataCreazione"));
					file.setFileName(result.getString("fileName"));
					//file.setOwnerId(result.getInt("ownerId"));
					file.setMime(result.getString("summary"));
					file.setSubCartellaId(result.getInt("idSubCartella"));
					file.setFileId(fileId);
					return file;
				}
			}
		}	
	}
	public boolean esisteInCartella(String Name , int subCartellaId) throws SQLException {
		String query = "SELECT * FROM file WHERE file.fileName=? AND file.idSubCartella =?";
		PreparedStatement pstatement = connesione.prepareStatement(query);
		pstatement.setString(1, Name);
		pstatement.setInt(2, subCartellaId);
		pstatement.executeUpdate();
		
		return !pstatement.executeQuery().isBeforeFirst();
	}
	public void spostaFile(int fileID, int where) {
		String query = "UPDATE file SET idSubCartella =? WHERE idfile = ?";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(2, fileID);
			pstatement.setInt(1, where);
			pstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void creaFile(File f) {
		String query = "INSERT into  file ( idsubcartella, fileName, dataCreazione, summary) VALUES (?,?,?,?) ";
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, f.getSubCartellaId());
			pstatement.setString(2, f.getFileName());
			pstatement.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			pstatement.setString(4, f.getMime());
			
			pstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public List<File> getFilesOfSubFolder(int subFolderId) throws SQLException{
				String query = "SELECT * FROM file WHERE idsubcartella = ?";
		
		try (PreparedStatement pstatement = connesione.prepareStatement(query);) {
			pstatement.setInt(1, subFolderId);
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					ArrayList<File> files=new ArrayList<File>();
					while(result.next()) {
						File file=new File();
						file.setDataCreazione(result.getDate("dataCreazione"));
						file.setFileName(result.getString("fileName"));
						//file.setOwnerId(result.getInt("ownerId"));
						file.setMime(result.getString("summary"));
						file.setSubCartellaId(subFolderId);
						file.setFileId(result.getInt("idfile"));
						
						files.add(file);
					}
					return files;
				}
			}
		}	
	}
	
}
