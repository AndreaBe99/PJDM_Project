# PJDM_Project
Progetto per il corso di Programmazione Java per Dispositivi Mobili, dell'Università di Tor Vergata.

Il progetto d’esame si basa sullo sviluppo di un’applicazione che permette all’utente di calcolare il proprio tasso alcolemico attraverso l’inserimento dei suoi dati, come ad esempio l’età o il peso, e degli alcolici da lui consumati, in modo tale da avere a disposizione un risultato approssimativo del tempo necessario a smaltire l’alcol per rientrare nei limiti della legge, e quindi poter guidare. L’applicazione verrà struttura nel modo seguente:

## LATO CLIENT
L’applicazione prevede l’accesso attraverso uno Username e una Password se si è già registrati al servizio, altrimenti lo si può fare attraverso l’apposita schermata. Una volta effettuato il login l’applicazione si presenta attraverso una singola Activity contenente tre schermate principali che possono essere navigate attraverso una BottomNavigation Bar (Fragment).
Il primo Fragment consiste nell’etilometro in cui si sceglie un profilo tra quelli già inseriti, se si è a stomaco vuoto o pieno, e i drink bevuti, i quali vengono mostrati attraverso una lista. Una volta completato l’inserimento si preme un Fab il quale apre un Dialog che mostra il risultato del test, ovvero il tasso alcolemico e il tempo che ci vuole per smaltirlo. Nel caso in cui si è al disopra dei limiti di legge saranno disponibili due pulsanti. Il primo consente di settare una Notifica che apparirà una volta passato il tempo necessario a smaltire l’alcol. Il secondo consente di ottenere la posizione dell’utente, attraverso l’utilizzo del GPS, e mandare le coordinate con un link di Google Maps, utilizzando una qualsiasi app di messaggistica, ad un proprio contatto. Tale Acquisizione contenente il tasso alcolemico e la data in cui è stata effettuata, e viene registrata dal server attraverso l’apposita Servlet.
Il secondo Fragment mostra la lista dei Profili inseriti, ognuno dei quali può essere cancellato attraverso uno swipe. Inoltre, è previsto anche un Fab che se premuto apre una schermata per la registrazione di nuovi profili. Infine, se si clicca su uno di essi si aprirà una schermata contenente le informazioni del profilo selezionato compreso lo storico delle sue acquisizioni. Da questa schermata inoltre è possibile modificare alcuni dati come il peso.
L’ultimo Fragment prevede una schermata riguardante le informazioni dell’app, come ad esempio il numero e il nome della versione, e due collegamenti rispettivamente al sito dell’ACI, riguardante le sanzioni relative alla guida nello stato di ebrezza, e al sito del Ministero della Salute sulla pagina relativa alla sensibilizzazione dell’utilizzo di alcolici.

## LATO SERVER
La prima Servlet serve per gestire il Login e la Registrazione al servizio attraverso uno Username univoco e una Password.
Ogni utente, una volta effettuato l’accesso all’applicazione può creare più profili ognuno dei quali con diversi dati anagrafici (Nome, data di nascita, Peso, Altezza). La seconda Servlet si occuperà di gestire tali profili associati a un singolo utente (creazione, visualizzazione, cancellazione).
Ogni profilo creato dall’utente può effettuare un’“Acquisizione”, ciò consiste nel calcolare il tasso alcolemico del profilo selezionato inserendo gli alcolici consumati. L’ultima Servlet si occuperà dell’inserimento e della visualizzazione delle Acquisizioni per ciascun profilo.

## GUIDA
Modalità di realizzazione del Progetto:
1) realizzazione di un documento di specifiche da presentare prima della realizzazione Presentazione proposta il 8 Giugno durante orario lezione
2) realizzazione del progetto e di schemi SW che ne spieghino l'architettura (preferibilmente UML)
3) realizzazione di una presentazione che illustri i seguenti aspetti del progetto:
  a) motivazioni del sistema (a che serve, che problema risolve)
  b) requisiti tecnici del sistema (cosa deve fare, che interazioni sono possibili)
  c) soluzioni software e pattern di programmazione utilizzati
  d) test di funzionamento del sistema


Requisiti del progetto:
- Il progetto deve presentare una componente Server realizzata con Servlet e Tomcat ed una componente Client realizzata in Android
- La comunicazione tra Client e Server è realizzata in HTTP e JSON a deve prevedere almeno due metodi: una GET per prendere dati dati dal server, ed una POST per inviare dei dati dal server.

Elementi architetturali lato client che saranno valutati:
- Uso di Handler, Asynctask, Service, Broadcast Receiver
- Uso di Fragment e Navigation Framework
- Uso del Database locale e delle Preferences Android
- Interazioni con altre app (share, edit, view, etc.)
- Interazioni con il sistema (localizzazione, bluetooth, sensori, etc.)

Elementi architetturali lato server che saranno valutati:
- Inizializzazione del server Tomcat e delle servlet
- Integrazione con Database
- Uso del Pattern DAO

Verrà valutato inoltre l'uso di Pattern di programmazione ad oggetti, come ad esempio:
- Sigleton
- Factory
- Decorator
