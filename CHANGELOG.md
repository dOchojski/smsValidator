# Changelog

## [0.1.0] - 2025-08-27
### Added
- Pierwsza wersja aplikacji.
- REST API `/api/v1/sms/evaluate`.
- Obsługa SMS-ów.
- Ekstrakcja URL-i z treści wiadomości.
- Klasyfikacja PHISHING/SAFE na podstawie poziomu ryzyka.
- Integracja z zewnętrznym serwisem evaluateUri (stub WireMock).
- Obsługa START/STOP (subskrypcja w pamięci).
- Dockerfile + docker-compose.
- Obraz dostępny w DockerHub: `dochojski/phishing-sms`.

### Tests
- Testy jednostkowe (UrlExtractor, klasyfikacja).
- Testy integracyjne (REST API z mockiem evaluateUri).
