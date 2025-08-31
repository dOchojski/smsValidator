package pl.smsvalidator.phishing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.smsvalidator.phishing.model.dto.internal.SmsEvaluationRequest;
import pl.smsvalidator.phishing.model.dto.internal.SmsEvaluationResponse;
import pl.smsvalidator.phishing.model.dto.internal.SmsSubscriptionRequest;
import pl.smsvalidator.phishing.service.SmsEvaluationService;

@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsEvaluationController {
    private final SmsEvaluationService service;

    @PostMapping(value = "/evaluate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsEvaluationResponse> evaluate(@Valid @RequestBody SmsEvaluationRequest request) {
        return ResponseEntity.ok(service.evaluate(request));
    }

    @PostMapping(value = "/subscribe",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void subscribe(@Valid @RequestBody SmsSubscriptionRequest request) {
        service.subscribe(request);
    }
}

