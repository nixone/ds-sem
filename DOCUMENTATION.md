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

*Po kliknutí na jednotlivé zastávky je možné zobraziť detail nastupovania ľudí do konkrétnych autobusov na zastávkach.*

## Vlastné štatistiky

Jednou z úloh semestrálnej práce bolo navrhnúť a vyhodnotiť vlastné štatistiky. My sme navrhli tieto:

*	Plnosť autobusu - Popisuje percentuálne zaplnenie autobusu pri príchode na štadión. Táto štatistika sa ukázala počas experimentovania ako vhodná na ich intuitívne usmerňovanie.
*	Podiel zmeškania na konkrétnych linkách - Pre každú linku popisuje percenuálny podiel ľudí, ktorí z tej linky nestihli zápas. Numerické výsledky tejto štatistiky sú iba odhadové a popisné, nakoľko sa linky križujú a nie je jasné, ku ktorej linke započítať cestujúcich, ktorí prídu na zastávky, kde sa križuje viacero liniek. Tieto štatistiky sa však ukázali ako najprospešnejšie, nakoľko jasne udali, na ktorej linke je potrebné experimentovať s výpravou autobusov, nakoľko bolo jasné, ktorá linka zhoršuje celkové štatistiky.

# Simulačná štúdia

Pri vypracovaní neboli poskytnuté presné špecifikácie niektorých častí systému, a teda sme mali postupovať pri vypracovávaní logicky, učiniť vlastné ale podložené rozhodnutia.

## 1. Je výhodné používať iba autobus č. 2

Jedným z takýchto rozhodnutí je **ignorácia prvého typu autobusu**. Je úplne zbytočné, aby sme
čo i len zvažovali použitie autobusu č. 1 na zvoz ľudí. Nakoľko je možné na zastávke obsluhovať súčasne viacero autobusov, a všetky autobusy majú rovnakú rýchlosť, nastupovanie a vystupovanie, môžeme použitie dvoch autobusov č. 2 porovnať s jedným autobusom č. 1. V takomto prípade prídeme k záveru, že v každom prípade bude pre nás výhodnejšie využiť služby dvoch autobusov č. 2 ako jedného č. 1. Aj cenovo je takéto riešenie priateľnejšie. Je teda zrejmé, že autobus č. 1 nemusíme v riešení ani zvažovať.

## 2. Skúška kumulatívnej predpovede

Veľmi dôležitým rozhodnutím pri vypravovaní autobusov je **časové oneskorenie jednotlivých autobusov na danej linke**. 

*Úvaha:* Nakoľko máme veľmi podrobné informácie o príchodoch ľudí na zastávky, je vcelku jednoduché zostaviť tzv. *kumulatívnu predpoveď počtu ľudí na zastávke* pre čas, ktorý ešte nenastal. Toto je funkcia pre danú linku, ktorá nám pre určitý čas vráti predpoveď počtu ľudí, ktorí sa tam v tomto čase budú nachádzať, ak ich nikto nevyzdvihne. Nakoľko presne vieme, aký čas nám trva dostaviť sa na zastávku, a koľko tam v tom čase bude ľudí, je veľmi jednoduché následne zostaviť sekvenciu výprav autobusov tak, aby sme maximalizovali využitie autobusov ale minimalizovali čakanie ľudí na zastávkach.

## 3. Stratégia bez čakania

*Pri všetkých experimentoch boli dáta získané z 250 replikácií simulácie.*

### Experiment bez čakania č. 1

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **15** | **6** | **6**
 *Výprava* | `cumulative:15` | `cumulative:6` | `cumulative:6`

% ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | --- | ---
7.73% | 8m 08s | 97.95%

Riešenie kumulatívnej predpovede takmer okamžite prinieslo dobrý výsledok, ale za použitia príliš veľkého počtu autobusov a vidíme priestor na zlepšenie.

### Experiment bez čakania č. 2

Ako základné riešenie sa snažíme použiť riešenie čo najbližšie tomu navrhnutému z kroku 2., avšak použitím rovnakých rozostupov odchodov autobusov.

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **15** | **6** | **6**
 *Výprava* | `static:3 3 15` | `static:19 5 6` | `static:13 5 6`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | --- | ---
