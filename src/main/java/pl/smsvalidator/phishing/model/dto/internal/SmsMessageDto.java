package pl.smsvalidator.phishing.model.dto.internal;

public record SmsMessageDto(String id, String sender, String recipient, String text) {
}