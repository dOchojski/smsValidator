package pl.smsvalidator.phishing.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.smsvalidator.phishing.adapter.EvaluateUriClient;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.ThreatType;
import pl.smsvalidator.phishing.model.dto.ScoreDto;
import pl.smsvalidator.phishing.service.SmsSubscriptionService;

@SpringBootTest
@AutoConfigureMockMvc
class SmsEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EvaluateUriClient client;

    @Autowired
    private SmsSubscriptionService subscriptionService;

    @BeforeEach
    void enableSubscription() {
        subscriptionService.handleCommand("48700800999", "START");
    }

    @Test
    void shouldReturnPhishingResponse() throws Exception {
        when(client.evaluate(anyString()))
            .thenReturn(List.of(new ScoreDto(ThreatType.SOCIAL_ENGINEERING, ConfidenceLevel.HIGHER)));

        String requestBody = """
            {
              "messages": [
                { "id": "1", "sender": "Bank", "recipient": "48700800999", "text": "Dopłać 1 PLN: http://bank-pl.com/verify" }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/sms/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.results[0].classification").value("PHISHING"));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EvaluateUriClient evaluateUriClient() {
            return Mockito.mock(EvaluateUriClient.class);
        }
    }
}
