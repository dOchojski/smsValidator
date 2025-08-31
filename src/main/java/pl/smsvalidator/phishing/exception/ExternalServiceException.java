package pl.smsvalidator.phishing.exception;

public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

