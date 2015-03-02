# Diskrétna simulácia - Semestrálna práca č. 1 - Monte Carlo

*Vytvorené ako semestrálna práca na predmet Diskrétna Simulácia. Autor: Martin Olešnaník, akademický rok 2014/2015.
Autor sa zrieka zodpovednosti za použitie akejkoľvek časti tejto semestrálnej práce ako základ práce niekoho iného.*

## Hra

Pre potreby semestrálnej práce bola definovaná strelecká hra, ktorá má 6 strelcov,
ktorí strieľajú na ohračných strelcov podľa definovanej stratégie. Našou úlohou
bolo zisťovať a analyzovať výsledok hry pri veľkom množstve opakovaní.

## Cieľ semestrálnej práce

Cieľom tejto semestrálnej práce, pomocou metódy Monte Carlo bolo zistiť pravdepodobnosti,
že po ukončení hry

* sú všetci strelci mŕtvi
* prežije strelec A
* prežije strelec B
* prežije strelec C
* prežije strelec D
* prežije strelec E
* prežije strelec F

za dvoch rozličných predpokladov, ktoré boli použité ako jeden z parametrov simulácie:

* všetci strelci si svoj cieľ vyberajú náhodne
* všetci strelci strieľajú na najlepšieho živého strelca (okrem seba)

## Vypracovanie

Vypracovaním tejto semestrálnej práce je priložený program, ktorý simuluje
použitím metódy Monte Carlo veľký počet (replikácií) hry a vyhodnocuje odhady
pravdepodobností zisťovaných javov. Na základe týchto odhadov môžeme tvoriť naše
závery.

## Užívateľské rozhranie

Po spustení aplikácie je užívateľovi ponúknuté grafické rozhranie na ovladanie
a sledovanie priebehu simulácie. Pomocou grafických prvkov môže meniť vstupné
parametre simulácie.

![Užívateľské rozhranie]
(https://raw.githubusercontent.com/nixone/ds-sem-1/master/screenshots/ui-doc.png)

1. Výber stratégie hráčov
2. Nastavenie počtu replikácií simulačného modelu
3. Nastavenia percenta replikácií, ktoré sa užívateľovi pre celkový efekt neprezentujú
4. Nastavenie percenta najnovších replikácií, ktoré sa v dátach užívateľovi prezentujú počas priebehu
5. Tlačidlo na odštartovanie simulácie
6. Výber konkrétneho sledovaného aspektu simulácie
7. Aktuálny odhad pravdepodobnosti danej udalosti simulačného modelu
8. Grafický priebeh odhadu pravdpeodobnosti danej udalosti simulačného modelu

## Výsledok simulačného modelu

Simulačný model po 100 miliónoch replikácií odhadol nasledovné pravdepodobnosti:

Stratégia | Sledovaný aspekt | Pravdepodobnosť
--- | --- | ---
Náhodný výber | Všetci mŕtvi | 25.38 %
Náhodný výber | Ostal hráč A | 12.89 %
Náhodný výber | Ostal hráč B | 14.72 %
Náhodný výber | Ostal hráč C | 15.71 %
Náhodný výber | Ostal hráč D | 7.29 %
Náhodný výber | Ostal hráč E | 4.95 %
Náhodný výber | Ostal hráč F | 19.05 %
Najlepší strelec | Všetci mŕtvi | 7.15 %
Najlepší strelec | Ostal hráč A | 14.14 %
Najlepší strelec | Ostal hráč B | 9.55 %
Najlepší strelec | Ostal hráč C | 0.07 %
Najlepší strelec | Ostal hráč D | 40.59 %
Najlepší strelec | Ostal hráč E | 28.48 %
Najlepší strelec | Ostal hráč F | 0.02 %

## Záver

Na základe rozdielnych pravdepodobností môžeme vidieť adekvátnosť vplyvu vybranej stratégie
hráčmi na celkový výsledok hry.
