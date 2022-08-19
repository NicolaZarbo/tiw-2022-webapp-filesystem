package it.polimi.tiw.beans;

import java.sql.Date;

public class Cartella {
	private int CartellaId;
	private int UtenteId;
	private Date dataDiCreazione;
	
	public int getCartellaId() {
		return CartellaId;
	}
	public void setCartellaId(int cartellaId) {
		CartellaId = cartellaId;
	}
	public int getUtenteId() {
		return UtenteId;
	}
	public void setUtenteId(int utenteId) {
		UtenteId = utenteId;
	}
	public Date getDataDiCreazione() {
		return dataDiCreazione;
	}
	public void setDataDiCreazione(Date dataDiCreazione) {
		this.dataDiCreazione = dataDiCreazione;
	}


}
