package pl.smsvalidator.phishing.controller;

import pl.smsvalidator.phishing.model.dto.*;
import pl.smsvalidator.phishing.service.SmsEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsEvaluationController {
    private final SmsEvaluationService service;

    @PostMapping(value = "/evaluate", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public SmsEvaluationResponse evaluate(@Valid @RequestBody SmsEvaluationRequest request) {
        return service.evaluate(request);
    }
}

