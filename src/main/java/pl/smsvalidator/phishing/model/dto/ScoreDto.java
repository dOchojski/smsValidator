package pl.smsvalidator.phishing.model.dto;

import pl.smsvalidator.phishing.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreDto {
    private ThreatType threatType;
    private ConfidenceLevel confidence;
}

