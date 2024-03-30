package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuizUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuizUser getQuizUserSample1() {
        return new QuizUser().id(1L).nickname("nickname1");
    }

    public static QuizUser getQuizUserSample2() {
        return new QuizUser().id(2L).nickname("nickname2");
    }

    public static QuizUser getQuizUserRandomSampleGenerator() {
        return new QuizUser().id(longCount.incrementAndGet()).nickname(UUID.randomUUID().toString());
    }
}
