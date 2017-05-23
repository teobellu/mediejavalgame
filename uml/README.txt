/***********************************************************************************************/
Date:
+
22/05/2015
+
/***********************************************************************************************/
+
UML Iniziale
+
@Bellusci Matteo 
+
@Bartolini Jacopo
+
AVVERTENZE
+
Il presente pdf comprende 2 diversi ma analoghi diagrammi UML iniziali.
+
@Diagramma 1
+
Il primo è ad alto livello, è presente principalmente l’aspetto logico del progetto da noi pensato, 
senza entrare troppo nel dettaglio, abbiamo preferito dare più importanza a pattern architetturali, 
design pattern e organizzazione delle principali classi/package.
+
@Diagramma 2
+
Il secondo è in appendice, comprende anche classi e collegamenti non previsti per l’UML iniziale ad 
alto livello inizialmente concepito, pertanto, essendo solamente in aggiunta, non abbiamo curato 
l’organizzazione. Sono presenti più nel dettaglio alcuni dei parametri e metodi pensati per le principali 
classi, nonché la concreta applicazione di alcuni dei design pattern utilizzati per il progetto. 
È stato messo in appendice per consultazione, qualora sia necessario approfondire il primo diagramma 
UML
+
Nel branch master sono presenti già alcune classi.
+
/***********************************************************************************************/
+
Altre note:
+
(Se dovesse servire) Per maggior chiarezza si evidenziano alcuni dei pattern finora utilizzati,
+
@Pattern Model view controller
+
Utilizzato del definire l’architettura
+
@Design Pattern State
+
Organizzazione degli stati del gioco (flusso di gioco)
+
@Design Patter Strategy
+
Utilizzato ad esempio per la gestione degli effetti
+
@Design Patter Factory Method
+
Utilizzato ad esempio per ritornare un’istanza di un nuovo stato quando si riceve in ingresso 
una stringa (CLI ad esempio)
+
@Overload @Override <@Design Pattern Strategy>
+
Utilizzato per determinare quando un effetto deve essere attivato (alternativa a 
+
@Design Pattern Observer + @Design Pattern Visitor) 
+
@Altre note: UML software: draw.io objectAid rispettivamente per il principale e per il secondo
+
@Altre note: (Software) Gestione UserConfig, impostazioni gioco: Java™ Preferences API:
+
@Link:
http://docs.oracle.com/javase/7/docs/technotes/guides/preferences/
+
/***********************************************************************************************/
.