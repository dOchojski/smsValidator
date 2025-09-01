package pl.smsvalidator.phishing.web;

import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;

public interface IEvaluateUriClient {

    EvaluateUriResponse evaluate(String url);
}
