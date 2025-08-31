package pl.smsvalidator.phishing.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import pl.smsvalidator.phishing.exception.ExternalServiceException;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.ThreatType;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DummyEvaluateUriClientTest {

    private final DummyEvaluateUriClient dummyEvaluateUriClient = new DummyEvaluateUriClient();

    @Test
    void shouldEvaluateUrl() {
        //given
        String url = "http://url.com";

        EvaluateUriResponse expectedResponse = new EvaluateUriResponse(List.of(new EvaluateUriResponse.Score(
                ThreatType.SOCIAL_ENGINEERING,
                ConfidenceLevel.HIGHER
        )));

        //when
        EvaluateUriResponse result = dummyEvaluateUriClient.evaluate(url);

        //then
        assertThat(result).isEqualTo(expectedResponse);
    }
}
