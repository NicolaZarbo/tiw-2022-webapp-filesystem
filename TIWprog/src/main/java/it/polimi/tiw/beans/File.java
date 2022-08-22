package it.polimi.tiw.beans;

import java.sql.Date;

public class File {
	private int FileId;
	private int ownerId;
	private int SubCartellaId;
	private String fileName;
	private String mime;
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
	public String getMime() {
		return mime;
	}
	public void setMime(String summary) {
		this.mime = summary;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	
}
