let contx = "";
let fi = true;
const user = "";
let backButton;

document.addEventListener('DOMContentLoaded', function() {

	if (fi) init();
	fi = false;
});
function init() {
	contx = window.location;
	backButton = document.getElementById("back");
	initCronologia();
	window.localStorage.setItem("origin",0);
}
function updateUsr(nuUser) {
	user = nuUser;
}

function creaCartella() {
	const xm = getXMLhr();
	xm.open("get", contx + "CreaCartella", true);
	xm.onload = () => {
		if (xm.status == 200) {
			//showGestione();
			showHome();
		} else
			alert(xm.responseText);
	}
	xm.send();
}
function creaSubCartella(cartID, nome) {
	const xm = getXMLhr();
	xm.open("get", contx + "CreaSottoCartella?cartellaId=" + cartID + "&nomeSub=" + nome, true);
	xm.onload = () => {
		if (xm.status == 200) {
			showHome();
		} else
			alert(xm.responseText);
		removeForms()

	}
	xm.send();
}
function showHome() {
	const xm = getXMLhr();
	xm.open("get", contx + "HomePage", true);
	xm.onload = () => {
		if (xm.status = 200) {
			jsonToCartelleAndSub(xm.responseText);
			initCronologia()
			backButton.style = "visibility: hidden;";
		} else if (xm.status > 399)
			alert(xm.responseText);
	}
	xm.send();

}

function showDocumenti(subId) {
	const xm = getXMLhr();
	xm.open("get", contx + "Documenti" + "?subCartellaId=" + subId);
	xm.send();
	xm.onload = function() {
		if (xm.status == 200) {
			jsonToDocumenti(xm.responseText,subId);
			backButton.style = "visibility: visible;";
			//gestButton.style = "visibility: visible;";

		} else if (xm.status == 204) {alert(xm.responseText) }
		else alert(xm.responseText);
	}
}

function cancella(fileId) {
	const xm = getXMLhr();
	xm.open("post", contx + "CancellaFile", true);
	const ff = new FormData();
	//ff.append("subCartellaId", subId)
	ff.append("fileId", fileId)
	xm.send(ff);
	xm.onload = function() {
		if (xm.status == 200) {
			showHome()
		} else if (xm.status > 399)
			alert(xm.responseText);
	}
}
function spostaFile(fileId, subId) {
	const xm = getXMLhr();
	xm.open("post", contx + "SpostaFile", true);
	const ff = new FormData();
	ff.append("subCartellaId", subId)
	ff.append("fileId", fileId)
	xm.send(ff);
	xm.onload = function() {
		if (xm.status == 200) {
			showHome()
		} else if (xm.status > 399)
			alert(xm.responseText);
	}
}
function showFile(fileId) {
	const xm = getXMLhr();
	xm.open("get", contx + "AccediDocumento?fileId=" + fileId,true);
	xm.send();
	xm.onload = function() {
		if (xm.status == 200) {
			printJsonFile(xm.responseText);
			backButton.style = "visibility: visible;";
			//gestButton.style = "visibility: visible;";
			salvaInCronologia("showFile()")

		} else if (xm.status > 399)
			alert(xm.responseText);
	}
}
function creaFile(formData) {
	const xm = getXMLhr();
	xm.open("post", contx + "CreaFile", true);
	xm.onload = () => {
		if (xm.status == 200) {
			showHome();
			document.getElementById("topZone").style = "visibility:visible";
		} else {
			alert(xm.responseText)
			removeForms()

		}
	}
	xm.send(formData);
}
function vaiACreaFile(subId) {
	const xm = getXMLhr();
	xm.open("get", contx + "CreaFile?subCartella=" + subId,true);
	xm.send();
	xm.onload = function() {
		if (xm.status == 200) {
			//jsonToCartelleAndSub(xm.responseText);
			getAppDiv().innerHTML = '<div  class="templContainer" >' + xm.responseText + '</div>';
			backButton.style = "visibility: visible;";
			//gestButton.style = "visibility: visible;";
		} else if (xm.status > 399)
			alert(xm.responseText);
	}
}

function logout() {
	document.getElementById("topZone").style = "visibility:collapse";
}
function showRegistrazione() {
	const xm = getXMLhr();
	xm.open("get", contx + "registrazione");
	xm.send();
	let html;
	xm.onload = function() {
		if (xm.status == 200) {
			getAppDiv().innerHTML = '<div  class="templContainer" >' + xm.responseText + '</div>';
			const but=document.getElementById("mostra");
			but.onmousedown=(cl)=>showPsw(cl);
			but.onmouseup=(cl)=>obsPsw(cl);
		} else if (xm.status > 399)
			alert(xm.responseText);
	}
}
function checkRegistrazione() {
	const error=document.getElementById("err")
	const form = new FormData(document.getElementById("form"));
	if ( form.get("password") == "" || form.get("username") == "" || form.get("mail") == "") {
		//alert("missing form inputs")
		error.innerText="missing form inputs"
		return;
	}
	if (form.get("password") != form.get("password2") ) {
		//alert("passwords don't match")
		error.innerText="passwords don't match"

		return;
	}
	const xm = getXMLhr();
	xm.open("post", contx + "registrazione", true);
	xm.onload = () => {
		if (xm.status == 200) {
			showHome();
			document.getElementById("topZone").style = "visibility:visible";
		} else if (xm.status == 406 || xm.status == 400) {
			document.getElementById("err").innerHTML = xm.response;

		}
	}
	xm.send((form));
}
function showLogin() {
	const xm = getXMLhr();
	xm.open("get", contx + "redirectLogin",true);
	xm.send();
	//let html;
	xm.onload = () => {
		if (xm.status == 200) {
			getAppDiv().innerHTML = '<div  class="templContainer" >' + xm.responseText + '</div>';
		} else if (xm.status > 399)
			alert(xm.responseText);
	}
}
function checkLogin() {//find login div and take values

	const form = new FormData(document.getElementById("form"));
	if (form.get("password") == "" || form.get("username") == "" || form.get("mail") == "") {
		alert("missing or wrong inputs")
		return;
	}
	const xm = getXMLhr();
	xm.open("post", contx + "login", true);
	xm.onload = () => {
		if (xm.status == 200) {
			showHome();
			document.getElementById("topZone").style = "visibility:visible";
		} else if (xm.status == 406) {
			document.getElementById("err").innerHTML = "credenziali errate";
		} else document.getElementById("err").innerHTML = xm.response;


	}
	xm.send(form);
}

function controlRegex(str) {
	return str.contains()
}
function getAppDiv() {
	return document.getElementById("app");
}

/**@returns {XMLHttpRequest} */
function getXMLhr() {
	return new XMLHttpRequest()
}
function showPsw(click){
	click.preventDefault();
	const pp1=document.getElementById("pwd");
	const pp2=document.getElementById("pwd2");

	if(pp1.type="password"){
		pp1.type="text";
		pp2.type="text";
	}else if(pp1.type="text"){
		pp1.type="password";
		pp2.type="password";
	}

}
function obsPsw(click){
	click.preventDefault();
	const pp1=document.getElementById("pwd");
	const pp2=document.getElementById("pwd2");

	if(pp1.type="text"){
		pp1.type="password";
		pp2.type="password";
		}
}
