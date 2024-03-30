package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Feedback getFeedbackSample1() {
        return new Feedback().id(1L);
    }

    public static Feedback getFeedbackSample2() {
        return new Feedback().id(2L);
    }

    public static Feedback getFeedbackRandomSampleGenerator() {
        return new Feedback().id(longCount.incrementAndGet());
    }
}
