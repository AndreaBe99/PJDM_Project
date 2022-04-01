# PJDM_Project
Progetto per il corso di Programmazione Java per Dispositivi Mobili, dell'Università di Tor Vergata.

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
