package pl.smsvalidator.phishing.model.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
public class UrlEvaluationDto {
    private String url;
    private List<ScoreDto> scores;
}