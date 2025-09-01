package pl.smsvalidator.phishing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionMode {
    START(true),
    STOP(false);

    private final boolean enabled;
}
