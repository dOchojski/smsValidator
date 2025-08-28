package pl.smsvalidator.phishing;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.smsvalidator.phishing.adapter.EvaluateUriClient;
import pl.smsvalidator.phishing.model.*;
import pl.smsvalidator.phishing.model.dto.*;
import pl.smsvalidator.phishing.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class ClassificationThresholdTest {

    @Test
    void messageBecomesPhishingWhenAnyUrlHighOrAbove() {
        // given
        UrlExtractor extractor = new UrlExtractor();
        EvaluateUriClient client = Mockito.mock(EvaluateUriClient.class);
        SmsSubscriptionService subscriptionService = new SmsSubscriptionService();

        subscriptionService.handleCommand("48700800999", "START");

        Mockito.when(client.evaluate(anyString()))
            .thenReturn(List.of(
                new ScoreDto(ThreatType.MALWARE, ConfidenceLevel.SAFE)
            ));

        SmsEvaluationService svc = new SmsEvaluationService(extractor, client, subscriptionService);

        SmsMessageDto msg = new SmsMessageDto();
        msg.setId("1");
        msg.setSender("Bank");
        msg.setRecipient("48700800999");
        msg.setText("Dopłać 999 PLN: http://sadasdasdsa.com/");

        SmsEvaluationRequest req = new SmsEvaluationRequest(List.of(msg));

        // when
        SmsEvaluationResponse resp = svc.evaluate(req);

        // then
        assertEquals(Classification.SAFE,
            resp.getResults().getFirst().getClassification());
    }
}