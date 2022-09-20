
function cestinoo(divEl){
	const divCont=document.createElement("div");
	
	divCont.id="dropCestTarget";

	const cest=document.createElement("div");
	divEl.appendChild(cest);
	cest.className="cestBase"
	cest.appendChild(divCont);
	const pX=cest.getBoundingClientRect().x;
	const lines=[document.createElement("div"),document.createElement("div"),document.createElement("div")]
	for(let i=0;i<3;i++){
		lines[i].className="vLine";
		lines[i].style="left:"+(pX+29*i+22)+"px"
		cest.appendChild(lines[i]);
	}
	const op=document.createElement("div");
	cest.appendChild(op);
	op.className="opening";
	const copp=document.createElement("div");
	divCont.appendChild(copp);
	copp.className="cop";
	copp.id="cop";
	return cest;
}
/**
 * 
 * @param {String} json 
 * @param {int} origin the id of the original sub cartella in case of spostamento
 */
function jsonToCartelleAndSub(json){
	const pigna=document.createElement("a");

    if(json.length==0)
    	return;
    const origin=window.localStorage.getItem("origin");
    const aDiv =document.getElementById("app");
    aDiv.innerHTML ="";
     
    pigna.className="createButton";
	pigna.innerText="Crea cartella";
	pigna.setAttribute("onclick","creaCartella()");
	aDiv.appendChild(pigna)
	
    const title=document.createElement("h1");
    title.innerText="Home Page";
    let dts=[]
    aDiv.ondragover=(drag)=>{
		drag.preventDefault();
		dts=drag.dataTransfer.getData("text/plain").split("&");
		title.innerText="scegli cartella dove spostare il file "+dts[0]
		}	
	aDiv.ondrop=(drag)=>{drag.preventDefault();}
	const cestino=cestinoo(aDiv);
	const cop=document.getElementById("cop");
	cestino.ondragover=(dr)=>{dr.preventDefault()}
	cestino.ondragenter=(dr)=>{dr.preventDefault();cop.classList.add("dragCestino")}
	cestino.ondragexit=(dr)=>{dr.preventDefault();cop.classList.remove("dragCestino")}	
	cestino.ondrop=(drop)=>{
		drop.preventDefault();
		cop.classList.remove("dragCestino")		
		dropCestino(aDiv, dts[1],dts[0])
		//cancella(dts[1])
	}
	cestino.id="cestino";	
    aDiv.appendChild(title);
	const cartelleList=JSON.parse(json);
	
	let cartCont=document.createElement("div")
	aDiv.appendChild(cartCont);
	cartCont.className="carMan";
	let count=1;
	cartelleList.forEach(cartellaOb => {
		let cart=document.createElement("div")
		let cartP=document.createElement("p")
		cartP.innerHTML="Cartella "+count;
		cart.appendChild(cartP);
		cart.className="cartelle";
		cart.className+=(count%2==0)?"":" cartelle2"
		let creaBot=document.createElement("button")
		creaBot.innerHTML="creaSottoCartella";
		creaBot.onclick=()=>{showFormSubCartella(cartellaOb['cId'],cart)}
		cart.appendChild(creaBot);
		creaBot.className="cartButton"
		cartCont.appendChild(cart);
		let subCont=document.createElement("div")
		cart.appendChild(subCont);
		//let innerCount=1;
		
		cartellaOb["subs"].forEach(sub => { 
			let subDiv=document.createElement("div");
			subDiv.ondragover=(dr)=>{if(origin!=sub.id)dr.preventDefault()}
			subDiv.ondragenter=(dr)=>{if(origin!=sub.id)subDiv.classList.add("dragTarget"); else subDiv.classList.add("origine")}
			subDiv.ondragexit=(dr)=>{if(origin!=sub.id)subDiv.classList.remove("dragTarget"); else subDiv.classList.remove("origine")}

			subDiv.ondrop=(drop)=>{	
				drop.preventDefault();
				dropp(sub.id,dts)
			}
			let but=document.createElement("button");
			but.onclick=()=>{creaFileForm(subDiv,sub["id"],but)}
			but.innerText="Crea file";
			subDiv.appendChild(but);
			//creaFileButton(sub,subId);
			subCont.appendChild(subDiv);
			subDiv.className="subCartella";
			let subLink=document.createElement("a");
			subLink.innerHTML=sub["nome"];

			if(origin==sub.id){
				subDiv.classList.add("origine2");
				subLink.innerHTML="ORIGINE";
				window.localStorage.setItem("origin",0);//reset
				}
			subLink.className="subCartellaLink";
			subLink.onclick=()=>{
				showDocumenti(sub["id"]);
				salvaInCronologia("showDocumenti(" + sub["id"] + ")")
			};
			

			subDiv.appendChild(subLink);
		});
		 count++;
	});
}
/**@param {DragEvent} drag*/
function dropp(subId,dts){
	const fileId=dts[1]
	spostaFile(fileId,subId)
}
function dropCestino(fatherEl,fileId,fileName){
	const sicuro=document.createElement("div")
	fatherEl.appendChild(sicuro);
	sicuro.className="sicuro"
	const pa=document.createElement("p")
	const bottoneOk=document.createElement("button")
	const bottoneNo=document.createElement("button")
	bottoneOk.onclick=()=>{cancella(fileId)}
	bottoneNo.onclick=()=>{showHome();}
	bottoneNo.innerText="no"
	bottoneOk.innerText="si"
	pa.innerText="Sicuro di voler cancellare il file '"+fileName+"' ?";
	sicuro.appendChild(pa);
	sicuro.appendChild(bottoneNo);
	sicuro.appendChild(bottoneOk);
}

