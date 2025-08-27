# Phishing SMS Evaluator

Prosty serwis do oceny SMS-ów. Dla każdego SMS-a:
1) wyciąga URL-e,
2) odpytuje zewnętrzny `evaluateUri`,
3) klasyfikuje jako `PHISHING` jeśli którykolwiek URL ma `confidence >= HIGH`,
4) zwraca szczegółową listę ocen URL.

## Konfiguracja (env / application.yml)
- `WEBRISK_BASE_URL` (domyślnie `https://webrisk.googleapis.com`)
- `WEBRISK_API_TOKEN`
- `THREAT_TYPES` – CSV, np. `SOCIAL_ENGINEERING,MALWARE,UNWANTED_SOFTWARE`
- `ALLOW_SCAN` – `true/false`

## Uruchomienie lokalne
```bash
export WEBRISK_BASE_URL="http://localhost:9090"  # stub
export WEBRISK_API_TOKEN="dummy"
mvn spring-boot:run
