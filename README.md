# WordLearn – anglų žodžio priminimas kas valandą

Šis projektas kas valandą parodo pranešimą (notification) su nauju anglišku žodžiu
ir jo lietuvišku vertimu. Pagrindiniame ekrane taip pat matosi paskutinis parodytas
žodis ir yra mygtukas iš karto pamatyti kitą.

## Variantas A: gauti APK BE Android Studio (per GitHub, nemokamai)

Projekte jau yra `.github/workflows/build.yml` failas, kuris automatiškai sukompiliuoja
APK debesyje (GitHub Actions), kai tik įkeli kodą į GitHub. Nereikia nieko diegti į
kompiuterį.

1. Susikurk nemokamą paskyrą **github.com** (jei dar neturi).
2. Paspausk **New repository**, duok pavadinimą (pvz. `wordlearn`), pasirink **Public**
   arba **Private**, paspausk **Create repository**.
3. Naujo repo puslapyje paspausk **uploading an existing file** (arba
   "Add file" → "Upload files").
4. Išsiskleisk šio projekto ZIP failą savo kompiuteryje ir įtempk **visą turinį**
   (visus aplankus ir failus iš `WordLearnApp` aplanko) į GitHub įkėlimo langą.
   *(Svarbu: kelk turinį, kuris yra VIDUJE `WordLearnApp` aplanko, o ne patį aplanką.)*
5. Paspausk **Commit changes**.
6. Eik į skirtuką **Actions** viršuje. Turėtų automatiškai pradėti veikti "Build APK"
   darbas (jei nepradėjo – paspausk jį kairėje ir tada **Run workflow**).
7. Palauk 2–5 minutes, kol atsiras žalias varnelė ✅.
8. Paspausk ant baigto darbo → apačioje skiltyje **Artifacts** matysi
   `WordLearn-debug-apk` – atsisiųsk jį (tai bus `.zip`, viduje bus `app-debug.apk`).
9. Persiųsk `app-debug.apk` į telefoną (per Google Drive, el. paštą, USB ar pan.).
10. Telefone atidaryk failą → jei paprašys, leisk "Install from unknown sources" →
    **Install**.

**Pastaba dėl `.github` aplanko:** naršyklės failų įkėlimo langas kartais nerodo ar
nepriima paslėptų aplankų iš kai kurių operacinių sistemų failų naršyklių. Jei taip
nutinka, paprasčiausias sprendimas – GitHub svetainėje, jau įkėlus likusius failus,
paspausti **Add file → Create new file**, įvesti kelią `.github/workflows/build.yml`
kaip failo pavadinimą (GitHub pats sukurs aplankus) ir įklijuoti šio failo turinį
(jis yra ir šiame ZIP faile, aplanke `.github/workflows/`).

Tai ir yra tikras, į telefoną įsidiegiamas appsas – be Android Studio kompiuteryje.

## Variantas B: per Android Studio (jei turi/nori kompiuterį)

1. Atsisiųsk ir įsidiek **Android Studio** (https://developer.android.com/studio).
2. Atsidaryk Android Studio → **Open** → pasirink šio projekto aplanką (`WordLearnApp`).
3. Palauk, kol Gradle sinchronizuosis.
4. Prijunk Android telefoną USB laidu su įjungtu "USB debugging" (Developer options),
   arba naudok emuliatorių (Device Manager → Create device).
5. Paspausk žalią **Run ▶** mygtuką.

## Kaip veikia appsas

- `app/src/main/assets/words.json` – žodžių sąrašas (žodis + vertimas). Gali laisvai
  pridėti, keisti ar pašalinti įrašus – tiesiog redaguok šį failą ir sukompiliuok iš naujo.
- `WordRepository.kt` – atsakingas už žodžių skaitymą ir sekimą, koks žodis parodytas
  paskutinis (kad neitum ratu per tuos pačius žodžius kelis kartus iš eilės).
- `WordReminderWorker.kt` – `WorkManager` užduotis, kuri kas valandą pasiima kitą žodį
  ir parodo pranešimą.
- `NotificationHelper.kt` – sukuria pranešimų kanalą ir parodo patį pranešimą.
- `MainActivity.kt` – pagrindinis ekranas: mygtukas įjungti/išjungti priminimus ir
  mygtukas iš karto pamatyti kitą žodį.

## Svarbu žinoti

- Android 13+ reikalauja atskiro leidimo pranešimams (`POST_NOTIFICATIONS`) – appsas
  jo paprašys automatiškai, kai paspausi "Įjungti priminimus".
- `WorkManager` periodinės užduotys realiai gali suveikti šiek tiek vėliau nei tiksliai
  kas valandą (Android taip taupo bateriją) – tai normalu.
- Kai kuriuose telefonuose (ypač Xiaomi, Huawei, Samsung su agresyviu baterijos
  taupymu) appsą reikia rankiniu būdu pridėti į "autostart" arba "no battery
  optimization" sąrašą, kitaip Android gali visai sustabdyti foninę užduotį.
- Šis APK yra "debug" versija – Android telefonas prieš diegimą paklaus leidimo
  "Install from unknown sources" (arba "Allow from this source"), nes appsas
  nėra iš Google Play. Tai normalu ir saugu, nes tai tavo paties sukompiliuotas kodas.
- Dabartinis žodžių sąrašas turi apie 220 populiariausių angliškų žodžių.

## Kas toliau (idėjos plėtrai)

- Pridėti tarimą (audio) arba pavyzdinį sakinį prie kiekvieno žodžio.
- Leisti pasirinkti kelias temas (kelionės, verslas, buitis ir pan.).
- Statistika: kiek žodžių jau parodyta, mažas testas kartą per savaitę.
