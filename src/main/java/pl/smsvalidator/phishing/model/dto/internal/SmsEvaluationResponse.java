package pl.smsvalidator.phishing.model.dto.internal;

import java.util.List;

public record SmsEvaluationResponse(List<MessageResultDto> results) {
}
