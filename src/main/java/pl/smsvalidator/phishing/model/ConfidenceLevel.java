package pl.smsvalidator.phishing.model;

public enum ConfidenceLevel {
    SAFE, LOW, MEDIUM, HIGH, HIGHER, VERY_HIGH, EXTREMELY_HIGH;

    public boolean atLeast(ConfidenceLevel threshold) {
        return this.ordinal() >= threshold.ordinal();
    }
}