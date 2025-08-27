package pl.smsvalidator.phishing.model.dto;

import pl.smsvalidator.phishing.model.Classification;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
public class MessageResultDto {
    private String id;
    private Classification classification;
    private List<UrlEvaluationDto> urls;
}

