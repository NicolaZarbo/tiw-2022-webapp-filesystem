package it.polimi.tiw.beans;

import java.sql.Date;

public class File {
	private int FileId;
	private int ownerId;
	private int SubCartellaId;
	private String fileName;
	private String summary;//or mime type?
	private Date dataCreazione;
	
	
	public int getFileId() {
		return FileId;
	}
	public void setFileId(int fileId) {
		FileId = fileId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getSubCartellaId() {
		return SubCartellaId;
	}
	public void setSubCartellaId(int subCartellaId) {
		SubCartellaId = subCartellaId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	
}
