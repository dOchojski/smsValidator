package pl.smsvalidator.phishing.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.smsvalidator.phishing.config.WebRiskProperties;
import pl.smsvalidator.phishing.exception.ExternalServiceException;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriRequest;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Profile("!local")
@Component
@RequiredArgsConstructor
public class EvaluateUriClient implements IEvaluateUriClient {
    private static final String PATH = "/v1eap1:evaluateUri";

    private final RestTemplate restTemplate;
    private final WebRiskProperties webRiskProperties;

    public EvaluateUriResponse evaluate(String url) {
        EvaluateUriRequest body = createEvaluateRequest(url);

        RequestEntity<EvaluateUriRequest> requestEntity = RequestEntity.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer %s".formatted(webRiskProperties.getApiToken()))
                .body(body);

        try {
            return restTemplate.exchange(requestEntity, EvaluateUriResponse.class).getBody();
        } catch (Exception ex) {
            throw new ExternalServiceException("Couldn't retrieve evaluate URI response", ex);
        }
    }

    private EvaluateUriRequest createEvaluateRequest(String url) {
        return EvaluateUriRequest.from(url,
                webRiskProperties.getThreatTypes(),
                webRiskProperties.getAllowScan());
    }
}