package pl.smsvalidator.phishing.model.dto.internal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.smsvalidator.phishing.model.SubscriptionMode;

public record SmsSubscriptionRequest(@NotEmpty String recipient, @NotNull SubscriptionMode subscriptionMode) {
}