8.88% | 8m 03s | 96.41%

Po tomto experimente sme sa priblížili hranici splniteľnosti modelu za podobnej výpravy autobusov, ako nám bola odporúčaná pomocou kumulatívnej predpovede.

### Experiment bez čakania č. 3

Po experimentovaní zisťujeme, že sa nám oplatí meniť hlavne výpravy linky B, nakoľko akékoľvek úpravy na ostatných linkách nám zhoršujú štatistiky a zároveň vidíme, že podiel nedopravených ľudí na štadión je z linky B najväčší.

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **15** | **6** | **6**
 *Výprava* | `static:3 3 15` | `static:11 3 6` | `static:13 5 6`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | --- | ---
6.44% | 9m 26s | 89.83%

Vidíme, že pri použití rovnakého počtu autobusov na jednotlivých linkách sme zmenou ich výpravy síce zvýšili priemerný čas čakania cestujúcich na autobus, ale ušetrili sme podiel ľudí, ktorí sa na zápas nedostavia.

### Experiment bez čakania č. 4

Keďže akékoľvek experimentovanie s výpravou autobusov už neprinášalo priaznivejšie výsledky, skúšali sme redukovať počet autobusov na jednotlivých linkách. Jediná linka, ktorá nám pri dodržaní požiadaviek takúto redukciu povolila, bola linka A, kde sme boli schopní odobrať jeden autobus.

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **14** | **6** | **6**
 *Výprava* | `static:3 3 14` | `static:11 3 6` | `static:13 5 6`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | ---
7.95% | 9m 58s | 90.49%

Akýmkoľvek ďaľším experimentovaním sme dosahovali horšie výsledky a nesplnili sme požiadavky na kvalitu prevozu cestujúcich, preto **výsledky experimentu č. 4 považujeme za finálne pre stratégiu bez čakania** a cena takéhoto riešenia je stanovená na **167 700 000 Kč**.

## 4. Stratégia s čakaním

*Pri všetkých experimentoch boli dáta získané z 250 replikácií simulácie.*

Pri stratégii s čakaním znova začíname riešením, ktoré nám je odporučené použitím výprav kumulatívnej predpovede.

### Experiment s čakaním č. 1

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **19** | **10** | **10**
 *Výprava* | `cumulative:19` | `cumulative:10` | `cumulative:10`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | ---
7.88% | 3m 25s | 94.69%

Ako vidíme, takéto riešenie spĺňa požadované kritéria, ale používa veľké množstvo autobusov. Preto, takisto ako pri stratégii bez čakania, skúšame experimentovať s výpravou autobusov.

### Experiment s čakaním č. 2

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **19** | **10** | **10**
 *Výprava* | `static:0 3 19` | `static:10 5 10` | `static:7 5 10`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | ---
7.84% | 5m 6s | 95.04%

### Experiment s čakaním č. 3

Ako sme mohli pomocou animátora vidieť, najväčšie problémy s dopravou cestujúcich vznikali na linke B, teda sme s ňou experimentovali, až kým sme neprišli ku lepšiemu riešeniu:

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **19** | **10** | **10**
 *Výprava* | `static:0 3 19` | `static:5.25 6.75 10` | `static:7 5 10`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | ---
5.6% | 5m 8s | 97.32%

### Experiment s čakaním č. 4

Experimentovaním s výpravou linky A sme dospeli ku:

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **18** | **10** | **10**
 *Výprava* | `static:0 2.5 18` | `static:5.25 6.75 10` | `static:7 5 10`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | ---
6.27% | 4m 37s | 95.9%

### Experiment s čakaním č. 5

Experimentovaním s výpravou linky C sme dospeli ku:

 Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
 **Autobus č. 2 x** | **18** | **10** | **8**
 *Výprava* | `static:0 2.5 18` | `static:5.25 6.75 10` | `static:6.5 5.6 8`
 
 % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie | Plnosť autobusov
 --- | --- | ---
7.86% | 5m 22s | 97.59%

Výsledky tohto experimentu považujeme za finálne pre túto stratégiu, nakoľko akýmkoľvek experimentom s výpravou autobusov sme nevedeli nájsť lepšie riešenie, ktoré by akokoľvek ponúkalo možnosť zlepšenia ceny riešenia. **Cena tohto riešenia je 232 200 000 Kč**.

