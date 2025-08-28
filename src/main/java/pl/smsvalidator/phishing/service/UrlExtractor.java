package pl.smsvalidator.phishing.service;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.*;

@Component
public class UrlExtractor {
    private static final Pattern URL_PATTERN = Pattern.compile(
        "(?i)(https?://[\\p{L}0-9\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)");

    public List<String> extract(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        List<String> urls = new ArrayList<>();
        Matcher m = URL_PATTERN.matcher(text);
        while (matcher.find()) {
            String url = matcher.group();
            url = url.replaceAll("[,.;!?]+$", "");
            urls.add(url);
        }
        return urls;
    }
}