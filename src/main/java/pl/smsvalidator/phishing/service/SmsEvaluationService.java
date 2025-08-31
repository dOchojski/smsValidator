package pl.smsvalidator.phishing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smsvalidator.phishing.exception.ExternalServiceException;
import pl.smsvalidator.phishing.model.Classification;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;
import pl.smsvalidator.phishing.model.dto.internal.*;
import pl.smsvalidator.phishing.web.EvaluateUriClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsEvaluationService {

    private final UrlExtractorService urlExtractorService;
    private final EvaluateUriClient evaluateUriClient;
    private final SmsSubscriptionService subscriptionService;

    public void subscribe(SmsSubscriptionRequest request) {
        subscriptionService.setSubscriptionToRecipient(request.recipient(), request.subscriptionMode());
    }

    public SmsEvaluationResponse evaluate(SmsEvaluationRequest request) {
        List<MessageResultDto> results = request.messages()
                .stream()
                .filter(this::isRecipientSubscribed)
                .map(this::processMessage)
                .toList();

        return new SmsEvaluationResponse(results);
    }

    private boolean isRecipientSubscribed(SmsMessageDto message) {
        return subscriptionService.isSubscriptionActive(message.recipient());
    }

    private MessageResultDto processMessage(SmsMessageDto message) {
        List<String> urls = urlExtractorService.extract(message.text());

        List<UrlEvaluationDto> urlEvaluations = new ArrayList<>();
        Classification classification = Classification.SAFE;

        for (String url : urls) {
            EvaluateUriResponse evaluateResponse = evaluateUriClient.evaluate(url);
            if (evaluateResponse == null) {
                throw new ExternalServiceException("Couldn't retrieve evaluate URI response");
            }

            List<EvaluateUriResponse.Score> scores = evaluateResponse.scores();
            urlEvaluations.add(new UrlEvaluationDto(url, scores));

            boolean anyHighOrAbove = scores
                    .stream()
                    .map(EvaluateUriResponse.Score::confidenceLevel)
                    .anyMatch(ConfidenceLevel::atLeastHigh);

            if (anyHighOrAbove) {
                classification = Classification.PHISHING;
            }
        }

        return new MessageResultDto(message.id(), classification, urlEvaluations);
    }
}