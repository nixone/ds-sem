# Diskrétna simulácia - Semestrálna práca č.2

*Vytvorené ako semestrálna práca na predmet Diskrétna Simulácia. Autor: Martin Olešnaník, akademický rok 2014/2015. Autor sa zrieka zodpovednosti za použitie akejkoľvek časti tejto semestrálnej práce ako základ práce niekoho iného.*

## Úvod

Cieľom semestrálnej práce č.2 bolo určiť kapacitu bezpečnostnej kontroly letiskového terminálu, teda určiť počet cestujúcich, ktorí sú schopní prejsť bezpečnostnou kontrolou v časovom intervale 24 hodín za nepretržitej prevádzky. Od užívateľa (zákazníka) sme dostali definované určité parametre simulovaného systému:

*	Dĺžky dopravníkov pre odkladanie a vyzdvihávanie prepraviek na odloženie batožiny
*	Správanie sa prichádzajúcich zákazníkov, priebehy bezpečnostných kontrol a špecifikáciu
	priebehu bezpečnostnej kontroly
	
Zákazník od nás požaduje výslednú kapacitu cestujúcich, ktorá musí vyhovovať určitým podmienkam:

*	Priemerný čas strávený cestujúcim na bezpečnostnej kontrole neprekročí 8 minút
*	Priemerný počet čakajúcich v rade pred detektormi neprekročí 40 osôb

## Vypracovanie

Pre vypracovanie štúdie som zvolil vývoj vlastného softvéru pre simuláciu daného systému. Nakoľko podobnú požiadavku môžem od klientov dostať aj v budúcnosti, zvolil som rozdeliť prácu na naprogramovanie **nezávislého simulačného jadra** a následne jeho **implementácie na konkrétny požadovaný systém**.

### Simulačné jadro

Simulačné jadro som navrhol tak, aby spĺňalo princípy udalostnej simulácie, v ktorej je pre potreby sledovaných parametrov systému zanedbateľný čas na úrovni vykonávania konkrétnych udalostí. Toto jadro je objektovým programovaním využívané ako základ konkrétnej implementácie špecifického modelu.

Simulačné jadro podporuje niekoľko spôsobov behu simulácie:

*	Beh simulácie **bez sledovania**, v takomto móde vidí používateľ len základné informácie o priebehu simulácie pre efektívne vykonávanie. Týmto spôsobom sa obvykle dostaneme ku výsledkom najrýchlejšie ale nevidíme jej priebeh.

*	Beh simulácie **po krokoch**, v ktorom je simulácia pozastavená a krokovanie simulácie na nasledujúce udalosti je vykonávane na pokyn užívateľa.

*	Beh simulácie **v čase**, kedy je priebeh spomaľovaný, aby imitoval priebeh reálneho času v simulácii, samozrejme zrýchlenie času je takýmto spôsobom možné. 

Simulačné jadro zabezpečuje plánovanie udalostí, vykonávanie udalostí a správu priebehu simulácie a replikácií.

### Konkrétny model

Pre potreby konkrétneho modelu sme potrebovali navrhnúť udalosti, ktorými bude simulácia riadená. Udalosti zobrazujem na nasledujúcom diagrame, ktorý zobrazuje aj ich možné následnosti a fronty, ktoré sa v simulácii vyskytujú:

![Diagram udalostí]
(https://github.com/nixone/ds-sem/blob/master/img/EventDiagram.png)

*Každý front má dve udalosti, ktoré sú vyvolané pri pridaní (resp. odobratí) prvkov do (resp. z) frontu. Tieto udalosti zvyčajne nejakým spôsobom na zmenu frontu zareagujú (napríklad naplánujú ďaľšiu udalosť). Tieto udalosti sú zaradené do simulačného jadra úplne rovnakým spôsobom ako akékoľvek iné udalosti*

*	**Príchod cestujúceho** plánuje príchody cestujúcich počas simulácie. Po príchode zaradí cestujúceho do frontu ľudí s batožinou (vyvolá udalosť **+ Fronta ľudí s batožinou**, *analogicky zaradenia /resp. vymazania/ prvkov z frontu budú vyvolávať ekvivalentné udalosti)
*	**Oskenovanie batožiny** indikuje koniec skenovania danej batožiny. Batožinu zaradí medzi prepravky po a potencionálne začne ďaľšie skenovanie batožiny a poskytne priestor na odovzdanie ďaľšej batožiny.
*	**Prechod detektorom** indikuje koniec procesu, kedy cestujúci prechádza detektorom kovov. Po tejto udalosti je cestujúci pustený na prevzatie (resp. čakanie) na svoju batožinu zo skenera, rovno vyradený zo systému, alebo posunutý na vykonanie osobnej kontroly.
*	**Koniec osobnej kontroly** indikuje koniec osobnej kontroly daného cestujúceho, na ktorú bol vyvolaný po prechode detektorom kovov. Po tejto udalosti cestujúci čaká na svoju prepravku alebo je vyradený zo systému.

### Beh simulačného modelu

Medzi replikáciami nie je v simulačnom modeli inicializovaný stav modelu nanovo. Takýmto spôsobom sme schopní simulovať kontinuálny chod po sebe idúceho veľkého množstva replikácií bez zbytočnej potreby zahrievať celý model pred každou replikáciou na novo. Zahrievanie prvej replikácie prebieha ignoráciou a vykonaním *"nultej"* replikácie.

## Užívateľské rozhranie

![Užívateľské rozhranie]
(https://github.com/nixone/ds-sem/blob/master/img/ui.png)