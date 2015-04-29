# Diskrétna simulácia - Semestrálna práca č.3

*Vytvorené ako semestrálna práca na predmet Diskrétna Simulácia. Autor: Martin Olešnaník, akademický rok 2014/2015. Autor sa zrieka zodpovednosti za použitie akejkoľvek časti tejto semestrálnej práce ako základ práce niekoho iného.*

# Zadanie

Zadaním tejto semestrálnej práce bolo vytvoriť simulačný program pre zvoz ľudí na hokejový
štadión pred zápasom a na základe tohto programu vytvoriť simulačnú štúdiu, na základe ktorej odporúčime stratégiu zvozu dopravcovi. Špecifikácia zadania sa nachádza v priloženom dokumente ``zadanie.pdf``.

# Požadovaný výstup

Dopravcovi máme poskytnúť:

*	Voľbu stratégie, a to, či jeho vozidlá (autobusy) majú na zastávkach po nastúpení všetkých ľudí (pokiaľ ešte ostáva miesto) čakať na ďaľších prichádzajúcich, alebo okamžite odchádzajú.

*	Počty jednotlivých autobusov prevádzkovaných na jednotlivých linkách.

# Vypracovanie

## Agentový diagram

Nakoľko sme používali agentovo orientovanú simuláciu, pre jednoduchosť zostavovania programu sme vytvorili agentový diagram.

