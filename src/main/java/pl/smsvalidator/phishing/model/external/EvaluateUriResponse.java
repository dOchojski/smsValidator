package pl.smsvalidator.phishing.model.external;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluateUriResponse(
    @JsonProperty("scores")
    List<Score> scores) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Score(
        @JsonProperty("threatType")
        String threatType,
        @JsonProperty("confidenceLevel")
        String confidenceLevel) {
    }
}