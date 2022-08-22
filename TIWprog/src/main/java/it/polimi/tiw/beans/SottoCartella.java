package it.polimi.tiw.beans;

import java.sql.Date;
import java.util.List;

public class SottoCartella {
	private int subCartellaId;
	private int cartellaId;
	private List<Integer> fileIds;
	private List<File> files;
	private Date dataDiCreazione;
	
	
	public int getSubCartellaId() {
		return subCartellaId;
	}
	public void setSubCartellaId(int subCartellaId) {
		this.subCartellaId = subCartellaId;
	}
	public int getCartellaId() {
		return cartellaId;
	}
	public void setCartellaId(int utenteId) {
		this.cartellaId = utenteId;
	}
	public Date getDataDiCreazione() {
		return dataDiCreazione;
	}
	public void setDataDiCreazione(Date dataDiCreazione) {
		this.dataDiCreazione = dataDiCreazione;
	}
	public List<Integer> getFileIds() {
		return fileIds;
	}
	public void setFileIds(List<Integer> fileIds) {
		this.fileIds = fileIds;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
}