# SMS Validator

**SMS Validator** to prosty serwis napisany w Javie, którego celem jest analiza treści SMS‑ów i wykrywanie potencjalnych prób phishingu.
Aplikacja wyłuskuje linki z wiadomości, sprawdza je za pomocą zewnętrznego serwisu oceniającego (Google WebRisk lub stub WireMock), oznacza wiadomości jako bezpieczne lub phishingowe. 
Dodatkowo użytkownicy mogą włączać/wyłączać filtrowanie wiadomości za pomocą komend **START** / **STOP**.

---

## Funkcjonalności
- Obsługa wielu SMS‑ów w jednym żądaniu.
- Ekstrakcja adresów URL z treści wiadomości.
- Integracja z zewnętrznym serwisem **`evaluateUri`** (domyślnie zasymulowanym przez WireMock, w środowisku produkcyjnym może to być Google WebRisk).
- Klasyfikacja wiadomości:
    - **PHISHING** – jeżeli dla przynajmniej jednego adresu URL poziom zaufania (`confidenceLevel`) jest wysoki (`HIGH` lub wyższy).
    - **SAFE** – we wszystkich pozostałych przypadkach.
- Obsługa komend **START** / **STOP** przez osobny endpoint:
    - `START` – włącza usługę filtrowania dla numeru odbiorcy.
    - `STOP` – wyłącza usługę filtrowania dla numeru odbiorcy.
- REST API:
    - `POST /api/v1/sms/evaluate` – przetwarza listę wiadomości i zwraca listę wyników z klasyfikacją oraz oceną poszczególnych adresów URL.
    - `POST /api/v1/sms/subscribe` – ustawia stan subskrypcji (START/STOP) dla odbiorcy.
- Gotowy obraz Dockera dostępny w **Docker Hub**.

---

## Architektura
Projekt jest zbudowany w oparciu o architekturę warstwową:

- **Controller** – przyjmuje żądania HTTP i zwraca odpowiedzi.
- **Service** – zawiera logikę biznesową:
    - ekstrakcja URL‑i z treści wiadomości,
    - wywołanie zewnętrznego serwisu `evaluateUri`,
    - klasyfikacja wiadomości oraz obsługa subskrypcji.
- **Web/Adapter** – implementacja klienta `evaluateUri`. W profilu `local` używany jest `DummyEvaluateUriClient`, który zwraca stały wynik, a w pozostałych profilach `EvaluateUriClient` wywołuje rzeczywiste API Google WebRisk (lub stub WireMock).
- **Stub (WireMock)** – symuluje odpowiedzi Google WebRisk na podstawie prostych reguł. Mappingi znajdują się w katalogu `wiremock/mappings`.

### Przepływ
1. Klient wysyła żądanie `POST /api/v1/sms/evaluate` z listą wiadomości.
2. Serwis wyłuskuje adresy URL z treści każdej wiadomości.
3. Dla każdego URL‑a serwis wysyła zapytanie do `evaluateUri`.
4. Otrzymane oceny (`scores`) mapowane są na ogólną klasyfikację wiadomości (PHISHING/SAFE).
5. Zwracana jest lista wyników (`results`), zawierająca ID wiadomości, klasyfikację i ocenę poszczególnych adresów URL.

---

## Konfiguracja
Parametry serwisu konfiguruje się za pomocą zmiennych środowiskowych (patrz `application.yml` oraz `docker-compose.yml`):

| Zmienna | Opis | Wartość domyślna |
| --- | --- | --- |
| `WEBRISK_BASE_URL` | adres serwisu `evaluateUri` (np. stub WireMock) | `https://webrisk.googleapis.com` |
| `WEBRISK_API_TOKEN` | token autoryzacyjny używany przez klienta `evaluateUri` | `token-not-set` |
| `ALLOW_SCAN` | flaga `true/false` przekazywana do Google WebRisk | `false` |
| `THREAT_TYPES` | lista typów zagrożeń rozpoznawanych przez API (`SOCIAL_ENGINEERING`, `MALWARE`, `UNWANTED_SOFTWARE`) | `SOCIAL_ENGINEERING,MALWARE,UNWANTED_SOFTWARE` |

Pozostałe parametry, takie jak `timeout-ms` (czas oczekiwania na odpowiedź) czy port serwera (`server.port`), można ustawiać w pliku `application.yml`.

---

## Uruchamianie

### Opcja 1. Użycie obrazu z Docker Hub
Aby szybko uruchomić aplikację, można skorzystać z gotowego obrazu:

```bash
# pobierz obraz
docker pull dochojski/phishing-sms:latest

# uruchom aplikację na porcie 18080 wraz z lokalnym stubem webrisk
# pamiętaj, że webrisk-stub musi być dostępny; można użyć docker-compose
docker run -p 18080:8080 \
  -e WEBRISK_BASE_URL=http://webrisk-stub:8080 \
  -e WEBRISK_API_TOKEN=dummy \
  -e THREAT_TYPES=SOCIAL_ENGINEERING,MALWARE,UNWANTED_SOFTWARE \
  -e ALLOW_SCAN=true \
  dochojski/phishing-sms:latest

```

### Opcja 2. Uruchomienie przez docker-compose

W repozytorium znajduje się plik docker-compose.yml, który startuje zarówno aplikację, jak i stub WireMock:

```bash
docker-compose up --build
# aplikacja dostępna będzie pod adresem http://localhost:8080
```
## Założenia i uproszczenia

Stan subskrypcji (komendy START/STOP) przechowywany jest w pamięci (mapa ConcurrentHashMap). W wersji produkcyjnej należałoby użyć bazy danych

Zewnętrzny serwis evaluateUri jest zasymulowany przez WireMock

Mappingi phishing/safe w plikach phishing.json i safe.json bazują na prostych regułach. Dodatkowa konfiguracja znajduje się w katalogu wiremock/mappings.


## Przykłady użycia API

```bash
# Zapytanie
curl -X POST "http://localhost:8080/api/v1/sms/evaluate" \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      { "id": "1", "sender": "Bank", "recipient": "48700800999", "text": "Dopłać 1 PLN: http://bank-pl.com/verify" },
      { "id": "2", "sender": "InPost", "recipient": "48700800999", "text": "Śledzenie: https://inpost.pl/tracking/ABC" }
    ]
  }'

# Odpowiedź
{
  "results": [
    {
      "id": "1",
      "classification": "PHISHING",
      "urls": [
        {
          "url": "http://bank-pl.com/verify",
          "scores": [
            { "threatType": "SOCIAL_ENGINEERING", "confidenceLevel": "HIGHER" }
          ]
        }
      ]
    },
    {
      "id": "2",
      "classification": "SAFE",
      "urls": [
        {
          "url": "https://inpost.pl/tracking/ABC",
          "scores": [
            { "threatType": "MALWARE", "confidenceLevel": "SAFE" }
          ]
        }
      ]
    }
  ]
}
```

```bash
# Włączenie filtrowania dla numeru
curl -X POST "http://localhost:8080/api/v1/sms/subscribe" \
  -H "Content-Type: application/json" \
  -d '{ "recipient": "48700800999", "subscriptionMode": "START" }'

# Wyłączenie filtrowania
curl -X POST "http://localhost:8080/api/v1/sms/subscribe" \
  -H "Content-Type: application/json" \
  -d '{ "recipient": "48700800999", "subscriptionMode": "STOP" }'
```