package pl.smsvalidator.phishing.model.dto.internal;

import pl.smsvalidator.phishing.model.Classification;

import java.util.List;

public record MessageResultDto(String id, Classification classification, List<UrlEvaluationDto> urls) {
}

