//0,"eksp_rap.sc","_Eksport raportów","Jarek",0,1.0.3,SYSTEM
///////////////////////////////////////
// Eksport raportów
// Autor: Jarek Czekalski
///////////////////////////////////////

int err
string kat_wyj, nazwa_wyj
int plik_wyj, ib
limit 32767

string sub wczytaj_string (string prompt, string war_pocz)
string wynik = war_pocz
Form prompt, 400, 110
  MEdit "", wynik, 80, 10, 200, 24+14
  Button "&OK", 20, 50, 80, 24, 2
  Button "&Anuluj", 300, 50, 80, 24, -1
int nRetV = execform
if nRetV==0 || nRetV==-1 then close : error ""
wczytaj_string = wynik
endsub

buf = firma.wersja
replace ".", ""
buf = Mid(buf,3)
if Len(buf)==2 then buf+="0"
if find regular at "^[0-9]^2-[a-z]" then insert "0" // dla 2015.a

kat_wyj = wczytaj_string ("Podaj katalog wyjœciowy:","s:\\raporty\\std\\kd\\kd"+buf)
buf = kat_wyj
if !find regular "\\$" then kat_wyj += "\\"

int abprg(2)
abprg(1) = Open Katalog()+"41basprg_0.dat" for base "BASPROG"
if !abprg(1) then error "B³¹d przy otwieraniu bazy BASPROG !!!"
abprg(2) = Open Katalog()+"bazy.2\\42fp.dat" for base "FORMUL_PROG"
if !abprg(2) then error "B³¹d przy otwieraniu bazy FORMUL_PROG !!!"
int bform = open Katalog()+"bazy.2\\43kp0201.dat" for base "FORMULY"
int bprg
long iMut // numer mutacji pliku, je¿eli nazwy siê dubluj¹
string sTresc

for ib=1 to ib>2
  bprg = abprg(ib)
  Mkdir(kat_wyj)

  SetKey (bprg, "skrot")
  
  err = GetRec (bprg, FS)
  while !err
    buf = GetField(bprg,"nazwa")
    delete regular at "?##\\"
    nazwa_wyj = buf
    if GetField(bprg,"idcomp") != 0 then goto nast
    // w KDP pole (prg, "nazwa") ma 50 znaków, ale dali tam tak d³ugie œcie¿ki, ¿e
    // nazwa pliku jest przycinana - lepiej wyci¹gn¹æ nazwê pliku z wnêtrza raportu
    buf = GetField(bprg, "dane")
    if ib == 1 then
      if find regular at "^////[0-9]+,\"{[~\"]++}\"," then nazwa_wyj = regular 1
    else
      if find regular at "^////\"{[~\"]++}\"," then nazwa_wyj = regular 1
    endif
    if ib == 2 then
      // w kd150c s¹ formu³y widma, które nie maj¹ odpowiedników w prawdziwej
      // bazie formu³ - pomin¹æ widma
      SetKey(bform, "nazwa")
      SetKeySeg(bform, "nazwa", GetField(bprg, "skrot"))
      if GetRec(bform, EQ) != 0 then goto nast
    endif
    if nazwa_wyj == "" then nazwa_wyj = wczytaj_string ("Podaj nazwê dla _" + GetField(bprg,"skrot"),"")
    if nazwa_wyj then
      print kat_wyj+nazwa_wyj,GetField(bprg,"skrot"), GetField(bprg,"idcomp"), "..."
      buf = GetField(bprg,"dane")

      sTresc = buf
      iMut = 1
      while find file kat_wyj+nazwa_wyj
        iMut += 1
        buf = nazwa_wyj
        if find regular "^{*}_[0-9]++.{*}$" then buf = (regular 1) + "." + (regular 2)
        move to 0
        if !find "." then buf += "."
        move to 0
        replace ".", (using "_%l.", iMut)
        // message (using "nazwa_wyj zmieniona z\n%s\nna\n%s", nazwa_wyj, buf)
        nazwa_wyj = buf
      wend

      plik_wyj = 0
      plik_wyj = open kat_wyj+nazwa_wyj for binary output
      if plik_wyj==0 then
        print "Nie uda³o siê zachowaæ pliku ";kat_wyj;nazwa_wyj;lf
      else
        print #plik_wyj; sTresc
        close plik_wyj
        print "Zapisany"
      endif
    endif
    print lf
    nast:
    err = GetRec (bprg, NX)
  wend

  // drugi przebieg zapisze wsio w podkatalogu
  kat_wyj += "formuly\\"
  
next ib
