package pl.smsvalidator.phishing.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class UrlExtractorTest {

    @Autowired
    private SmsSubscriptionService subscriptionService;

    private final UrlExtractor extractor = new UrlExtractor();

    @BeforeEach
    void enableSubscription() {
        subscriptionService.handleCommand("48700800999", "START");
    }

    @Test
    void shouldExtractSingleUrl() {
        String text = "Kliknij tutaj: http://bank-pl.com/verify";
        List<String> urls = extractor.extract(text);
        assertThat(urls).containsExactly("http://bank-pl.com/verify");
    }

    @Test
    void shouldExtractMultipleUrls() {
        String text = "Link1: https://abc.com, Link2: http://xyz.net";
        List<String> urls = extractor.extract(text);
        assertThat(urls).containsExactlyInAnyOrder("https://abc.com", "http://xyz.net");
    }

    @Test
    void shouldReturnEmptyWhenNoUrl() {
        String text = "Brak linków w tym SMS.";
        List<String> urls = extractor.extract(text);
        assertThat(urls).isEmpty();
    }
}
