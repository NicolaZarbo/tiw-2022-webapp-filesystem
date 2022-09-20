/**
 * 
 *es input : salvaInCronologia("function("+params+")");
  */
function salvaInCronologia(funz){
	const crono =JSON.parse(window.localStorage.getItem("crono"));
	crono.push(funz);
	window.localStorage.setItem("crono",JSON.stringify(crono));
}
function richiamaUltimo(){
	const preCrono=window.localStorage.getItem("crono");
	const crono=JSON.parse(preCrono);
	const attuale=crono.pop();
	if(crono.length==0){
		showHome();
		return;
	}
	const ret=crono.pop();
	window.localStorage.setItem("crono",JSON.stringify(crono));
	const ffff=new Function(ret);
	ffff();
	//chiama la funzione
}

function initCronologia(){
	const crono=[];
	window.localStorage.setItem("crono",JSON.stringify(crono));
}