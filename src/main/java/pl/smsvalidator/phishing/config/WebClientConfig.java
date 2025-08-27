package pl.smsvalidator.phishing.config;

import org.springframework.context.annotation.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebRiskProperties props) {
        HttpClient client = HttpClient.create()
            .responseTimeout(java.time.Duration.ofMillis(props.getTimeoutMs()));
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(client))
            .baseUrl(props.getBaseUrl())
            .build();
    }
}