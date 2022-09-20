package enumerazioni;

public enum HtmlPath {
	DOCUMENTI("WEB-INF/Documenti.html"),
	DOCUMENTO("WEB-INF/Documento.html"),
	HOME("WEB-INF/HomePage.html"),
	GESTIONE_CONTENUTI("WEB-INF/GestioneContenuti.html"),
	//LOGIN("WEB-INF/insertTempl/Login.html"),
	REGISTRAZIONE("WEB-INF/insertTempl/Registrazione.html"),
	FILE_CREATION("WEB-INF/FileCreation.html");
	private String url;
	HtmlPath(String url) {
		this.url=url;
	}
	public String getUrl() {
		return url;
	}
}
