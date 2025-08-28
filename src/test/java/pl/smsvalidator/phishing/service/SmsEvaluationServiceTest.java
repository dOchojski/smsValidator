package pl.smsvalidator.phishing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.smsvalidator.phishing.adapter.EvaluateUriClient;
import pl.smsvalidator.phishing.model.Classification;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.ThreatType;
import pl.smsvalidator.phishing.model.dto.ScoreDto;
import pl.smsvalidator.phishing.model.dto.SmsEvaluationRequest;
import pl.smsvalidator.phishing.model.dto.SmsEvaluationResponse;
import pl.smsvalidator.phishing.model.dto.SmsMessageDto;

class SmsEvaluationServiceTest {

    private final UrlExtractor extractor = new UrlExtractor();
    private final EvaluateUriClient client = mock(EvaluateUriClient.class);
    private final SmsSubscriptionService subscriptionService = new SmsSubscriptionService();
    private SmsEvaluationService service;

    @BeforeEach
    void setUp() {
        service = new SmsEvaluationService(extractor, client, subscriptionService);
        subscriptionService.handleCommand("48700800999", "START");
    }


    @Test
    void shouldClassifyAsPhishingWhenHighConfidence() {
        SmsMessageDto msg = new SmsMessageDto("1", "Bank", "48700800999", "Sprawdź: http://bad.com");
        SmsEvaluationRequest req = new SmsEvaluationRequest(List.of(msg));

        when(client.evaluate(anyString()))
            .thenReturn(List.of(new ScoreDto(ThreatType.MALWARE, ConfidenceLevel.HIGHER)));

        SmsEvaluationResponse resp = service.evaluate(req);

        assertThat(resp.getResults()).hasSize(1);
        assertThat(resp.getResults().get(0).getClassification()).isEqualTo(Classification.PHISHING);
    }

    @Test
    void shouldClassifyAsSafeWhenBelowThreshold() {
        SmsMessageDto msg =
            new SmsMessageDto("2", "InPost", "48700800999", "Śledzenie: https://inpost.pl/tracking/ABC");
        SmsEvaluationRequest req = new SmsEvaluationRequest(List.of(msg));

        when(client.evaluate(anyString()))
            .thenReturn(List.of(new ScoreDto(ThreatType.MALWARE, ConfidenceLevel.SAFE)));

        SmsEvaluationResponse resp = service.evaluate(req);

        assertThat(resp.getResults()).hasSize(1);
        assertThat(resp.getResults().get(0).getClassification()).isEqualTo(Classification.SAFE);
    }
}
