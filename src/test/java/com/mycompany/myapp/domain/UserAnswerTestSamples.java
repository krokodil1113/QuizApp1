package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserAnswerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAnswer getUserAnswerSample1() {
        return new UserAnswer().id(1L).customAnswerText("customAnswerText1");
    }

    public static UserAnswer getUserAnswerSample2() {
        return new UserAnswer().id(2L).customAnswerText("customAnswerText2");
    }

    public static UserAnswer getUserAnswerRandomSampleGenerator() {
        return new UserAnswer().id(longCount.incrementAndGet()).customAnswerText(UUID.randomUUID().toString());
    }
}
