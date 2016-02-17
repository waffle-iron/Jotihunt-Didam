# Jotihunt APP Voor Scoutinggroep Didam #

Ieder jaar wordt in Gelderland een spel gespeeld door vrijwel alle scoutinggroepen in Gelderland. Dit speel is de zogenaamde ```Jotihunt```

## Wat is de Jotihunt? ##
De ```Jotihunt``` is een spel waarbij Jagers jagen op Vossen.
Hierbij spelen verschillende teams als Jager en andere teams als Vossen.

Het speelgebied is heel de provincie Gelderland, en deze is opgedeeld in vijf verschillende deelgebieden.
Deze deelgebieden zijn:

* Alpha
* Bravo
* Charlie
* Delta
* Echo
* Foxtrot

In ieder deelgebied zijn er teams met Vossen en Jagers. Deze mogen echter niet uit hun deelgebied komen.
Het spel speelt zich dus af binnen een deelgebied.

### De regels ###
De Vossen moeten alle scouting gebouwen afgaan. Deze moeten dat echter via een bepaalde route doen, waardoor ze niet een scouting gebouw over kunnen slaan.
Het is de bedoeling dat een vos in een bepaalde afstand van een scouting gebouw een sticker plakt. Als dit gebeurt is, is deze scoutinggroep ```gehunt```.
Als een groep is ```gehunt```, betekend dit dat de desbetreffende groep 1 uur de tijd heeft om de sticker te vinden, voordat de vossen 


Vossen mogen echter alleen ```gehunt``` worden, als hun status op actief staat. Op het moment dat de status van de vos op inactief staat, mogen ze niet gehunt worden.
Staat de status van de vos echter op oranje, dan betekend het dat de vos wel gehunt mag worden, maar binnen een half uur op inactief komt te staan. Opschieten dus!

### Hoe kan je winnen? ###
Je kan punten verdienen door vossen te jagen, en opdrachten op te lossen.
**Een hunt levert 2 punten op.**
Het kan echter ook zo zijn dat het *happy hour* is, als dit het geval is levert het 3 punten op.

## Wat moet er mogelijk zijn in de app? ##
### Login Scherm###
In het begin van de app moet het mogelijk te zijn om in te loggen. Dit wordt ook gebruikt voor de verificatie van de ```API``` calls.
### Hoofd Scherm ###
Op het moment dat er ingelogd is, moet er een activity komen te staan met Google Maps. 
Deze moet gecentreerd zijn op Gelderland, met alle deelgebieden ingeladen.

Vervolgens moet hij een lijst met Vossen en Jagers ophalen van de API Server, met een standaard intervaltijd van 30 seconden. Tijdens deze update wordt niet alleen de data van andere jagers van de ```API Server``` opgehaald, maar ook de huidige locatie van de jager verstuurd. Dit moet echter aan of uit te zetten zijn bij de **instellingen**. De intervaltijd moet ook aan te passen zijn in **instellingen**.

Vossen moeten worden weergegeven met een vos icoontje, en jagers met een ander icoontje.
Op het moment dat er op een vos wordt gedrukt, moet er de status in komen te staan en het tijdstip van deze locatie.

Op het moment dat er op een jager wordt gedrukt, moet er een gebruikersnaam van deze jager komen te staan.
### Instellingen ###
In de instellingen moet er een optie komen te staan die de update tijd van de coördinaten ophalen bepaald.
Ook moet het mogelijk zijn om het versturen van de locatie als jager aan of uit te zetten.

## Extra's ##
Een implementatie voor score zou leuk zijn. Deze kan worden opgehaald door middel van de ```Jotihunt API```.


Ook zou het handig zijn om een tablad voor aantekeningen te maken. Deze moeten uiteraard ook gesynchroniseerd zijn met de andere gebruikers van de app.