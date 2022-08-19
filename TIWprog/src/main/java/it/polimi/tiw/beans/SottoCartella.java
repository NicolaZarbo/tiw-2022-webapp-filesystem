package it.polimi.tiw.beans;

import java.sql.Date;

public class SottoCartella {
	private int subCartellaId;
	private int cartellaId;
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
}