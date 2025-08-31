package pl.smsvalidator.phishing.model;

public enum ConfidenceLevel {
    SAFE,
    LOW,
    MEDIUM,
    HIGH,
    HIGHER,
    VERY_HIGH,
    EXTREMELY_HIGH;

    public boolean atLeastHigh() {
        return this == HIGH
                || this == HIGHER
                || this == VERY_HIGH
                || this == EXTREMELY_HIGH;
    }
}