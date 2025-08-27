package pl.smsvalidator.phishing.service;

import pl.smsvalidator.phishing.adapter.EvaluateUriClient;
import pl.smsvalidator.phishing.model.*;
import pl.smsvalidator.phishing.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SmsEvaluationService {

    private final UrlExtractor urlExtractor;
    private final EvaluateUriClient evaluateUriClient;

    private static final ConfidenceLevel THRESHOLD = ConfidenceLevel.HIGH;

    public SmsEvaluationResponse evaluateBatch(SmsEvaluationRequest req) {
        List<MessageResultDto> results = new ArrayList<>();

        req.getMessages().forEach(msg -> {
            List<String> urls = urlExtractor.extract(msg.getText());
            List<UrlEvaluationDto> urlEvaluations = new ArrayList<>();
            boolean risky = false;

            for (String url : urls) {
                List<ScoreDto> scores = evaluateUriClient.evaluate(url);
                urlEvaluations.add(new UrlEvaluationDto(url, scores));

                boolean anyHighOrAbove = scores.stream()
                    .anyMatch(s -> s.getConfidence().atLeast(THRESHOLD));
                risky = risky || anyHighOrAbove;
            }

            results.add(new MessageResultDto(
                msg.getId(),
                risky ? Classification.PHISHING : Classification.SAFE,
                urlEvaluations));
        });
        return new SmsEvaluationResponse(results);
    }
}