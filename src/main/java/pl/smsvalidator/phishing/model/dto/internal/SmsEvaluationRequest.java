package pl.smsvalidator.phishing.model.dto.internal;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SmsEvaluationRequest(@NotEmpty List<SmsMessageDto> messages) {
}