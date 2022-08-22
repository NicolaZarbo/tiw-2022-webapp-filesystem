package it.polimi.tiw.beans;

import java.sql.Date;
import java.util.List;

public class Cartella {
	private int CartellaId;
	private int UtenteId;
	private Date dataDiCreazione;
	private List<Integer> subCartelle;
	
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
	public List<Integer> getSubCartelle() {
		return subCartelle;
	}
	public void setSubCartelle(List<Integer> subCartelle) {
		this.subCartelle = subCartelle;
	}


}
