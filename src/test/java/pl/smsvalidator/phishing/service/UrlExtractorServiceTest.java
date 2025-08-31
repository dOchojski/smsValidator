package pl.smsvalidator.phishing.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UrlExtractorServiceTest {

    UrlExtractorService extractor = new UrlExtractorService();

    @Test
    void extractsMultipleUrls() {
        String text = "Dopłać 999zł: http://pekao-pl.com/verify oraz https://inpost.pl/tracking/ABC";
        List<String> urls = extractor.extract(text);
        assertEquals(2, urls.size());
        assertTrue(urls.get(0).startsWith("http://"));
        assertTrue(urls.get(1).startsWith("https://"));
    }

    @Test
    void extractsUnicodePaths() {
        String text = "Sprawdź https://aaaaaa.pl/zażółć-gęślą-jaźń";
        List<String> urls = extractor.extract(text);
        assertEquals(1, urls.size());
    }
}