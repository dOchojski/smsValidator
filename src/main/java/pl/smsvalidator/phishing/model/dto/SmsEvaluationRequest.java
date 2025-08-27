package pl.smsvalidator.phishing.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class SmsEvaluationRequest {
    @NotEmpty
    private List<SmsMessageDto> messages;
}