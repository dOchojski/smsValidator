package pl.smsvalidator.phishing.model.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.ThreatType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluateUriResponse(
    @JsonProperty("scores")
    List<Score> scores) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Score(
        @JsonProperty("threatType")
        ThreatType threatType,
        @JsonProperty("confidenceLevel")
        ConfidenceLevel confidenceLevel) {
    }
}