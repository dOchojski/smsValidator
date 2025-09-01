package pl.smsvalidator.phishing.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.smsvalidator.phishing.config.WebRiskProperties;
import pl.smsvalidator.phishing.exception.ExternalServiceException;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvaluateUriClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WebRiskProperties webRiskProperties;

    @InjectMocks
    private EvaluateUriClient evaluateUriClient;

    @Test
    void shouldEvaluateUrl() {
        //given
        String url = "http://url.com";

        when(restTemplate.exchange(any(), eq(EvaluateUriResponse.class))).thenReturn(ResponseEntity.ok().body(
                new EvaluateUriResponse(List.of())
        ));

        //when
        evaluateUriClient.evaluate(url);

        //then
        verify(restTemplate, times(1)).exchange(any(), eq(EvaluateUriResponse.class));
    }

    @Test
    void shouldThrowExceptionWhenCouldNotRetrieveScoresFromExternalClient() {
        //given
        String url = "http://url.com";

        when(restTemplate.exchange(any(), eq(EvaluateUriResponse.class))).thenThrow(HttpClientErrorException.class);

        //expect
        assertThrows(ExternalServiceException.class, () -> evaluateUriClient.evaluate(url), "Couldn't retrieve evaluate URI response");
    }
}