![Agent diagram]
(https://github.com/nixone/ds-sem/raw/master/img/agent_diagram.png)

*	**Model Agent** - Agent zodpovedný za sprostredkovávanie komunikácie medzi ostatnými agentami
*	**Surrounding Agent** - Agent okolia zodpovedný za príchody ľudí na zastávky
*	**Movement Agent** - Agent zodpovedný za výpravu a pohyb vozidiel medzi jednotlivými zastávkami
*	**Boarding Agent** - Agent zodpovedný za správu nástupu ľudí do vozidiel
*	**Exiting Agent** - Agent zodpovedný za správu výstupu ľudí z vozidiel

## Animátor

Súčasťou zostaveného programu je takisto animátor, pomocou ktorého môže zákazník podrobne sledovať priebeh celej simulácie.

![Animator]
(https://github.com/nixone/ds-sem/raw/master/img/animator.png)

# Štúdia

## Rozhodnutia a závery

Pri vypracovaní neboli poskytnuté presné špecifikácie niektorých častí systému, a teda sme mali postupovať pri vypracovávaní logicky, učiniť vlastné ale podložené rozhodnutia.

### Iba autobus č. 2!

Jedným z takýchto rozhodnutí je **ignorácia prvého typu autobusu**. Je úplne zbytočné, aby sme
čo i len zvažovali použitie autobusu č. 1 na zvoz ľudí. Nakoľko je možné na zastávke obsluhovať súčasne viacero autobusov, a všetky autobusy majú rovnakú rýchlosť, nastupovanie a vystupovanie, môžeme použitie dvoch autobusov č. 2 porovnať s jedným autobusom č. 1. V takomto prípade prídeme k záveru, že v každom prípade bude pre nás výhodnejšie využiť služby dvoch autobusov č. 2 ako jedného č. 1. Aj cenovo je takéto riešenie priateľnejšie. Je teda zrejmé, že autobus č. 1 nemusíme v riešení ani zvažovať.

### Kumulatívna predpoveď!

Ďaľším je **časové oneskorenie jednotlivých autobusov na danej linke**. Nakoľko máme veľmi podrobné informácie o príchodoch ľudí na zastávky, je vcelku jednoduché zostaviť tzv. *kumulatívnu predpoveď počtu ľudí na zastávke* pre čas, ktorý ešte nenastal. Toto je funkcia pre danú linku, ktorá nám pre určitý čas vráti predpoveď počtu ľudí, ktorí sa tam v tomto čase budú nachádzať, ak ich nikto nevyzdvihne. Nakoľko presne vieme, aký čas nám trva dostaviť sa na zastávku, a koľko tam v tom čase bude ľudí, je veľmi jednoduché následne zostaviť sekvenciu výprav autobusov tak, aby sme maximalizovali využitie autobusov ale minimalizovali čakanie ľudí na zastávkach.

### Kedy vypraviť mikrobusy súkromného dopravcu?

Výprava mikrobusov dáva zmysel, ak sa vyskytuje časový priestor na zastávkach, počas ktorého nie sú pokryté normálnou dopravou, a musí sa čakať na "druhú otočku" autobusov. Počas normálneho vypravovania autobusov sú tieto vypravované tak, aby sa snažili pokryť potreby všetkých potencionálnych cestujúcich a preto nenehávajú priestor na vypravenie mikrobusov.

Mikrobusy teda vypravujeme po skončení "prvej vlny" vypravených autobusov, a to tak, aby boli všetci cestujúci, ktorým sa mikrobusy ponúknu, ochotní nimi cestovať.

## Experimenty

Experimentami s našim simulačným modelom sme dospeli k dátam, na základe ktorých môžeme jasne rozhodnúť o stanovenej stratégii oboch dopravcov a takisto aj o konkrétnom spôsobe výpravy vozidiel pre zvýšenie úžitku a zachovania spokojnosti cestujúcich.

*Časové značky sú udávané od momentu 1h 13 minút pred začiatkom zápasu, nakoľko tento moment značí začiatok prichádzania cestujúcich na zastávky.* 

*Všetky dáta boli získané po vykonaní 1000 replikácií simulácie.*

### Stratégia bez čakania

Pri stratégii bez čakania sa autobus okamžite po naplnení pasažiermi vypraví na cestu na ďaľšiu zastávku.

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **16** | **7** | **7**
 *Výprava* | 3.3m +~3m | 17m +~5.5m | 11.8m +~5m
 **Mikrobus x** | **2** | **1** | **10**
 *Výprava* | 59m +0.5m | 62m +0.5m | 53m +0.5m

 Varianta | % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov | Zisk súkr. dopravcu
 --- | --- | --- | --- | ---
 Bez súkr. | 7.51% | 6m 30s | 98.54% | -
 So súkr. | 5.09% | 5m 54s | 98.18% | 3117 Kč

Náklady takto vypravených autobusov: **193 500 000 Kč**.
 
### Stratégia s čakaním 1.5 minúty

Pri stratégii s čakaním autobus po vyzdvihnutí všetkých čakajúcich (v prípade, že má miesto) čaká ešte na ďaľších prichádzajúcich 1.5 minúty. Až po tomto momente (alebo po naplnení kapacity) zo zastávky odchádza.

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **18** | **10** | **10**
 *Výprava* | 0m +~0.3-3.5m | 10m +~3-6m | 6.3m +~4-7m
 **Mikrobus x** | **13** | - | -
 *Výprava* | 58m +1m | - | -

 Varianta | % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov | Zisk súkr. dopravcu
 --- | --- | --- | --- | ---
 Bez súkr. | 7.73% | 3m 40s | 97.96% | -
 So súkr. | 5.37% | 3m 23s | 98.02% | 3119 Kč

Náklady takto vypravených autobusov: **245 100 000 Kč**.

## Pozorovania

1.	**Čakanie je výhodné pre cestujúceho, nevýhodné pre dopravcu.** Ako môžeme vidieť, čakanie na zastávke radikálne redukuje čas, ktorý cestujúci musí stráviť čakaním na vozidlo. Na druhej strane je potrebné vypraviť väčšie množstvo autobusov, a preto je pre dopravcu táto varianta neodporúčaná.
2. **Dopravca by dokázal využil viac mikrobusov.** Pri simulovaní je jasne vidieť, že v prípade, ak by dopravca mal dostupných viac mikrobusov, priestor na ich zúžitkovanie by stále existoval. Maximálny zárobok dopravcu pri zvoze (mikrobusy "to nestihnú otočiť" od času svojej výpravy) je 3120 Kč, a teda v oboch prípadoch sa zisk súkromného dopravcu pohybuje až takmer pri hrane jeho maximálnej možnej hodnoty.

## Správnosť riešenia

Je zrejmé, že čím plnšie sú autobusy, ktoré prichádzajú ku štadiónu, tým efektívnejšie a výhodnejšie je riešenie. Na vyhodnotenie, ako plné sú autobusy používame nami vytvorenú štatistiku. Jej hodnota sa pri všetkých prípadoch pohybovala na hodnote vyššej ako 97%. Takúto hodnotu pre zaplnenie autobusov môžeme intuitívne pokladať za veľmi dobrú, až optimistickú, nakoľko v reálnom svete sa s takto využitými dopravnými prostredkami nestretávame.

**Nakoľko dokážeme "využívať kapacitu autobusov na 97%" a zároveň akékoľvek zníženie počtu autobusov na ktorejkoľvek linke by porušilo podmienky zvozu cestujúcich, považujeme naše riešenie za vyhovujúce.**

## Vyhodnotenie a záver

Na základe zistených pozorovaní a simulácie môžeme dopravcovi jasne odporučiť **zvoliť stratégiu zvozu cestujúcich bez čakania s použitím 30 autobusov č. 2**.

Súkromnému dopravcovi môžeme odporučiť pri tejto stratégii rozdeliť jeho **13 mikrobusov pomerom 2:1:10 na linky A:B:C**.
