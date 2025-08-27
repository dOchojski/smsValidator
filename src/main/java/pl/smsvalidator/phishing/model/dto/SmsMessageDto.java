package pl.smsvalidator.phishing.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsMessageDto {
    @NotBlank
    private String id;
    @NotBlank
    private String sender;
    @NotBlank
    private String text;
}