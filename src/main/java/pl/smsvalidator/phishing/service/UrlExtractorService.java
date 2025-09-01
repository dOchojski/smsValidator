package pl.smsvalidator.phishing.service;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.*;

@Component
public class UrlExtractorService {
    private static final Pattern URL_PATTERN = Pattern.compile(
        "(?i)(https?://[\\p{L}0-9\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)");
    private static final String SPECIAL_SIGN_PATTERN = "[,.;!?]+$";

    public List<String> extract(String message) {
        Matcher matcher = URL_PATTERN.matcher(message);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            String url = removeSpecialSigns(matcher.group());
            urls.add(url);
        }
        return urls;
    }

    private String removeSpecialSigns(String url) {
        return url.replaceAll(SPECIAL_SIGN_PATTERN, "");
    }
}