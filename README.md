# SymfoniaFk
Pomocnicze raporty do systemu Sage Symfonia Kadry i Płace (R)

# Raporty diagnostyczne

Raporty diagnostyczne przeznaczone są dla zaawansowanych
wdrożeniowców Symfonii, wskazane umiejętności programistyczne.
Konfiguracja tych raportów często polega na edycji kodu.

## Eksport raportów

Pozwala zapisać wszystkie raporty oraz formuły
programu do plików tekstowych.

W zamieszczonej wersji raport otwiera bazę 41basprg_0.dat.
To dlatego, że ja zawsze tuż po instalacji programu tworzę
kopię bazy raportów, tzw. dziewiczą. I z tej bazy korzysta
eksport raportów. Można oczywiście zmienić sobie na
41basprg.dat.

Program Kadry i Płace nie ma w ogóle raportów lokalnych.
Raporty są przechowywane zawsze w katalogu programu i są
wspólne dla wszystkich firm. W ERP jest oczywiście odwrotnie.

Formuły eksportowane są do podkatalogu `formuly`,
a pobierane są domyślnie z katalogu wzorcowego, `bazy.2`.
Trzeba by było podłożyć `KatalogFirmy()`, żeby pobrał
formuły z aktualnej bazy (na Symfonii).

Działa również dla ERP.

