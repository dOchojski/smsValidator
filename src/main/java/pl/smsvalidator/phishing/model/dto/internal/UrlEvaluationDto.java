package pl.smsvalidator.phishing.model.dto.internal;

import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;

import java.util.List;

public record UrlEvaluationDto(String url, List<EvaluateUriResponse.Score> scores) {
}