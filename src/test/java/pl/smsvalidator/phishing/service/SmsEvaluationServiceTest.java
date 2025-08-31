package pl.smsvalidator.phishing.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.smsvalidator.phishing.model.Classification;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.ThreatType;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;
import pl.smsvalidator.phishing.model.dto.internal.SmsEvaluationRequest;
import pl.smsvalidator.phishing.model.dto.internal.SmsEvaluationResponse;
import pl.smsvalidator.phishing.model.dto.internal.SmsMessageDto;
import pl.smsvalidator.phishing.model.dto.internal.UrlEvaluationDto;
import pl.smsvalidator.phishing.web.EvaluateUriClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsEvaluationServiceTest {

    @Mock
    private UrlExtractorService extractor;

    @Mock
    private EvaluateUriClient client;

    @Mock
    private SmsSubscriptionService subscriptionService;

    @InjectMocks
    private SmsEvaluationService service;

    @Test
    void shouldClassifyAsPhishingWhenHighConfidence() {
        //given
        String url = "http://bad.com";
        List<EvaluateUriResponse.Score> scores = List.of(new EvaluateUriResponse.Score(ThreatType.MALWARE, ConfidenceLevel.HIGH));
        SmsMessageDto message = new SmsMessageDto("1", "Bank", "48700800999", "Sprawdź: http://bad.com");
        SmsEvaluationRequest request = new SmsEvaluationRequest(List.of(message));

        when(extractor.extract(message.text())).thenReturn(List.of(url));
        when(client.evaluate(url)).thenReturn(new EvaluateUriResponse(scores));
        when(subscriptionService.isSubscriptionActive("48700800999")).thenReturn(Boolean.TRUE);

        //when
        SmsEvaluationResponse response = service.evaluate(request);

        //then
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().getFirst().id()).isEqualTo("1");
        assertThat(response.results().getFirst().classification()).isEqualTo(Classification.PHISHING);
        assertThat(response.results().getFirst().urls()).isEqualTo(List.of(new UrlEvaluationDto(url, scores)));
    }

    @Test
    void shouldClassifyAsSafeWhenBelowThreshold() {
        //given
        String url = "http://good.com";
        List<EvaluateUriResponse.Score> scores = List.of(new EvaluateUriResponse.Score(ThreatType.MALWARE, ConfidenceLevel.LOW));
        SmsMessageDto message = new SmsMessageDto("1", "Bank", "48700800999", "Sprawdź: http://good.com");
        SmsEvaluationRequest request = new SmsEvaluationRequest(List.of(message));

        when(extractor.extract(message.text())).thenReturn(List.of(url));
        when(client.evaluate(url)).thenReturn(new EvaluateUriResponse(scores));
        when(subscriptionService.isSubscriptionActive("48700800999")).thenReturn(Boolean.TRUE);

        //when
        SmsEvaluationResponse response = service.evaluate(request);

        //then
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().getFirst().id()).isEqualTo("1");
        assertThat(response.results().getFirst().classification()).isEqualTo(Classification.SAFE);
        assertThat(response.results().getFirst().urls()).isEqualTo(List.of(new UrlEvaluationDto(url, scores)));
    }

    @Test
    void shouldThrowExceptionWhenCouldNotRetrieveEvaluateUriResponseFromExternalClient() {
        //given
        String url = "http://good.com";
        SmsMessageDto message = new SmsMessageDto("1", "Bank", "48700800999", "Sprawdź: http://good.com");
        SmsEvaluationRequest request = new SmsEvaluationRequest(List.of(message));

        when(extractor.extract(message.text())).thenReturn(List.of(url));
        when(client.evaluate(url)).thenReturn(null);
        when(subscriptionService.isSubscriptionActive("48700800999")).thenReturn(Boolean.TRUE);

        //expect
        assertThatThrownBy(() -> service.evaluate(request), "Couldn't retrieve evaluate URI response");
    }
}
