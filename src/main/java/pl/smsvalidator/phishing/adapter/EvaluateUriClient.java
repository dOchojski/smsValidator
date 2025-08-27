package pl.smsvalidator.phishing.adapter;

import pl.smsvalidator.phishing.config.WebRiskProperties;
import pl.smsvalidator.phishing.exception.ExternalServiceException;
import pl.smsvalidator.phishing.model.*;
import pl.smsvalidator.phishing.model.dto.ScoreDto;
import pl.smsvalidator.phishing.model.external.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EvaluateUriClient {
    private final WebClient webClient;
    private final WebRiskProperties props;
    private static final String PATH = "/v1eap1:evaluateUri";

    public List<ScoreDto> evaluate(String url) {
        EvaluateUriRequest body =
            EvaluateUriRequest.builder()
                .uri(url)
                .threatTypes(props.getThreatTypes())
                .allowScan(props.isAllowScan())
                .build();

        try {
            EvaluateUriResponse resp = webClient.post()
                .uri(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiToken())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(EvaluateUriResponse.class)
                .block();

            if (resp == null || resp.scores() == null) {
                return List.of(new ScoreDto(ThreatType.MALWARE, ConfidenceLevel.SAFE));
            }

            return resp.scores().stream()
                .map(s -> new ScoreDto(
                    ThreatType.valueOf(s.threatType()),
                    ConfidenceLevel.valueOf(s.confidenceLevel())))
                .toList();

        } catch (Exception e) {
            throw new ExternalServiceException("evaluateUri call failed", e);
        }
    }
}