package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnswerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Answer getAnswerSample1() {
        return new Answer().id(1L).text("text1");
    }

    public static Answer getAnswerSample2() {
        return new Answer().id(2L).text("text2");
    }

    public static Answer getAnswerRandomSampleGenerator() {
        return new Answer().id(longCount.incrementAndGet()).text(UUID.randomUUID().toString());
    }
}
