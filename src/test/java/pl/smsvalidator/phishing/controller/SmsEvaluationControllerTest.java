package pl.smsvalidator.phishing.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.smsvalidator.phishing.model.ConfidenceLevel;
import pl.smsvalidator.phishing.model.SubscriptionMode;
import pl.smsvalidator.phishing.model.ThreatType;
import pl.smsvalidator.phishing.model.dto.external.EvaluateUriResponse;
import pl.smsvalidator.phishing.service.SmsSubscriptionService;
import pl.smsvalidator.phishing.web.EvaluateUriClient;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SmsEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EvaluateUriClient client;

    @Autowired
    private SmsSubscriptionService subscriptionService;

    @Test
    void shouldSwitchSubscription() throws Exception {
        //given
        String requestBody = """
                {
                    "recipient": "48690680321",
                    "subscriptionMode": "START"
                }
                """;

        //expect
        mockMvc.perform(post("/api/v1/sms/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnPhishingResponse() throws Exception {
        //given
        String requestBody = """
                {
                  "messages": [
                    { "id": "1", "sender": "Bank", "recipient": "48700800999", "text": "Dopłać 1 PLN: http://bank-pl.com/verify" }
                  ]
                }
                """;

        when(client.evaluate(anyString()))
                .thenReturn(new EvaluateUriResponse(List.of(new EvaluateUriResponse.Score(ThreatType.SOCIAL_ENGINEERING, ConfidenceLevel.HIGHER))));

        subscriptionService.setSubscriptionToRecipient("48700800999", SubscriptionMode.START);

        //expect
        mockMvc.perform(post("/api/v1/sms/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].classification").value("PHISHING"));
    }

    @Test
    void shouldNotReturnResponseWhenRecipientTurnedOffSubscription() throws Exception {
        //given
        String requestBody = """
                {
                  "messages": [
                    { "id": "1", "sender": "Bank", "recipient": "48700800999", "text": "Dopłać 1 PLN: http://bank-pl.com/verify" }
                  ]
                }
                """;

        subscriptionService.setSubscriptionToRecipient("48700800999", SubscriptionMode.STOP);

        //expect
        mockMvc.perform(post("/api/v1/sms/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results", hasSize(0)));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EvaluateUriClient evaluateUriClient() {
            return Mockito.mock(EvaluateUriClient.class);
        }
    }
}
