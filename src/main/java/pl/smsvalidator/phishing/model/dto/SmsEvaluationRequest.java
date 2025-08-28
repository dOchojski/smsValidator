package pl.smsvalidator.phishing.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsEvaluationRequest {
    @NotEmpty
    private List<SmsMessageDto> messages;
}