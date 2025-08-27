package pl.smsvalidator.phishing;

import pl.smsvalidator.phishing.adapter.EvaluateUriClient;
import pl.smsvalidator.phishing.model.*;
import pl.smsvalidator.phishing.model.dto.*;
import pl.smsvalidator.phishing.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class ClassificationThresholdTest {
    @Test void messageBecomesPhishingWhenAnyUrlHighOrAbove() {
        // given
        UrlExtractor extractor = new UrlExtractor();
        EvaluateUriClient client = Mockito.mock(EvaluateUriClient.class);

        Mockito.when(client.evaluate(anyString()))
            .thenReturn(List.of(
                new ScoreDto(ThreatType.MALWARE, ConfidenceLevel.HIGH)
            ));

        SmsEvaluationService svc = new SmsEvaluationService(extractor, client);

        SmsEvaluationRequest req = new SmsEvaluationRequest();

        SmsMessageDto msg = new SmsMessageDto();
        msg.setId("1");
        msg.setSender("Bank");
        msg.setText("Dopłać 999 PLN: http://fake-bank.com/pay");

        req.setMessages(List.of(msg));

        // when
        SmsEvaluationResponse resp = svc.evaluateBatch(req);

        // then
        assertEquals(Classification.PHISHING, resp.getResults().getFirst().getClassification());
    }
}