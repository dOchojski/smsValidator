package pl.smsvalidator.phishing.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.List;

@Builder
public record EvaluateUriRequest(
    String uri,
    @JsonProperty("threatTypes")
    List<String> threatTypes,
    @JsonProperty("allowScan")
    boolean allowScan) {
}
