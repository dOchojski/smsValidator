package pl.smsvalidator.phishing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate(WebRiskProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getTimeoutMs());
        requestFactory.setReadTimeout(properties.getTimeoutMs());

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(properties.getBaseUrl());
        restTemplate.setUriTemplateHandler(uriBuilderFactory);

        return restTemplate;
    }
}