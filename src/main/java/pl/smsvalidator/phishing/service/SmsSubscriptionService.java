package pl.smsvalidator.phishing.service;

import org.springframework.stereotype.Service;
import pl.smsvalidator.phishing.model.SubscriptionMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsSubscriptionService {

    private final Map<String, Boolean> subscriptions = new ConcurrentHashMap<>();

    public void setSubscriptionToRecipient(String recipient, SubscriptionMode mode) {
        if (recipient == null || mode == null) {
            return;
        }
        subscriptions.put(recipient, mode.isEnabled());
    }

    public boolean isSubscriptionActive(String recipient) {
        if (recipient == null) {
            return false;
        }
        return subscriptions.getOrDefault(recipient, false);
    }
}
