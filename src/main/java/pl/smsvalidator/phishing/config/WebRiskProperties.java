package pl.smsvalidator.phishing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pl.smsvalidator.phishing.model.ThreatType;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "webrisk")
public class WebRiskProperties {
    private String baseUrl;
    private String apiToken;
    private Boolean allowScan;
    private List<ThreatType> threatTypes;
    private Integer timeoutMs;
}