function creaFileForm(subEl,subId,but){
	let submit=document.createElement("button");
	const form=document.createElement("form");
	form.style="top:"+(but.getBoundingClientRect().y)+"px; left:"+(but.getBoundingClientRect().x)+"px";

	form.className="flyingForm";
	const nome=document.createElement("input");
	nome.type="text"
	nome.pattern="[A-Za-z0-9_]{1,15}[.]{1,1}[a-z]{1,4}"
	nome.name="filename";
	const selMime=document.createElement("select");
	selMime.name="mime";
	const options=["application","audio","font","image","message","model","multipart","text","video"];
	for(let i=0;i<9;i++){
		const val=document.createElement("option");
		val.value=options[i];
		val.innerHTML=options[i]
		selMime.appendChild(val);
	}
	const subT=document.createElement("input");
	subT.type="text"
	subT.pattern="[A-Za-z0-9_]{1,20}";
	subT.name="submime";
	const names=["filename","mime","submime","sommario"];
	const labls=[];
	for(let i=0;i<4;i++){
		let lab=document.createElement("label");
		lab.for=names[i]
		lab.innerText=names[i].toUpperCase()+" :";
		labls.push(lab);
	}
	const summ=document.createElement("input");
	summ.type="text";
	summ.name="sommario"
	summ.pattern="[a-zA-Z0-9,.;:_]{0,255}";
	let formInsides=[
		labls[0],nome,document.createElement("br"),
		labls[1],selMime,document.createElement("br"),
		labls[2],subT,document.createElement("br"),
		labls[3], summ,document.createElement("br"),submit];
	for(let i=0;i<formInsides.length;i++){
		form.appendChild(formInsides[i]);
	}
	removeForms();
	subEl.appendChild(form)
	submit.innerText="submit";
	 submit.onclick=(sub)=>{
		sub.preventDefault()
		const formData =new FormData( form);
		formData.append("subCartellaId",subId);
		creaFile(formData);	 
		showHome()
	}
}
function removeForms(){
	const olds=document.getElementsByClassName("flyingForm")
	for (let i=0;i<olds.length;i++)
		olds.item(i).parentNode.removeChild(olds.item(i));
}
function showFormSubCartella(cartId, cartElement){//click.screenY-click.screenX-
	const miniForm=document.createElement("form")
	miniForm.style="top:"+(cartElement.getBoundingClientRect().y)+"px; left:"+(cartElement.getBoundingClientRect().x)+"px";
	const label=document.createElement("label")
	label.for="nomeSub";
	label.innerText="nome subCartella"
	const input=document.createElement("input")
	input.type="text";
	input.name="nomeSub";
	input.pattern="[a-zA-Z0-9_]{1,15}";
	const but=document.createElement("button")
	but.innerText="submit"
	but.onclick=(submit)=>{
		submit.preventDefault();
		const n=new FormData(miniForm);
		creaSubCartella(cartId,n.get("nomeSub"))
	}
	miniForm.className="flyingForm"
	miniForm.appendChild(label)
	miniForm.appendChild(input)
	miniForm.appendChild(but)
	removeForms();
	cartElement.appendChild(miniForm)
}
function printJsonFile(json){
	if(json.length==0)
    	return;
    const aDiv = getAppDiv();
    const fZone=document.createElement("div");
    fZone.className="fZone";
    aDiv.innerHTML ="";
    aDiv.appendChild(fZone);
    const file=JSON.parse(json);
    const fiName=document.createElement("h1");
    const fiData=document.createElement("p");
	const mime=document.createElement("p");
	const somm=document.createElement("p");

    fiName.innerText=file.nome;
    fiData.innerText="data creazione : "+file.data;
    mime.innerText="mime : "+file.mime;
    somm.innerText="sommario : "+file.sum;
    const funnyLine=document.createElement("div");
    funnyLine.id="funnyLine";
    fZone.appendChild(fiName)
    fZone.appendChild(funnyLine)
    fZone.appendChild(mime)
    fZone.appendChild(fiData)
    fZone.appendChild(somm)

}

function jsonToDocumenti(json,subId){
	if(json.length==0)
    	return;
    const aDiv = getAppDiv();
    aDiv.innerHTML ="";	
    const sub=JSON.parse(json);
	const files=sub["files"];
    const home=document.createElement("h1");
    home.innerText=sub["subName"];
    aDiv.appendChild(home);
	const fiCon=document.createElement("div");
	fiCon.className="carMan";
	aDiv.appendChild(fiCon);
	if(files.length==0){
		home.innerText+="  (vuota)"
	}
	files.forEach(fi=>{
		let fili=document.createElement("div")
		fili.draggable=true;
		fili.ondragstart=(drag)=>{addDraggable(drag, fi.fileId, fi.nome,subId)}
		fiCon.appendChild(fili);
		fili.className="fileCon";
		let parF=document.createElement("p")
		let accA=document.createElement("a")
		//let spst=document.createElement("a")
		accA.className="fileButton";
		//spst.className="fileButton";
		parF.innerText=fi.nome;
		accA.innerText="accedi";
		//spst.innerText="sposta";
		accA.onclick=()=>{showFile(fi.fileId);}
		//spst.onclick=()=>{goSposta(fi.fileId);}
		fili.appendChild(parF);
		fili.appendChild(accA);
		//fili.appendChild(spst);


		
	})	

}
function addDraggable(drag, fid, fname,subId){
	drag.dataTransfer.setData("text", fname+"&"+fid);
	window.localStorage.setItem("origin",subId);
	showHome()
}