## Výsledky experimentov a stratégie

Dopravnému podniku ponúkame tieto výsledky experimentov:

Stratégia | Použité autobusy č. 2 | % ľudí, ktorí nestihli zápas | Čas čakania na autobus | Cena riešenia
 --- | --- | --- | --- | ---
Bez čakania | 26 | 7.95% | 9m 58s | **167 700 000 Kč**
S čakaním | 36 | 7.86% | 5m 22s | **232 200 000 Kč**

Dopravnému podniku teda ponúkame stratégiu **bez čakania pri použití 26 autobusov** pri cene **167 700 000 Kč**.

## 5. Kedy vypraviť mikrobusy súkromného dopravcu?

*Úvaha:* Výprava mikrobusov dáva zmysel, ak sa vyskytuje časový priestor na zastávkach, počas ktorého nie sú pokryté normálnou dopravou, a musí sa čakať na "druhú otočku" autobusov. Počas normálneho vypravovania autobusov sú tieto vypravované tak, aby sa snažili pokryť potreby všetkých potencionálnych cestujúcich a preto nenehávajú priestor na vypravenie mikrobusov.

Mikrobusy teda vypravujeme po skončení "prvej vlny" vypravených autobusov, a to tak, aby boli všetci cestujúci, ktorým sa mikrobusy ponúknu, ochotní nimi cestovať.

### Experiment č. 1

Linka A | Linka B | Linka C | Zisk
 --- | --- | --- | --- 
`static:53 0.1 13` | - | - | 3120 Kč

Počas experimentovania sme zistili, že vypravovať mikrobusy priskoro nemá zmysel, nakoľko nie sú do nich ochotní cestujúci nastupovať. Naša úvaha sa aspoň podľa našich experimentov javí ako správna. Pri vypravení v časovej bubline medzi prvými vlnami autobusov však nemajú mikrobusy dosť času na druhé kolo zbierania cestujúcich a preto je cena 3120 Kč maximum, ktoré môžu vyzbierať (30 Kč x 13 mikrobusov x 8 ľudí = 3120 Kč).

Výsledkom nášho experimentu je teda rada súkromnému dopravcovi, ktorá bude znieť: **Vypravte všetkých 13 mikrobusov na linku A, takmer po sebe v 53 minúte**.

## 6. Vyhodnotenie zmien štatistík pri výprave súkromného dopravcu

Môžeme vidieť, že pri výprave mikrobusov súkromného dopravcu sa nám mení stav systému vzhľadom na požiadavky, a obecne sú cestovatelia spokojnejší, nakoľko sú dopravení na zápas s väčšou pravdepodobnosťou a takisto čakať na odvoz nemusia priemerne tak dlho.

Varianta | % ľudí, ktorí nestihli zápas | Čas čakania na odvezenie 
 --- | --- | --- 
Bez súkromného dopravcu | 7.95% | 9m 58s
So súkromným dopravcom | 6.58% | 9m 41s

# Záver

### Odporúčanie dopravnému podniku

Na základe simulačnej štúdie bolo dopravnému podniku odporúčené zvoliť stratégiu zvozu cestujúcich bez čakania pri použití 26 autobusov typu č. 2:

Vozidlo | Linka A | Linka B | Linka C
 --- | --- | --- | ---
**Autobus č. 2 x** | **14** | **6** | **6**
*Výprava* | `static:3 3 14` | `static:11 3 6` | `static:13 5 6`
 
Cena tohto riešenia je **167 700 000 Kč**.
 
### Odporúčanie súkromnému dopravcovi
 
Na základe simulačnej štúdie bolo odporúčené **vypraviť všetkých 13 mikrobusov na linku A v čase 53 minút**. Takáto výprava mikrobusov mu zabezpečí **najväčší zisk, vo výške 3120 Kč**.

### Zmena systému pri výprave súkromného dopravcu

Ako sme zo simulačnej štúdie videli, použitie mikrobusov pri zvoze na zápas bolo priaznivé pre cestujúcich, nakoľko väčšie množstvo z nich sa dokázalo dostať na zápas včas a takisto nemuseli čakať tak dlho, ako pri variante bez výpravy mikrobusov.