package pl.smsvalidator.phishing.web;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.ThreatType;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;

import java.util.List;

@Profile("local")
@Component
public class DummyEvaluateUriClient implements IEvaluateUriClient {

    public EvaluateUriResponse evaluate(String url) {
        return new EvaluateUriResponse(List.of(new EvaluateUriResponse.Score(
                ThreatType.SOCIAL_ENGINEERING,
                ConfidenceLevel.HIGHER
        )));
    }
}
