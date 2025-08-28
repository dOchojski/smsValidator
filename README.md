# 📱 SMS Validator

Prosty serwis napisany, którego celem jest analiza treści SMS-ów i wykrywanie potencjalnych prób phishingu.

---

## ✨ Funkcjonalności
- Obsługa wielu SMS-ów w jednym żądaniu (batch).
- Ekstrakcja adresów URL z treści wiadomości.
- Integracja z zewnętrznym serwisem `evaluateUri` (zasymulowanym przez **WireMock**).
- Klasyfikacja SMS jako:
    - **PHISHING** – jeśli choć jeden URL ma `confidenceLevel >= HIGH`.
    - **SAFE** – w pozostałych przypadkach.
- Obsługa komend **START** / **STOP**:
    - `START` – włącza usługę filtrowania dla numeru odbiorcy.
    - `STOP` – wyłącza usługę filtrowania dla numeru odbiorcy.
- REST API (`/api/v1/sms/evaluate`).
- Gotowy obraz Dockera dostępny w **Docker Hub**.

---

## 🏗 Architektura
Warstwy projektu:
- **Controller** – przyjmuje żądania HTTP.
- **Service** – logika biznesowa (ekstrakcja URL, klasyfikacja, START/STOP).
- **Adapter** – komunikacja z zewnętrznym serwisem (`evaluateUri`).
- **Stub (WireMock)** – symuluje Google WebRisk API.

### Przepływ
1. Klient wywołuje `POST /api/v1/sms/evaluate`
2. Serwis wyciąga URL-e z treści.
3. Dla każdego URL → zapytanie do `evaluateUri` (WireMock).
4. Wynik oceny mapowany na klasyfikację SMS.
5. Zwracana lista wyników.

---

## ⚙️ Konfiguracja
Zmienne środowiskowe (z `docker-compose.yml`):
- `WEBRISK_BASE_URL` – adres serwisu evaluateUri (stub).
- `WEBRISK_API_TOKEN` – token autoryzacyjny (dummy w tej wersji).
- `THREAT_TYPES` – lista typów zagrożeń (domyślnie `SOCIAL_ENGINEERING,MALWARE,UNWANTED_SOFTWARE`).
- `ALLOW_SCAN` – `true/false`.

---

## 🐳 Uruchamianie

### Opcja 1. Lokalnie z Dockera Hub
Pobranie obrazu i uruchomienie:
```bash
docker pull dochojski/phishing-sms:latest
docker run -p 18080:8080 \
  -e WEBRISK_BASE_URL=http://webrisk-stub:8080 \
  -e WEBRISK_API_TOKEN=dummy \
  -e THREAT_TYPES=SOCIAL_ENGINEERING,MALWARE,UNWANTED_SOFTWARE \
  -e ALLOW_SCAN=true \
  dochojski/phishing-sms:latest
```


## 📚 Dodatkowe założenia

1. TART/STOP zapamiętywane w pamięci RAM (in-memory). W wersji produkcyjnej należałoby użyć DB/Redis.
2. Zewnętrzny serwis evaluateUri zasymulowany przez WireMock → brak kosztów za prawdziwy WebRisk.
3. Mappingi phishing/safe oparte o proste reguły domenowe/keywordy.

## 🔎 Przykłady użycia API
```bash

Request
curl -X POST "http://localhost:18080/api/v1/sms/evaluate" \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      { "id": "1", "sender": "Bank", "recipient": "48700800999", "text": "Dopłać 1 PLN: http://bank-pl.com/verify" },
      { "id": "2", "sender": "InPost", "recipient": "48700800999", "text": "Śledzenie: https://inpost.pl/tracking/ABC" }
    ]
  }'

Response
{
  "results": [
    {
      "id": "1",
      "classification": "PHISHING",
      "urls": [
        {
          "url": "http://bank-pl.com/verify",
          "scores": [
            { "threatType": "SOCIAL_ENGINEERING", "confidence": "HIGHER" }
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
            { "threatType": "MALWARE", "confidence": "SAFE" }
          ]
        }
      ]
    }
  ]
}
```