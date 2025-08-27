package pl.smsvalidator.phishing.service;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.*;

@Component
public class UrlExtractor {
    private static final Pattern URL_PATTERN = Pattern.compile(
        "(?i)\\b((?:https?://)[\\p{L}0-9\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)");

    public List<String> extract(String text) {
        List<String> urls = new ArrayList<>();
        Matcher m = URL_PATTERN.matcher(text);
        while (m.find())
            urls.add(m.group(1));
        return urls;
    }
}