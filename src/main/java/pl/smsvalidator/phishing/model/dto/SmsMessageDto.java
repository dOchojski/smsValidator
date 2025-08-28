package pl.smsvalidator.phishing.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessageDto {
    @NotBlank
    private String id;
    @NotBlank
    private String sender;
    @NotBlank
    private String recipient;
    @NotBlank
    private String text;
}