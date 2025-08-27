package pl.smsvalidator.phishing.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "webrisk")
public class WebRiskProperties {
    private String baseUrl;
    private String apiToken;
    private boolean allowScan = true;
    private List<String> threatTypes;
    private int timeoutMs = 5000;
}