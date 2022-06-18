# tiw-filesystem-webApp


## TOC
* [INFO](#info)
* [istruzioni](#istruzioni)
* [pure html](#versione-pure-html)
* [versione JS](#versione-con-javascript)

## TODO

- [ ] review java web slide
- [ ] server
- [ ] Db mySql
- [ ] servlets
- [ ] Js client ui
- [ ] server for js version
- [ ] Class uml for both servers
- [ ] Sequence uml

-----------

## INFO

Tecnologie Informatiche per il Web - AA. 2021-22  
Progetti d’esame: versione pure HTML e RIA  
### Istruzioni  
La presentazione effettuata online tramite MS Teams è individuale e richiede:  
* L’invio almeno tre giorni prima della data di presentazione della documentazione del
progetto
* La redazione della documentazione secondo gli esempi di esercizi mostrati durante
le lezioni e le esercitazioni e già disponibili nel sito WeBeep del corso
* L’effettuazione di una dimostrazione del progetto, sia nella versione HTML puro sia
nella versione con JavaScript
* La discussione del codice con eventuali domande sulle motivazioni della
progettazione e della codifica  

La dimostrazione richiede la preparazione di una base di dati con contenuto sufficiente alla
dimostrazione di TUTTI i possibili scenari di uso dell’applicazione.  
Inoltre si richiede di sapere utilizzare le funzioni di zoom dell’ambiente di sviluppo per
consentire la visualizzazione ottimale del codice da remoto.  
### Commenti generali:
* Le versioni pure HTML e JavaScript sono da realizzarsi come applicazioni Web
distinte.
* Eventuali funzioni non richieste di gestione dei dati (es, modifica o cancellazione)
possono essere realizzate se comode per il testing ma sono opzionali e non valutate.
* Il controllo di validità dei parametri deve essere fatto sia lato client sia a lato server.
Non si deve consentire a un utente di fare operazioni che il suo ruolo non permette.
Non si deve consentire a un utente malintenzionato di violare la sicurezza
dell’applicazione mediante l’invio di valori scorretti dei parametri.


## RICHIESTA

### Versione pure HTML
Un’applicazione web consente la gestione di cartelle, sottocartelle e documenti online.  
L’applicazione supporta registrazione e login di utenti mediante una pagina pubblica con opportune form. La registrazione controlla l’unicità dello username. Una cartella ha un proprietario, un nome e una data di creazione e può contenere (solo) sottocartelle. Una sottocartella può contenere (solo) dei documenti. Un documento ha un proprietario, nome, una data di creazione, un sommario e un tipo.   
Quando l’utente accede all’applicazione appare una HOME PAGE che contiene un albero delle proprie cartelle e delle sottocartelle.

>* Cartella 1
>>  * Cartella 11
>>  * Cartella 12
>>  * Cartella 13
>* Cartella 2
>>  * Cartella 21
>* Cartella 3
>>  * Cartella 31  
  
**Albero delle cartelle e sottocartelle nella
HOME PAGE**
         
>Cartella 11
>* Documento 1 >accedi >sposta  
>* Documento 2 >accedi >sposta
>* Documento 3 >accedi >sposta  
   
**Contenuto della pagina DOCUMENTI di
una sottocartella**  

Nell’**HOME PAGE** l’utente può selezionare una sottocartella e accedere a una pagina
DOCUMENTI che mostra l’elenco dei documenti di una sottocartella.  
Ogni documento in elenco ha due link: accedi e sposta. Quando l’utente seleziona il link accedi, appare una pagina **DOCUMENTO** che mostra tutti i dati del documento selezionato.  
Quando l’utente seleziona il link sposta, appare la **HOME PAGE** con l’albero delle cartelle e delle sottocartelle;
in questo caso la pagina mostra il messaggio “Stai spostando il documento X dalla sottocartella Y. Scegli la sottocartella di destinazione”, la sottocartella a cui appartiene il documento da spostare ***NON*** è selezionabile e il suo nome è evidenziato (per esempio con un colore diverso).   
Quando l’utente seleziona la sottocartella di destinazione, il documento è spostato dalla sottocartella di origine a quella di destinazione e appare la pagina **DOCUMENTI** che mostra il contenuto aggiornato della sottocartella di destinazione.  
Ogni pagina, tranne la **HOME PAGE**, contiene un collegamento per tornare alla pagina precedente.    
L’applicazione consente il logout dell’utente.  
Una pagina GESTIONE CONTENUTI raggiungibile dalla **HOME PAGE** permette all’utente di creare una cartella, una sottocartella di una cartella esistente e un documento all’interno di una sottocartella.

-----------

### Versione con JavaScript
Si realizzi un’applicazione client server web che modifica le specifiche precedenti come segue:
* La registrazione controlla la validità sintattica dell’indirizzo di email e l’uguaglianza tra
i campi “password” e “ripeti password”, anche a lato client.
* Dopo il login, l’intera applicazione è realizzata con un’unica pagina
* Ogni interazione dell’utente è gestita senza ricaricare completamente la pagina, ma
produce l’invocazione asincrona del server e l’eventuale modifica del contenuto da
aggiornare a seguito dell’evento.
* Errori a lato server devono essere segnalati mediante un messaggio di allerta
all’interno della pagina.
* La funzione di spostamento di un documento è realizzata mediante drag and drop.
* La funzione di creazione di una sottocartella è realizzata nella pagina HOME mediante
un bottone AGGIUNGI SOTTOCARTELLA posto di fianco ad ogni cartella. La
pressione del bottone fa apparire un campo di input per l’inserimento del nome della
sottocartella.
* La funzione di creazione di un documento è realizzata nella pagina HOME mediante
un bottone AGGIUNGI DOCUMENTO posto di fianco ad ogni sottocartella. La
pressione del bottone fa apparire una form di input per l’inserimento dei dati del
documento.
* Si aggiunge una cartella denominata “cestino”. Il drag & drop di un documento o di una
cartella nel cestino comporta la cancellazione. Prima di inviare il comando di
cancellazione al server l’utente vede una finestra modale di conferma e può decidere
se annullare l’operazione o procedere.
