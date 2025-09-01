package pl.smsvalidator.phishing.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.smsvalidator.phishing.model.SubscriptionMode;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.smsvalidator.phishing.model.SubscriptionMode.START;
import static pl.smsvalidator.phishing.model.SubscriptionMode.STOP;

@SpringBootTest
public class SubscriptionServiceIT {

    @Autowired
    private SmsSubscriptionService subscriptionService;

    @ParameterizedTest
    @MethodSource("shouldSetSubscriptionForRecipientArguments")
    void shouldSetSubscriptionForRecipient(String recipient, SubscriptionMode mode, boolean expectedResult) {
        //when
        subscriptionService.setSubscriptionToRecipient(recipient, mode);

        //when
        boolean result = subscriptionService.isSubscriptionActive(recipient);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> shouldSetSubscriptionForRecipientArguments() {
        return Stream.of(
                Arguments.of("48690680321", START, true),
                Arguments.of("48690680321", STOP, false),
                Arguments.of(null, STOP, false),
                Arguments.of("48690680321", null, false),
                Arguments.of(null, null, false));
    }
}
