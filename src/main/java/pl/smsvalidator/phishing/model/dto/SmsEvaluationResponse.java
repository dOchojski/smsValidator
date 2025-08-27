package pl.smsvalidator.phishing.model.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
public class SmsEvaluationResponse {
    private List<MessageResultDto> results;
}
