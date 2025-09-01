package pl.smsvalidator.phishing.model.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import pl.smsvalidator.phishing.model.ThreatType;

import java.util.List;

@Builder
public record EvaluateUriRequest(
        @NotNull
        String uri,
        @NotNull
        @JsonProperty("threatTypes")
        List<ThreatType> threatTypes,
        @JsonProperty("allowScan")
        boolean allowScan) {

    public static EvaluateUriRequest from(String url, List<ThreatType> threatTypes,  Boolean allowScan) {
        return EvaluateUriRequest.builder()
                .uri(url)
                .threatTypes(threatTypes)
                .allowScan(allowScan)
                .build();
    }
}
