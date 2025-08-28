package pl.smsvalidator.phishing.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsSubscriptionService {

    private final Map<String, Boolean> subscriptions = new ConcurrentHashMap<>();

    public void handleCommand(String recipient, String message) {
        String normalized = message.trim().toUpperCase();
        if (normalized.equals("START")) {
            subscriptions.put(recipient, true);
        } else if (normalized.equals("STOP")) {
            subscriptions.put(recipient, false);
        }
    }

    public boolean isActive(String recipient) {
        return subscriptions.getOrDefault(recipient, false);
    }
}
